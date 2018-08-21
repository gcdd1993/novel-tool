package com.gaochen.novel.downloader.controller;

import com.gaochen.novel.downloader.Main;
import com.gaochen.novel.downloader.domain.Novel;
import com.gaochen.novel.downloader.domain.NovelChapter;
import com.gaochen.novel.downloader.exception.CustomException;
import com.gaochen.novel.downloader.exception.ErrorMessage;
import com.gaochen.novel.downloader.service.ContentService;
import com.gaochen.novel.downloader.util.GUIUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author Segp-Group 3
 */
public class MainController implements Initializable {

	private static final String BASE_INFO = "查找到%d个结果!";
	private static final String BASE_CHAPTER_TITLE = "%s  共%d章";
	private static final String BASE_INFO1 = "%s 共找到%d章";

	private static final ContentService CONTENT_SERVICE = ContentService.getInstance();

	@FXML
	private TableView<Novel> tableViewNovel;

	@FXML
	private TableColumn<Novel, String> typeCol;

	@FXML
	private TableColumn<Novel, String> operationCol;

	@FXML
	private Button btnSearch;

	@FXML
	private TableColumn<Novel, String> statusCol;

	@FXML
	private TableColumn<Novel, String> updateDateCol;

	@FXML
	private TableColumn<Novel, String> updateCol;

	@FXML
	private TableColumn<Novel, String> authorCol;

	@FXML
	private TableColumn<Novel, String> chapterCountCol;

	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private TableColumn<Novel, String> nameCol;

	@FXML
	private TextField textKey;

	@FXML
	private Label infoText;

	private static Map<Integer,Stage> stageMap = new HashMap<>();

	@FXML
	void search(ActionEvent event) {
		stageMap.clear();
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				tableViewNovel.getItems().clear();
				String key = textKey.getText();
				List<Novel> novelList = CONTENT_SERVICE.search(key);
				updateMessage(String.format(BASE_INFO,novelList.size()));
				novelList.forEach(novel -> {
					try {
						List<NovelChapter> chapters = CONTENT_SERVICE.list(novel.getUrl());
						updateMessage(String.format(BASE_INFO1,novel.getName(),chapters.size()));
						novel.setNovelChapterList(chapters);
						tableViewNovel.getItems().add(novel);
					} catch (IOException e) {
						throw new CustomException(String.format(ErrorMessage.IO_ERROR,e.getMessage()));
					}
				});
				return null;
			}
		};
		infoText.textProperty().unbind();
		infoText.textProperty().bind(task.messageProperty());
		new Thread(task).start();
	}

	@FXML
	void setting(ActionEvent event) throws IOException {
		GUIUtil.showDialog(Main.FXMLS + "SettingFXML.fxml", "设置", null);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
		statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		updateDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUpdateDate()));
		updateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUpdate()));
		authorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
		nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		chapterCountCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNovelChapterList().size())));

		operationCol.setCellFactory(col -> new TableCell<Novel, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				this.setText(null);
				this.setGraphic(null);
				if (!empty) {
					Button chapterBtn = new Button("查看详情");
					this.setGraphic(chapterBtn);
					chapterBtn.setOnMouseClicked((me) -> {
						Stage maybeExist = stageMap.get(this.getIndex());
						if(maybeExist != null) {
							maybeExist.show();
						}else {
							Novel novel = this.getTableView().getItems().get(this.getIndex());
							try {
								Stage stage = GUIUtil.showDialog(Main.FXMLS + "ChapterFXML.fxml", String.format(BASE_CHAPTER_TITLE, novel.getName(), novel.getNovelChapterList().size()), novel);
								stageMap.put(this.getIndex(),stage);
							} catch (IOException e) {
								throw new CustomException(String.format(ErrorMessage.IO_ERROR,e.getMessage()));
							}
						}
					});
				}
			}

		});

	}
}