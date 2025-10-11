package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String DB_URL = "jdbc:mysql://localhost:3306/music_db?useSSL=false&serverTimezone=UTC";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

}
