package com.company.server;

import com.company.share.CellKind;
import com.company.share.PackageObj;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonoThreadClientHandler extends Thread {

    private final Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        clientDialog = client;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(clientDialog.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(clientDialog.getOutputStream());
            oos.reset();
            String clientName = ois.readUTF();
            synchronized (MultiThreadServer.clientVector) {
                MultiThreadServer.clientVector.add(new ClientObj(clientName));
                MultiThreadServer.clientVector.forEach(client -> {
                    if (Objects.equals(client.name, clientName)) {
                        clientObj = client;
                    }
                });
            }

            System.out.println("Connected " + clientName);
            label:
            while (!clientDialog.isClosed() && !clientDialog.isOutputShutdown() && !clientDialog.isOutputShutdown()) {
                System.out.println("Server reading from channel " + getId());
                String entry = ois.readUTF();
                System.out.println("READ from clientDialog message - " + entry);

                switch (entry) {
                    case "chosePartner":
                        AtomicBoolean flag = new AtomicBoolean(false);
                        String partner = ois.readUTF();
                        MultiThreadServer.clientVector.forEach(client -> {
                            if (Objects.equals(client.name, partner)) {
                                partnerObj = client;
                                if (partnerObj.cellKind == CellKind.cell) {
                                    System.out.println("Set " + partnerObj.name + " crossMark");
                                    partnerObj.cellKind = CellKind.crossMark;
                                }
                                partnerObj.partnerReadyFlag = true;
                                flag.set(true);
                                client.clientStatus = ClientStatus.completeConnect;
                            }
                        });
                        if (partnerObj.cellKind == CellKind.crossMark) {
                            if (flag.get()) {
                                clientObj.clientStatus = ClientStatus.completeConnect;
                                clientObj.cellKind = CellKind.zeroMark;
                                clientObj.partnerReadyFlag = true;
                                System.out.println("Set " + clientObj.name + " zeroMark");
                            } else {
                                clientObj.clientStatus = ClientStatus.waitPartner;
                            }
                        } else {
                            clientObj.partnerReadyFlag = true;
                        }
                        break;
                    case "send move":
                        partnerObj.packageObjLinkedList.add((PackageObj) ois.readObject());
                        System.out.println("get move " + partnerObj.packageObjLinkedList.size());
                        break;
                    case "get partner":
                        if (clientObj.partnerReadyFlag) {
                            System.out.println("set partner " + clientObj.name + " ->" + partnerObj.name);
                            if (clientObj.cellKind == CellKind.crossMark) System.out.println("crossMark");
                            if (clientObj.cellKind == CellKind.zeroMark) System.out.println("zeroMark");
                            oos.writeObject(clientObj.cellKind);
                            oos.reset();
                            clientObj.partnerReadyFlag = false;
                        }
                        break;
                    case "get move":
                        System.out.println("want to get move " + clientObj.packageObjLinkedList.size());
                        while (clientObj.packageObjLinkedList.size() == 0) {
                            Thread.sleep(100);
                        }
                        System.out.println("send move " + clientObj.packageObjLinkedList.size());
                        oos.writeObject(clientObj.packageObjLinkedList.getFirst());
                        clientObj.packageObjLinkedList.remove(clientObj.packageObjLinkedList.getFirst());
                        oos.reset();
                        break;
                    case "getClientCount":
                        oos.writeUTF(Integer.toString(MultiThreadServer.clientCount));
                        oos.reset();
                        break;
                    case "getClientList":
                        oos.writeUTF(Integer.toString(MultiThreadServer.clientCount - 1));
                        oos.reset();
                        System.out.println("Send client list " + (MultiThreadServer.clientCount - 1));
                        synchronized (MultiThreadServer.clientVector) {
                            MultiThreadServer.clientVector.forEach(client -> {
                                if (!Objects.equals(client.name, clientName) && client.clientStatus == ClientStatus.finding) {
                                    try {
                                        System.out.println("Send");
                                        oos.writeUTF(client.name);
                                        oos.reset();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }
                        oos.reset();
                        break;
                    case "quit":
                        break label;
                }
                Thread.sleep(100);
            }
            MultiThreadServer.clientCount--;
            System.out.println("Client disconnected " + clientDialog.getInetAddress() + " " + getId());

            ois.close();
            oos.close();

            clientDialog.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private

    ClientObj clientObj;
    ClientObj partnerObj;
}
