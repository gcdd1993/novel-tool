package com.gaochen.novel.downloader.domain;

import com.gaochen.novel.downloader.util.UrlUtils;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
@Data
public class ResultBase {
    protected String name;
    protected String url;

    public void setUrl(String url) {
        this.url = UrlUtils.canonicalizeUrl(url,"https://www.dingdiann.com");
    }
}
