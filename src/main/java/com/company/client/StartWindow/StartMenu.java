package com.company.client.StartWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenu {

    public StartMenu() {
    }

    public static void showMenu() throws IOException {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(StartMenu.class.getResource("startMenu.fxml"));
        Scene root = new Scene(fxmlLoader.load());
        window.setScene(root);
        window.setTitle("Start Menu");
        StartMenuController.connectionFlag = false;
        window.showAndWait();
    }

    public static StartMenu getInstance() {
        StartMenu localInstance = instance;
        if (localInstance == null) {
            synchronized (StartMenu.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new StartMenu();
                }
            }
        }
        return localInstance;
    }

    private static volatile StartMenu instance;

    public Stage getStage() {
        return window;
    }

    private static Stage window;
}
