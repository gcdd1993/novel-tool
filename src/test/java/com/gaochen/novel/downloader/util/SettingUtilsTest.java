package com.gaochen.novel.downloader.util;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class SettingUtilsTest {

    @Test
    public void read() throws IOException {
        System.out.println(SettingUtils.read());
    }

    @Test
    public void write() {
    }

    @Test
    public void init() throws IOException {
        SettingUtils.init();
    }
}