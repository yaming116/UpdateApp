package android.support.v4.app;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by ucmed on 2018/3/8.
 */
public class LocalNotificationCompat extends NotificationCompat {
    public static class Builder extends NotificationCompat.Builder{
        String mChannelId;
        /**
         * Constructor.
         *
         * Automatically sets the when field to {@link System#currentTimeMillis()
         * System.currentTimeMillis()} and the audio stream to the
         * {@link Notification#STREAM_DEFAULT}.
         *
         * @param context A {@link Context} that will be used to construct the
         *      RemoteViews. The Context will not be held past the lifetime of this
         *      Builder object.
         * @param channelId The constructed Notification will be posted on this
         *      NotificationChannel.
         */
        public Builder(@NonNull Context context, @NonNull String channelId) {
            super(context);
            mContext = context;
            mChannelId = channelId;

            // Set defaults to match the defaults of a Notification
            mNotification.when = System.currentTimeMillis();
            mNotification.audioStreamType = Notification.STREAM_DEFAULT;
            mPriority = PRIORITY_DEFAULT;
            mPeople = new ArrayList<String>();
        }

        /**
         * @deprecated use {@link #LocalNotificationCompat.Builder(Context,String)} instead.
         * All posted Notifications must specify a NotificationChannel Id.
         */
        @Deprecated
        public Builder(Context context) {
            this(context, null);
        }


        /**
         * Specifies the channel the notification should be delivered on.
         *
         * No-op on versions prior to {@link android.os.Build.VERSION_CODES#O} .
         */
        public Builder setChannelId(@NonNull String channelId) {
            mChannelId = channelId;
            return this;
        }
    }


}
