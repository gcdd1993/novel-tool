package com.gaochen.novel.downloader;

import com.gaochen.novel.downloader.controller.MainController;
import com.gaochen.novel.downloader.exception.CustomException;
import com.gaochen.novel.downloader.util.GUIUtil;
import com.gaochen.novel.downloader.util.SettingUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Segp-Group 3
 */
public class Main extends Application {
	
	/**
	 * Holds a reference to the resource folder of all fxml files
	 */
	public static String FXMLS = "/fxml/";
	public static String CSS = "/css/";
	public static String IMAGES = "/img/";
	
	MainController object = new MainController();
	static Stage stage;
	
	public static Scene sceneCopy;
	
	private static StackPane pane = new StackPane();
	
	@Override
	public void start(Stage stage) throws Exception {
		Thread.setDefaultUncaughtExceptionHandler((t,ex) -> {
			if(ex instanceof CustomException) {
				GUIUtil.alert(ex.getMessage());
			}else {
				GUIUtil.alert("【未知错误】\r\n" + ex.getMessage());
			}
		});
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(FXMLS+"MainFXML.fxml"));
		Parent root = loader.load();
		
		// the RootBorder is get to show pin dialoge box that will appear on a
		// screen
		pane.getChildren().add(root);
		
		Scene scene = new Scene(pane);
		
		scene.setOnKeyPressed(event -> {
			//启动事件
		});
		
		stage.setTitle("Novel Tool");
		stage.setScene(scene);
		stage.show();
		
		setStage(stage);
		setScene(scene);

		SettingUtils.init();
		
	}
	
	public static StackPane getPane() {
		return pane;
	}
	
	public void setPane(StackPane pane) {
		this.pane = pane;
	}
	
	private void setScene(Scene scene) {
		sceneCopy = scene;
	}
	
	public static Scene getScene() {
		return sceneCopy;
	}

	@SuppressWarnings("static-access")
	private void setStage(Stage stage) {
		// TODO Auto-generated method stub
		this.stage = stage;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	
	public static void main(String[] args) {
		//初始化
		launch(args);
		System.exit(1);
	}
	
}
