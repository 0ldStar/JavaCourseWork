package com.company.coursework.StartWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StartMenuController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button exitMenuButton;
    @FXML
    private DialogPane startMenu;
    @FXML
    private Button getPropertiesButton;
    @FXML
    private TextField IPTextField;
    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private TextField loginTextField;

    @FXML
    void exitMenuButtonClick(ActionEvent event) throws IOException {
        startMenu.setVisible(false);
        startMenu.setDisable(true);
        StartMenu.getInstance().getStage().close();
    }

    void sendProperties() throws IOException {


    }

    @FXML
    void getPropertiesButtonAction(ActionEvent event) throws IOException, ClassNotFoundException, InterruptedException {

    }

    @FXML
    void connectButtonClick(ActionEvent event) throws IOException {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wrong input!\nThink about it ☺", ButtonType.YES);
//        if (Client.getInstance() == null || !Client.getInstance().readyFlag) {
//            String host;
//            int port;
//            if (IPTextField.getText().equals("localhost")) {
//                host = IPTextField.getText();
//                port = 3345;
//            } else {
//                Pattern p = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");
//                Matcher m = p.matcher(IPTextField.getText());
//                if (m.matches() && !loginTextField.getText().isEmpty()) {
//                    host = m.group(1);
//                    port = Integer.parseInt(m.group(2));
//                } else {
//                    IPTextField.setText("localhost");
//                    loginTextField.setText("");
//                    alert.showAndWait();
//                    return;
//                }
//            }
//            try {
//                Client client = new Client(loginTextField.getText(), host, port);
//                client.start();
//                Thread.sleep(100);
//                if (Client.getInstance().closeFlag) {
//                    IPTextField.setText("localhost");
//                    loginTextField.setText("");
//                    alert.showAndWait();
//                    return;
//                }
//                getPropertiesButton.setDisable(false);
//                clientComboBox.setDisable(false);
//                sendProperties();
//                System.out.println("Update clientList");
//                updateClientComboBox();
//                connectionFlag = true;
//            } catch (IOException e) {
//                IPTextField.setText("localhost");
//                loginTextField.setText("");
//                alert.showAndWait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        } else if (Client.getInstance().readyFlag) {
//            System.out.println("Update clientList");
//            updateClientComboBox();
//            sendProperties();
//        }


    }

    public void updateClientComboBox() throws IOException {
//        Client.getInstance().getClientList();
//        clientComboBox.getItems().clear();
//        for (int i = 0; i < Client.getInstance().clientCount; i++) {
//            clientComboBox.getItems().add(Client.getInstance().clientsList.get(i));
//        }
//        if (clientComboBox.getItems() != null) clientComboBox.getSelectionModel().select(0);
    }

    @FXML
    void initialize() {
//        propertiesPackage = new PropertiesPackage();
        ArrayList<String> comboBoxTexts = new ArrayList<>();
        for (int i = 10; i <= 100; i += 10)
            comboBoxTexts.add(Integer.toString(i));
        startMenu.setContent(null);
        startMenu.setGraphic(null);
        IPTextField.setText("localhost");
        getPropertiesButton.setDisable(true);
        clientComboBox.setDisable(true);
    }

    public static boolean connectionFlag = false;
//    private PropertiesPackage propertiesPackage;
}
