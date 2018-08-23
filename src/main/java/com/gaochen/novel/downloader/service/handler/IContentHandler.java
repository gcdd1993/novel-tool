package com.gaochen.novel.downloader.service.handler;

import com.gaochen.novel.downloader.domain.Novel;
import com.gaochen.novel.downloader.domain.NovelChapter;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/23
 */
public interface IContentHandler {

    /**
     * 通过关键词查找小说
     */
    List<Novel> search(String key) throws IOException;

    /**
     * 通过小说链接获取小说章节列表
     */
    List<NovelChapter> list(String url) throws IOException;

    /**
     * 获取小说正文
     */
    String text(String url) throws IOException;
}
