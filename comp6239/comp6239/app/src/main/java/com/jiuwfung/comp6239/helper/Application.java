package com.jiuwfung.comp6239.helper;

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
        // get cache of current portrait
        File dir = new File(getCacheDirFile(), "portrait");
        // create dir for it
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        // delete some outdated files
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }
}
