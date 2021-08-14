module com.example.edu_0988_gui_chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens com.example.edu_0988_gui_chat to javafx.fxml;
    exports com.example.edu_0988_gui_chat;
}