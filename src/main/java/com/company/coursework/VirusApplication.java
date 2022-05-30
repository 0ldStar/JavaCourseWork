package com.company.coursework;

import com.company.coursework.Models.Cell;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class VirusApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VirusApplication.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Virus");
        mainController = fxmlLoader.getController();
        initialization();
        stage.setScene(scene);
        stage.show();
    }


    private void initialization() throws FileNotFoundException {
        linkedList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Cell cell = new Cell(i * 70, j * 70);
                linkedList.add(cell);
                mainController.getCellPain().getChildren().add(cell.getImageView());
            }
        }
    }

    public void addClickCount() {
        clickCount++;
        if (clickCount % 3 == 0) moveStatus = !moveStatus;
    }

    public static void main(String[] args) {
        launch();
    }

    public static VirusApplication getInstance() {
        VirusApplication localInstance = instance;
        if (localInstance == null) {
            synchronized (VirusApplication.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new VirusApplication();
                }
            }
        }
        return localInstance;
    }

    private static volatile VirusApplication instance;
    private int clickCount = 0;
    public boolean moveStatus = true; // todo need to fix
    private MainController mainController;
    public LinkedList<Cell> linkedList;

}