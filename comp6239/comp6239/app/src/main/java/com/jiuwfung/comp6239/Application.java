package com.jiuwfung.comp6239;

import android.os.SystemClock;

import java.io.File;

public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * Current APP CacheDirFile
     * @return
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * Current Portrait CacheDirFile
     * @return
     */
    public static File getPortraitTmpFile() {
        // get the Cache Path
        File dir = new File(getCacheDirFile(), "portrait");
        // create all dir needs
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        // delete outdated cache files
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
        // return a dir of current time
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }

    public static File getPortrait(){
        // get the Cache Path
        File dir = new File(getCacheDirFile(), "portrait");
        // create all dir needs
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        // delete outdated cache files
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
        // return a dir of current time
        File path = new File(dir, String.valueOf(SystemClock.uptimeMillis()));
        return path;
    }
}
