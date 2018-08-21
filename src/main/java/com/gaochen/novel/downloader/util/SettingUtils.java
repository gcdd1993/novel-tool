package com.gaochen.novel.downloader.util;

import com.gaochen.novel.downloader.domain.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class SettingUtils {
    private static final String PROPERTIES_FILE_PATH = System.getProperty("user.dir") + "/conf.properties";

    public static Config read() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
        return new Config().setThread(Integer.valueOf(properties.getProperty("thread"))).setPath(properties.getProperty("path"));
    }

    public static void write(Config config) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("path",config.getPath());
        properties.setProperty("thread",String.valueOf(config.getThread()));
        FileOutputStream outputStream = new FileOutputStream(PROPERTIES_FILE_PATH);
        properties.store(outputStream,"novelConfig");
    }

    public static void init() throws IOException {
        File file = new File(PROPERTIES_FILE_PATH);
        if(!file.exists()) {
            Config config = new Config().setPath(System.getProperty("user.dir") + "\\novels").setThread(20);
            write(config);
        }
    }

}
