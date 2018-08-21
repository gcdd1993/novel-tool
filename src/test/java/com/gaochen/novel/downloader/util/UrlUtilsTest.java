package com.gaochen.novel.downloader.util;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class UrlUtilsTest {

    @Test
    public void canonicalizeUrl() {
        System.out.println(UrlUtils.canonicalizeUrl("/ddk61724/","https://www.dingdiann.com"));
    }
}