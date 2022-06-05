package com.company.client;

import com.company.client.Main.VirusApplication;
import com.company.share.CellKind;
import com.company.share.PackageObj;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends Thread {

    public Client(String _login, String _ip, int _port) throws IOException {
        instance = this;
        login = _login;
        ip = _ip;
        port = _port;
        timer = new Timer();
    }

    public void update() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                Platform.runLater(() -> {
                if (!sendFlag && !closeFlag && VirusApplication.getInstance().initFlag && VirusApplication.getInstance().moveStatus != VirusApplication.getInstance().clientKind) {
                    System.out.println("get move action");
                    try {
                        getMove();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                /*});*/
            }
        }, 0, 400);
    }

    public void run() {
        System.out.println("client thread " + getId());
        try {
            socket = new Socket(ip, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.reset();
            ois = new ObjectInputStream(socket.getInputStream());
            if (oos != null) {
                System.out.println("Client connected to socket. ");
                oos.writeUTF(login);
                oos.reset();
                readyFlag = true;
                update();
                while (!socket.isClosed() && !closeFlag) {
//                    listening();
                    Thread.sleep(1000); //todo timerTask
                }
                System.out.println("Client disconnected " + closeFlag);
                ois.close();
                timer.cancel();
            } else {
                closeFlag = true;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Connection refused");
        }
        closeFlag = true;
    }

//    public void listening() throws IOException, ClassNotFoundException {
//        if (listenFlag) {
//            listenFlag = false;
//            String entry = ois.readUTF();
//            switch (entry) {
//                case "send move" -> getMove();
//                case "partner ready" -> {
//                    getClientKind();
//                    partnerReadyFlag = true;
//                }
//            }
//            listenFlag = true;
//        }
//    }

    public boolean getClientKind() throws IOException, ClassNotFoundException {
        if (!closeFlag) {
            listenFlag = false;
            oos.writeUTF("get partner");
            oos.reset();
            VirusApplication.getInstance().clientKind = (CellKind) ois.readObject();
            if (VirusApplication.getInstance().clientKind != VirusApplication.getInstance().moveStatus)
                VirusApplication.getInstance().mainController.getSkipMoveButton().setDisable(true);
            System.out.print("You play as ");
            if (VirusApplication.getInstance().clientKind == CellKind.crossMark) System.out.println("crossMark");
            else if (VirusApplication.getInstance().clientKind == CellKind.zeroMark) System.out.println("zeroMark");
            else System.out.println("unknown");
            listenFlag = true;
            return true;
        }
        return false;
    }

    public void sendMove(PackageObj packageObj) throws IOException {
        if (!closeFlag) {
            System.out.println("send move");
            oos.writeUTF("send move");
            oos.reset();
            oos.writeObject(packageObj);
            oos.reset();
        }
    }

    public void sendSkipMove() throws IOException {
        oos.writeUTF("skipMove");
        oos.reset();
//        VirusApplication.getInstance().mainController.skipMoveButtonClick(new ActionEvent());
    }

    private void getMove() throws IOException, ClassNotFoundException {
        if (!closeFlag) {
//            Thread t = new Thread(() -> {

            System.out.println("thread " + getId());
            try {
                sendFlag = true;
//                    ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
//                    ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                oos.writeUTF("get move");
                oos.reset();
//                        listenFlag = false;
                PackageObj tmp = (PackageObj) ois.readObject();
//                        listenFlag = true;
                if (tmp.skipFlag) {
                    VirusApplication.getInstance().skipMove();
                } else {
                    VirusApplication.getInstance().getClick(tmp.x, tmp.y);
                    System.out.println("got move " + tmp.x + " " + tmp.y);
                }
                sendFlag = false;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
//            });
//            t.start();
        }
    }

    public void quit() throws IOException {
        if (!closeFlag) {
            oos.writeUTF("quit");
            oos.close();
            closeFlag = true;
            readyFlag = false;
            System.out.println("Closing connections & channels on clentSide - DONE.");
        }
    }

    public void chosePartner(String name) throws IOException {
        if (!closeFlag) {
            oos.writeUTF("chosePartner");
            oos.writeUTF(name);
            oos.reset();
        }
    }

    public void getClientList() throws IOException {
        if (!closeFlag) {
            System.out.println("getClientList");
            oos.writeUTF("getClientList");
            oos.reset();
            listenFlag = false;
            clientCount = Integer.parseInt(ois.readUTF());
            clientsList = new ArrayList<>();
            for (int i = 0; i < clientCount; i++) {
                System.out.println("Read " + clientCount);
                clientsList.add(ois.readUTF());
            }
            listenFlag = true;
            System.out.println("Read all");
        }
    }

    public static Client getInstance() {
        Client localInstance = instance;
        if (localInstance == null) {
            synchronized (Client.class) {
                localInstance = instance;
            }
        }
        return localInstance;
    }

    private boolean listenFlag;
    public boolean partnerReadyFlag;
    public boolean readyFlag = false;
    private final String login;
    private final String ip;
    private final int port;
    private static volatile Client instance;
    public boolean closeFlag = false;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    public ArrayList<String> clientsList;
    public int clientCount;
    Socket socket = null;
    public boolean sendFlag = false;
    private Timer timer = null;
}
// todo Хранить список свободных клиентов и клиентов в игре
// todo Для клиентов в игре сделать отдельный класс
// todo Распрелелять тип клетки при создании пары
// todo Определить кто есть кто(тип клетки)
// todo Передавать ход
// todo Принимать его и вызывать clickAction
// todo починить view