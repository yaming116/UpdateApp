package me.shenfan.updateapp;

/**
 * Created by Sun on 2016/6/30.
 *
 * bindService call back
 */
public interface UpdateProgressListener {
    /**
     * download start
     */
     void start();

    /**
     * update download progress
     * @param progress
     */
     void update(int progress);

    /**
     * download success
     */
     void success();

    /**
     * download error
     */
     void error();
}
