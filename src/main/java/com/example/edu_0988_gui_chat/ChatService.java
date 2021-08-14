package com.example.edu_0988_gui_chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ChatService implements Runnable {

    private final String HOSTNAME = "localhost";
    private final int PORT = 8188;

    private final ObservableList<String> usersOnlineList;
    private final ObservableList<String> messagesList;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ChatService() {
        usersOnlineList = FXCollections.observableArrayList();
        messagesList = FXCollections.observableArrayList();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(HOSTNAME, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (socket.isConnected()) {
                String message = in.readUTF();

                if (message.startsWith("Сейчас онлайн: ")) {
                    putUsersOnline(message);
                    continue;
                }
                if (message.startsWith("К нам присоединился ")) {
                    usersOnlineList.add(message.replaceAll("К нам присоединился ", ""));
                }
                if (message.contains(" покинул чат")) {
                    usersOnlineList.remove(message.replaceAll(" покинул чат", ""));
                }
                messagesList.add(message);
            }
        } catch (IOException e) {
            //Ошибка соединения
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void putUsersOnline(String message) {
        String[] usersOnline = message.replaceAll("Сейчас онлайн: \\[", "")
                .replaceAll("]", "").split(", ");
        usersOnlineList.setAll(usersOnline);
    }


    public void send(String text) {
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> getMessagesList() {
        return messagesList;
    }

    public ObservableList<String> getUsersOnlineList() {
        return usersOnlineList;
    }

}
