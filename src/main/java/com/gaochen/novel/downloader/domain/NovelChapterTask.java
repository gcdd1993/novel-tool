package com.gaochen.novel.downloader.domain;

import com.gaochen.novel.downloader.service.Downloader;
import javafx.concurrent.Task;
import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/22
 */
public class NovelChapterTask extends Task<Void> {

    private static final String BASE_INFO = "共 %d 章";
    private static final String BASE_TASK_INFO = "正在下载 %s ";

    private Downloader downloader;
    @Getter
    private Config config;
    @Getter
    private Novel novel;

    public NovelChapterTask(Config config, Novel novel) {
        this.config = config;
        this.novel = novel;
    }

    @Override
    protected void succeeded() {
        updateMessage("下载完成!");
    }

    @Override
    protected void failed() {
        updateMessage("下载出错!");
    }

    @Override
    protected void cancelled() {
        downloader.stopAll();
        updateMessage(String.format("被强制停止!已下载 %d 章",downloader.currentSuccess()));
    }

    @Override
    protected Void call() throws Exception {
        downloader = new Downloader(config.getThread(), novel.getName(),config.getPath(),novel.getNovelChapterList(),config.isMerge());
        downloader.start();
        while (downloader.checkIfRunning() != Downloader.STAT_STOPPED) {
            if(downloader.checkIfRunning() == Downloader.STAT_DOWNLOAD) {
                long count = downloader.currentSuccess();
                int max = novel.getNovelChapterList().size();
                updateMessage(String.format(BASE_TASK_INFO,count) + " " + String.format(BASE_INFO,max));
                updateProgress(count,max);
            }else if(downloader.checkIfRunning() == Downloader.STAT_STORE) {
                updateMessage("正在保存到文件...");
            }
            Thread.sleep(1000);
        }
        return null;
    }

}
