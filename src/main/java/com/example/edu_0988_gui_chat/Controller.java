package com.example.edu_0988_gui_chat;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ListView<String> messagesListView;
    @FXML
    public ListView<String> usersOnlineListView;
    @FXML
    public TextField inputTextField;
    @FXML
    public Label onlineCountLabel;
    @FXML
    public Button button;

    private ChatService chatService;

    @FXML
    private void send() {
        String text = inputTextField.getText();
        chatService.send(text);
        inputTextField.clear();
        inputTextField.requestFocus();
    }

    @FXML
    private void connect() {
        // Запуск модели
        new Thread(chatService).start();
        // Переназначение кнопки
        button.setOnAction(actionEvent -> send());
        button.setText("Отправить");
        inputTextField.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatService = new ChatService();

        ObservableList<String> usersOnlineList = usersOnlineListView.getItems();
        // Устанавливаем обработчик изменения списка пользователей в модели
        chatService.getUsersOnlineList().addListener((ListChangeListener<String>) item -> {
            while (item.next()) {
                if (item.wasAdded()) {
                    Platform.runLater(() ->
                            usersOnlineList.addAll(item.getFrom(), item.getAddedSubList()));
                }
                if (item.wasRemoved()) {
                    Platform.runLater(() ->
                            usersOnlineList.removeAll(item.getRemoved()));
                }
                Platform.runLater(() -> onlineCountLabel.setText(String.valueOf(usersOnlineList.size())));
            }
        });

        chatService.getMessagesList().addListener((ListChangeListener<String>) messages -> {
            while (messages.next()) {
                if (messages.wasAdded())
                    Platform.runLater(() ->
                            messagesListView.getItems().addAll(messages.getFrom(), messages.getAddedSubList()));
            }
        });

        messagesListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView param) {
                return new ListCell<>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            if (item.startsWith("Вы: ") || item.startsWith("Вы -> ")) {
                                item = item.replaceAll("Вы: ", "").replaceAll("Вы ", "");
                                setAlignment(Pos.CENTER_RIGHT);
                            }
                            setText(item);
                        }
                    }
                };
            }
        });

        usersOnlineListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(item);
                            setOnMouseClicked(null);
                        } else {
                            setText(item);
                            setOnMouseClicked(event -> {
                                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                                    inputTextField.clear();
                                    inputTextField.requestFocus();
                                    inputTextField.appendText("/m " + getText() + " ");
                                }
                            });
                        }
                    }
                };
            }
        });
    }
}