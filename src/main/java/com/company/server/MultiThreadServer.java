package com.company.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.company.share.PackageObj;


public class MultiThreadServer {

    static ExecutorService executeIt = Executors.newFixedThreadPool(2);


    public static void main(String[] args) {
        clientVector = new Vector<>();
        clientCount = 0;
        try (ServerSocket server = new ServerSocket(3345);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket created, command console reader for listen to server commands");
            while (!server.isClosed()) {
                Socket client = server.accept();
                MonoThreadClientHandler clientThread = new MonoThreadClientHandler(client);
                clientCount++;
                clientThread.start();
                System.out.println("Connection accepted." + client.getInetAddress());
            }
            executeIt.shutdown();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static Integer clientCount;
    public static Vector<ClientObj> clientVector;
}
