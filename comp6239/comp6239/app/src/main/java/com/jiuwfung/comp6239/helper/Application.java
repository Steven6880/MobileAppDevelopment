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
        // 得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        // 创建所有的对应的文件夹
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        // 删除旧的一些缓存为文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // 返回一个当前时间戳的目录文件地址
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }
}
