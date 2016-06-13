package me.shenfan.updateapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sun on 2016/6/12.
 */
public class UpdateService extends Service {
    public static final String TAG =  "UpdateService";
    public static boolean DEBUG = false;

    //下载大小通知频率
    public static final int UPDATE_NUMBER_SIZE = 20 * 1024;

    //params
    private static String URL = "download_url";
    private static String ICO_RES_ID = "ico_res_id";
    private static String ICO_SMALL_RES_ID = "ico_small_res_id";


    private boolean startDownload;//开始下载
    private String downloadUrl;
    private int icoResId;
    private int icoSmallResId;

    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private int notifyId;
    private String appName;

    /**
     * whether debug
     */
    public static void debug(){
        DEBUG = true;
    }

    /**
     * start download
     *
     * @param context
     * @param url
     * @param icoResId
     * @param icoSmallResId
     */
    public static void start(Context context, String url, int icoResId, int icoSmallResId){
        Intent intent = new Intent();
        intent.setClass(context, UpdateService.class);
        intent.putExtra(URL, url);
        intent.putExtra(ICO_RES_ID, icoResId);
        intent.putExtra(ICO_SMALL_RES_ID, icoSmallResId);
        context.startService(intent);
    }


    private static Intent installIntent(String path){
        Uri uri = Uri.fromFile(new File(path));
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        return installIntent;
    }

    private static Intent webLauncher(String downloadUrl){
        Uri download = Uri.parse(downloadUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, download);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private static String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return "noName.apk";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

    private static File getDownloadDir(Context context){
        File downloadDir = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            downloadDir = new File(context.getExternalCacheDir(), "update");
        } else {
            downloadDir = new File(context.getCacheDir(), "update");
        }
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        return downloadDir;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        appName = getApplicationName();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!startDownload && intent != null){
            startDownload = true;
            downloadUrl = intent.getStringExtra(URL);
            icoResId = intent.getIntExtra(ICO_RES_ID, -1);
            icoSmallResId = intent.getIntExtra(ICO_SMALL_RES_ID, -1);
            notifyId = startId;
            buildNotification();
            new DownloadApk(this).execute(downloadUrl);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    private void buildNotification(){
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.update_app_model_prepare, appName))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icoSmallResId)
                .setProgress(100, 1, false)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(), icoResId))
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        manager.notify(notifyId, builder.build());
    }

    private void start(){
        builder.setContentTitle(appName);
        builder.setContentText(getString(R.string.update_app_model_prepare, 1));
        manager.notify(notifyId, builder.build());
    }

    private void update(int progress){
        builder.setProgress(100, progress, false);
        builder.setContentText(getString(R.string.update_app_model_progress, progress));
        manager.notify(notifyId, builder.build());
    }

    private void success(String path) {
        builder.setProgress(0, 0, false);
        builder.setContentText(getString(R.string.update_app_model_success));
        Intent i = installIntent(path);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        builder.setDefaults(Notification.FLAG_AUTO_CANCEL);
        Notification n = builder.build();
        n.contentIntent = intent;
        manager.notify(notifyId, n);
        startActivity(i);
        stopSelf();
    }

    private void error(){
        Intent i = webLauncher(downloadUrl);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentText(getString(R.string.update_app_model_error));
        builder.setContentIntent(intent);
        builder.setProgress(0, 0, false);
        Notification n = builder.build();
        n.contentIntent = intent;
        manager.notify(notifyId, n);
        stopSelf();
    }

    private static class DownloadApk extends AsyncTask<String, Integer, String>{

        private WeakReference<UpdateService> updateServiceWeakReference;

        public DownloadApk(UpdateService service){
            updateServiceWeakReference = new WeakReference<UpdateService>(service);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UpdateService service = updateServiceWeakReference.get();
            if (service != null){
                service.start();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            final String downloadUrl = params[0];

            final File file = new File(UpdateService.getDownloadDir(updateServiceWeakReference.get()),
                    UpdateService.getSaveFileName(downloadUrl));
            if (DEBUG){
                Log.d(TAG, "download url is " + downloadUrl);
                Log.d(TAG, "download apk cache at " + file.getAbsolutePath());
            }
            File dir = file.getParentFile();
            if (!dir.exists()){
                dir.mkdirs();
            }

            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;
            int updateTotalSize = 0;
            URL url;
            try {
                url = new URL(downloadUrl);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(20000);
                httpConnection.setReadTimeout(20000);

                if (DEBUG){
                    Log.d(TAG, "download status code: " + httpConnection.getResponseCode());
                }

                if (httpConnection.getResponseCode() != 200) {
                    return null;
                }

                updateTotalSize = httpConnection.getContentLength();

                if (file.exists()) {
                    if (updateTotalSize == file.length()) {
                        // 下载完成
                        return file.getAbsolutePath();
                    } else {
                        file.delete();
                    }
                }
                file.createNewFile();
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(file, false);
                byte buffer[] = new byte[4096];

                int readsize = 0;
                int currentSize = 0;
                int notifySize = 0;

                while ((readsize = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readsize);
                    currentSize += readsize;
                    notifySize += readsize;
                    if (notifySize > UPDATE_NUMBER_SIZE) {
                        notifySize = 0;
                        publishProgress((currentSize * 100 / updateTotalSize));
                    }
                }
                // download success
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (DEBUG){
                Log.d(TAG, "current progress is " + values[0]);
            }
            UpdateService service = updateServiceWeakReference.get();
            if (service != null){
                service.update(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UpdateService service = updateServiceWeakReference.get();
            if (service != null){
                if (s != null){
                    service.success(s);
                }else {
                    service.error();
                }
            }
        }
    }

}
