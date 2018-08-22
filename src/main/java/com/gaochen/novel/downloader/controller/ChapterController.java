package com.gaochen.novel.downloader.controller;

import com.gaochen.novel.downloader.domain.Novel;
import com.gaochen.novel.downloader.domain.NovelChapter;
import com.gaochen.novel.downloader.domain.NovelChapterTask;
import com.gaochen.novel.downloader.util.SettingUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Worker;
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

    private NovelChapterTask task;

    @Override
    public void initData(Novel novel) throws IOException {
        task = new NovelChapterTask(SettingUtils.read(),novel);
        chapterTable.getItems().clear();
        chapterTable.getItems().addAll(novel.getNovelChapterList());
    }

    @FXML
    void download(ActionEvent event) {
        if(!task.getState().equals(Worker.State.READY)) {
            task = new NovelChapterTask(task.getConfig(),task.getNovel());
        }
        downloadProgressBar.progressProperty().unbind();
        downloadProgress.textProperty().unbind();
        downloadProgressBar.progressProperty().bind(task.progressProperty());
        downloadProgress.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }

    @FXML
    void cancel(ActionEvent event) {
        task.cancel(true);
        downloadProgressBar.progressProperty().unbind();
        downloadProgress.textProperty().unbind();
        downloadProgressBar.setProgress(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        urlCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUrl()));
    }

}
