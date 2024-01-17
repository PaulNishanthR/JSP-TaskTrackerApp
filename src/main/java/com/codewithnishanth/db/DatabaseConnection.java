package com.codewithnishanth.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/task_db";
    private static final String username = "root";
    private static final String password = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionUrl, username, password);
            System.out.println("Connection " + !connection.isClosed());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("connection get successfully");

        return connection;
    }
}
