package com.company.coursework;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private AnchorPane cellPain;

    public AnchorPane getCellPain() {
        return cellPain;
    }

    @FXML
    void initialize() {

    }

}
