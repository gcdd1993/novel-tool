package com.gaochen.novel.downloader.controller;

import com.gaochen.novel.downloader.domain.Config;
import com.gaochen.novel.downloader.domain.Novel;
import com.gaochen.novel.downloader.domain.NovelChapter;
import com.gaochen.novel.downloader.service.Downloader;
import com.gaochen.novel.downloader.util.SettingUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class ChapterController implements Initializable, IWithValueInit<Novel> {

    private static final String BASE_INFO = "共 %d 章";
    private static final String BASE_TASK_INFO = "正在下载 %s ";

    private static Novel novel;

    @FXML
    private TableView<NovelChapter> chapterTable;

    @FXML
    private TableColumn<NovelChapter, String> nameCol;

    @FXML
    private TableColumn<NovelChapter, String> urlCol;

    @FXML
    private Label downloadProgress;

    @FXML
    private ProgressBar downloadProgressBar;

    private Task<Void> task;

    private Downloader downloader;

    @Override
    public void initData(Novel novel) {
        ChapterController.novel = novel;
        chapterTable.getItems().clear();
        chapterTable.getItems().addAll(novel.getNovelChapterList());
    }

    @FXML
    void download(ActionEvent event) throws IOException {
        Config config = SettingUtils.read();
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                downloader = new Downloader(config.getThread(), novel.getName(), config.getPath(), novel.getNovelChapterList());
                downloader.start();
                while (downloader.checkIfRunning() != 2) {
                    long count = downloader.currentSuccess();
                    int max = novel.getNovelChapterList().size();
                    updateMessage(String.format(BASE_TASK_INFO,count) + " " + String.format(BASE_INFO,max));
                    updateProgress(count,max);
                    Thread.sleep(1000);
                }
                updateMessage("下载完成!");
                return null;
            }

        };
        downloadProgressBar.progressProperty().unbind();
        downloadProgress.textProperty().unbind();
        downloadProgressBar.progressProperty().bind(task.progressProperty());
        downloadProgress.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }

    @FXML
    void cancel(ActionEvent event) {
        if(downloader != null) {
            downloader.stopAll();
        }
        if(task != null) {
            task.cancel(true);
        }
        downloadProgressBar.progressProperty().unbind();
        downloadProgress.textProperty().unbind();
        downloadProgressBar.setProgress(0);
        downloadProgress.setText(String.format("被强制停止!已下载 %d 章",downloader.currentSuccess()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        urlCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUrl()));
    }

}
