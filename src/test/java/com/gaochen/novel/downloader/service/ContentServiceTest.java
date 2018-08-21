package com.gaochen.novel.downloader.service;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class ContentServiceTest {

    @Test
    public void search() throws IOException {
        System.out.println(ContentService.getInstance().search("灵车"));
    }

    @Test
    public void list() throws IOException {
        System.out.println(ContentService.getInstance().list("https://www.dingdiann.com/ddk61724/"));
    }
}