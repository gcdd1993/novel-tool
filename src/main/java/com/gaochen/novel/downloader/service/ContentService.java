package com.gaochen.novel.downloader.service;

import com.gaochen.novel.downloader.domain.Novel;
import com.gaochen.novel.downloader.domain.NovelChapter;
import com.gaochen.novel.downloader.domain.ResultBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 * 内容获取服务
 */
public class ContentService {
    private static final String BASE_URL = "https://www.dingdiann.com/searchbook.php?keyword=%s";

    private ContentService() {}

    private volatile static ContentService instance;

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

    /**
     * 通过关键词查找小说
     */
    public List<Novel> search(String key) throws IOException {
        key = URLEncoder.encode(key,"UTF-8");
        String url = String.format(BASE_URL,key);
        Document document = Jsoup.connect(url).validateTLSCertificates(false).get();
        Elements elements = Xsoup.compile("//div[@class='novelslist2']/ul/li").evaluate(document).getElements();
        if(elements != null && !elements.isEmpty()) {
            return elements.stream().map(element -> {
                try {
                    Novel novel = new Novel()
                            .setType(Xsoup.compile("//span[@class='s1']/text()").evaluate(element).get().trim())
                            .setUpdate(Xsoup.compile("//span[@class='s3']/a/text()").evaluate(element).get().trim())
                            .setAuthor(Xsoup.compile("//span[@class='s4']/text()").evaluate(element).get().trim())
                            .setUpdateDate(Xsoup.compile("//span[@class='s6']/text()").evaluate(element).get().trim())
                            .setStatus(Xsoup.compile("//span[@class='s7']/text()").evaluate(element).get().trim());
                    novel.setName(Xsoup.compile("//span[@class='s2']/a/text()").evaluate(element).get().trim());
                    novel.setUrl(Xsoup.compile("//span[@class='s2']/a/@href").evaluate(element).get().trim());
                    return novel;
                } catch (Exception ex) {
                    return null;
                }
            }).filter(this::validate).collect(Collectors.toList());
        }else {
            return Collections.emptyList();
        }
    }

    /**
     * 通过小说链接获取小说章节列表
     */
    public List<NovelChapter> list(String url) throws IOException {
        Document document = Jsoup.connect(url).validateTLSCertificates(false).get();
        Elements elements = Xsoup.compile("//div[@id='list']/dl/dd/a").evaluate(document).getElements();
        if(elements != null && !elements.isEmpty()) {
            return elements.stream().map(element -> {
                try {
                    NovelChapter novelChapter = new NovelChapter();
                    novelChapter.setName(Xsoup.compile("//text()").evaluate(element).get());
                    novelChapter.setUrl(Xsoup.compile("//@href").evaluate(element).get());
                    return novelChapter;
                } catch (Exception ex) {
                    return null;
                }
            }).filter(this::validate).distinct().collect(Collectors.toList());
        }else {
            return Collections.emptyList();
        }
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
        // //div[@id='content']
        Document document = Jsoup.connect(url).validateTLSCertificates(false).get();
        try {
            String text = Xsoup.compile("//div[@id='content']/text()").evaluate(document)
                    .get();
            return text;
        } catch (Exception ex) {
            return null;
        }
//        return
//                .replace("chaptererror();章节错误,点此举报(免注册),举报后维护人员会在两分钟内校正章节内容,请耐心等待,并刷新页面。","");
    }

}
