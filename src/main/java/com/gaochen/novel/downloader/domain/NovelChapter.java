package com.gaochen.novel.downloader.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Accessors(chain = true)
public class NovelChapter extends ResultBase {
}
