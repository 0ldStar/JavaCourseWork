package com.company.coursework;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private AnchorPane cellPain;
    @FXML
    private TextField playerMoveTextField;
    @FXML
    private TextField clickCountTextField;
    @FXML
    private Button skipMoveButton;

    @FXML
    void skipMoveButtonClick(ActionEvent event) {
        VirusApplication.getInstance().skipMove();
    }

    public Button getSkipMoveButton() {
        return skipMoveButton;
    }

    public AnchorPane getCellPain() {
        return cellPain;
    }

    public void switchPlayerMove() {
        String str;
        if (VirusApplication.getInstance().moveStatus == CellKind.crossMark) {
            str = "CrossMark moves";
        } else {
            str = "CircleMark moves";
        }
        playerMoveTextField.setText(str);
    }

    public void setClickCountTextField(String str) {
        clickCountTextField.setText("Click left: " + str);
    }

    public void init() {
        playerMoveTextField.setText("CrossMark moves");
        setClickCountTextField("3");
    }

    @FXML
    void initialize() {
        init();
    }

}
