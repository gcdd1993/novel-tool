package com.gaochen.novel.downloader.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Accessors(chain = true)
public class Novel extends ResultBase {
    private String type; //作品分类
    private String update; //最新章节
    private String author; //作者
    private String updateDate; //更新时间
    private String status; //状态
    private List<NovelChapter> novelChapterList; //章节数
}
