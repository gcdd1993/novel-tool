package com.gaochen.novel.downloader.util;

import lombok.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class FileUtils {
    /**
     * 文件名合法性纠正
     */
    public static String specificatFileName(String fileName) {
        return fileName.replaceAll("[?\\\\/:*\"<>|]+","");
    }

    /**
     * 字符串保存至文件
     */
    public static void store2File(String fileName,@NonNull String content) throws IOException {
        File file = new File(fileName);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(content);
        bufferedWriter.flush();
        bufferedWriter.close();
        fileWriter.close();
    }

}
