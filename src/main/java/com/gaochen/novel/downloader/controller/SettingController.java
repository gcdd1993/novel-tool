package com.gaochen.novel.downloader.controller;

import com.gaochen.novel.downloader.domain.Config;
import com.gaochen.novel.downloader.exception.CustomException;
import com.gaochen.novel.downloader.exception.ErrorMessage;
import com.gaochen.novel.downloader.util.SettingUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class SettingController implements Initializable {

    @FXML
    private TextField pathText;

    @FXML
    private Button browseBtn;

    @FXML
    private ComboBox<Integer> threadCombox;

    @FXML
    private Button OKBtn;

    @FXML
    private Pane pane;

    @FXML
    private CheckBox mergeCheck;

    @FXML
    void browse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        directoryChooser.setTitle("选择保存目录");
        File file = directoryChooser.showDialog(pane.getScene().getWindow());
        pathText.setText(file.getPath());
    }

    @FXML
    void save(ActionEvent event) throws IOException {
        SettingUtils.write(new Config().setPath(pathText.getText()).setThread(threadCombox.getValue()).setMerge(mergeCheck.isSelected()));
        refresh();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        threadCombox.getItems().clear();
        threadCombox.getItems().addAll(10,15,20);
        try {
            refresh();
        } catch (IOException e) {
            throw new CustomException(String.format(ErrorMessage.IO_ERROR,e.getMessage()));
        }
    }

    private void refresh() throws IOException {
        Config config = SettingUtils.read();
        pathText.setText(config.getPath());
        threadCombox.setValue(config.getThread());
        mergeCheck.setSelected(config.isMerge());
    }
}
