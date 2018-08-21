package com.gaochen.novel.downloader.util;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class FileUtils {
    public static String specificatFileName(String fileName) {
        return fileName.replaceAll("[?\\\\/:*\"<>|]+","");
    }

}
