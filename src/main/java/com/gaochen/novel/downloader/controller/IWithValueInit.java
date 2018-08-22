package com.gaochen.novel.downloader.controller;

import java.io.IOException;

/**
 * Created by gaochen on 2018/5/17.
 */
public interface IWithValueInit<T> {
    void initData(T t) throws IOException;
}
