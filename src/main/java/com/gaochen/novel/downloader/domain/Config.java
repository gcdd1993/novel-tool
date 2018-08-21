package com.gaochen.novel.downloader.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
@Data
@Accessors(chain = true)
public class Config {
    private int thread;
    private String path;
}
