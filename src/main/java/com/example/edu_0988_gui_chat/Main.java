package com.example.edu_0988_gui_chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("sample.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("Chat 0988!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}