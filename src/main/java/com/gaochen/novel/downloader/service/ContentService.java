package com.gaochen.novel.downloader.service;

import com.gaochen.novel.downloader.domain.Novel;
import com.gaochen.novel.downloader.domain.NovelChapter;
import com.gaochen.novel.downloader.domain.ResultBase;
import com.gaochen.novel.downloader.service.handler.IContentHandler;
import lombok.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 * 内容获取服务
 */
public class ContentService {
    private ContentService() {}

    private volatile static ContentService instance;

    private IContentHandler contentHandler;

    public static ContentService getInstance() {
        if(instance == null) {
            synchronized (ContentService.class) {
                if(instance == null) {
                    instance = new ContentService();
                }
            }
        }
        return instance;
    }

    public void registerHandler(@NonNull IContentHandler newContentHandler) {
        if(contentHandler == null || !contentHandler.equals(newContentHandler)) {
            contentHandler = newContentHandler;
        }
    }

    /**
     * 通过关键词查找小说
     */
    public List<Novel> search(String key) throws IOException {
        return contentHandler.search(key).stream().filter(this::validate).collect(Collectors.toList());
    }

    /**
     * 通过小说链接获取小说章节列表
     */
    public List<NovelChapter> list(String url) throws IOException {
        return removeDuplicateWithOrder(contentHandler.list(url).stream().filter(this::validate).collect(Collectors.toList()));
    }

    /**
     * 验证内容有效性
     */
    private <T extends ResultBase> boolean validate(T t) {
        return t != null && t.getName() != null;
    }

    /**
     * 获取小说正文
     */
    public String text(String url) throws IOException {
        return contentHandler.text(url);
    }

    /**
     * 去重并维持顺序,有重复保留最后一个
     */
    private <T> List<T> removeDuplicateWithOrder(List<T> list) {
        Set<T> set = new HashSet<>();
        List<T> newList = new ArrayList<>();
        list.forEach(element -> {
            if(set.add(element)) {
                newList.add(element);
            }else {
                newList.remove(element);
                newList.add(element);
            }
        });
        list.clear();
        list.addAll(newList);
        return list;
    }

}
