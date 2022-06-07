package com.company.server;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {

    static ExecutorService executeIt = Executors.newFixedThreadPool(2);


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            ex.printStackTrace();
        }
        clientVector = new Vector<>();
        clientCount = 0;
        try {
            System.out.println(getWonCount("1"));
            System.out.println(getTotalCount("1"));
            System.out.println(getWonCount("3"));
            System.out.println(getTotalCount("3"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (ServerSocket server = new ServerSocket(3345)) {
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

    public static void addDatabaseInfo(String name, boolean isWon) {
        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            boolean rows = checkExist(name);
            System.out.println(rows);
            if (rows) {
                String updFieldStr = "SELECT wonGame FROM score WHERE login = '%s'".formatted(name);

                resultSet = statement.executeQuery(updFieldStr);
                resultSet.next();
                int wonScore = resultSet.getInt("wonGame");
                updFieldStr = "SELECT totalGame FROM score WHERE login = '%s'".formatted(name);
                resultSet = statement.executeQuery(updFieldStr);
                resultSet.next();
                int totalScore = resultSet.getInt("totalGame");
                totalScore++;
                if (isWon) {
                    wonScore++;
                }
                updFieldStr = "UPDATE score SET wonGame = '%d' WHERE login = '%s'".formatted(wonScore, name);
                statement.executeUpdate(updFieldStr);
                updFieldStr = "UPDATE score SET totalGame = '%d' WHERE login = '%s'".formatted(totalScore, name);
                statement.executeUpdate(updFieldStr);

            } else {
                int wonGame;
                if (isWon) wonGame = 1;
                else wonGame = 0;
                int totalGame = 1;
                String addFieldStd = "INSERT score(login, wonGame, totalGame) VALUES ('%s', %d, %d)".formatted(name, wonGame, totalGame);
                statement.executeUpdate(addFieldStd);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException, IOException {

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    private static boolean checkExist(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String findFieldStr = "SELECT login FROM score WHERE login = '%s'".formatted(name);
        ResultSet resultSet = statement.executeQuery(findFieldStr);
        return resultSet.next();
    }

    public static int getWonCount(String name) throws SQLException {
        if (checkExist(name)) {
            String updFieldStr = "SELECT wonGame FROM score WHERE login = '%s'".formatted(name);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(updFieldStr);
            resultSet.next();
            return resultSet.getInt("wonGame");
        } else return 0;
    }

    public static int getTotalCount(String name) throws SQLException {
        if (checkExist(name)) {
            String updFieldStr = "SELECT totalGame FROM score WHERE login = '%s'".formatted(name);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(updFieldStr);
            resultSet.next();
            return resultSet.getInt("totalGame");
        } else return 0;
    }

    public static Connection connection;
    public static Integer clientCount;
    public static Vector<ClientObj> clientVector;
}
