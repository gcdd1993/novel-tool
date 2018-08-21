package com.gaochen.novel.downloader.util;

import com.gaochen.novel.downloader.controller.IWithValueInit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by gaochen on 2018/5/16.
 */
public class GUIUtil {
    /**
     * 提示弹框
     * @param msg
     */
    public static void alert(String msg) {
        Alert information = new Alert(Alert.AlertType.INFORMATION,msg);
        information.setTitle("提示"); //设置标题，不设置默认标题为本地语言的information
        information.setHeaderText("消息"); //设置头标题，默认标题为本地语言的information
        Button infor = new Button("show Information");
        infor.setOnAction((e)->{
            information.showAndWait(); //显示弹窗，同时后续代码等挂起
        });
        information.show();
    }

    @SuppressWarnings("unchecked")
    public static<T> Stage showDialog(String fxmlUri, String title, T t) throws IOException {
        FXMLLoader loader = new FXMLLoader(GUIUtil.class.getResource(fxmlUri));
        Stage stage = new Stage(StageStyle.DECORATED);
        Scene scene = new Scene(
                (Pane) loader.load()
        );
        stage.setTitle(title);
        stage.setScene(scene);
        if(t != null) {
            ((IWithValueInit) loader.getController()).initData(t);
        }
        stage.setResizable(false);
        stage.show();
        return stage;
    }

}
