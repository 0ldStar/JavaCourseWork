package com.company.client.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.company.client.Client;
import com.company.client.Models.Cell;
import com.company.share.CellKind;
import com.company.share.PackageObj;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    private Button restartGameButton;
    @FXML
    private TextArea finishTextArea;

    @FXML
    private TextArea logTextArea;

    @FXML
    public void skipMoveButtonClick(ActionEvent event) throws IOException {
        VirusApplication.getInstance().skipMove();
        if (Client.getInstance() != null && !Client.getInstance().closeFlag)
            Client.getInstance().sendMove(new PackageObj(0, 0, true));
    }

    @FXML
    void restartGameButtonAction(ActionEvent event) {
        VirusApplication.getInstance().initGame();
    }

    public Button getSkipMoveButton() {
        return skipMoveButton;
    }

    public void addLogText(String str) {
        logTextArea.setText(logTextArea.getText() + str);
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

    public void finishGame() {
        skipMoveButton.setDisable(true);
        finishTextArea.setVisible(true);
        restartGameButton.setVisible(true);
        String wonCellStr = null;
        CellKind wonCell = VirusApplication.getInstance().wonCell;
        switch (wonCell) {
            case crossMark -> {
                wonCellStr = "CrossMark won!";
                finishTextArea.setStyle("-fx-text-fill: red ;");
            }
            case zeroMark -> {
                wonCellStr = "ZeroMark won!";
                finishTextArea.setStyle("-fx-text-fill: blue ;");
            }
            case cell -> wonCellStr = "Draw!";
        }
        finishTextArea.setText(wonCellStr + "\nTotal click: " + VirusApplication.getInstance().getClickCountStr());
    }

    public void init() {
        skipMoveButton.setDisable(false);
        finishTextArea.setVisible(false);
        restartGameButton.setVisible(false);
        playerMoveTextField.setText("CrossMark moves");
        logTextArea.setText("");
        setClickCountTextField("3");
    }

    @FXML
    void initialize() {
        init();
    }

}
