// DatabaseInitializer.java
package controller.listeners;

import utils.DatabaseConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            createAllTables();
        } catch (Exception e) {
            sce.getServletContext().log("Error during database initialization: " + e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private void createAllTables() throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {

            // Create tracks table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS tracks (" +
                            "trackId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "title VARCHAR(50), " +
                            "artist VARCHAR(50), " +
                            "genre VARCHAR(20), " +
                            "rating FLOAT, " +
                            "price DECIMAL(10,2)" +
                            ")"
            );

            // Create users table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "userId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "role VARCHAR(20), " +
                            "firstName VARCHAR(50), " +
                            "lastName VARCHAR(50), " +
                            "email VARCHAR(100) UNIQUE, " +
                            "password VARCHAR(255)" +
                            ")"
            );

            // Create UserGenres table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS UserGenres (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "userId INT, " +
                            "genre VARCHAR(50), " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );

            // Create AdminRoles table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS AdminRoles (" +
                            "userId INT PRIMARY KEY, " +
                            "role VARCHAR(50), " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );

            // Create ArtistDetails table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS ArtistDetails (" +
                            "userId INT PRIMARY KEY, " +
                            "bio LONGTEXT, " +
                            "specializedGenres LONGTEXT, " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );

            // Create advertisements table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS advertisements (" +
                            "adId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "title VARCHAR(100), " +
                            "content VARCHAR(255), " +
                            "imageData LONGBLOB, " +
                            "imageUrl VARCHAR(255), " +
                            "startDate DATE, " +
                            "endDate DATE, " +
                            "active TINYINT(1)" +
                            ")"
            );

            // Create promotions table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS promotions (" +
                            "promotionId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "code VARCHAR(50) UNIQUE, " +
                            "discount DECIMAL(10,2), " +
                            "startDate DATE, " +
                            "endDate DATE, " +
                            "usageCount INT, " +
                            "description VARCHAR(255)" +
                            ")"
            );

            // Create carts table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS carts (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "track_id INT NOT NULL, " +
                            "quantity INT NOT NULL DEFAULT 1, " +
                            "added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE, " +
                            "FOREIGN KEY (track_id) REFERENCES tracks(trackId) ON DELETE CASCADE, " +
                            "UNIQUE KEY unique_user_track (user_id, track_id)" +
                            ")"
            );

            // Create orders table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS orders (" +
                            "order_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "total_amount DECIMAL(10,2) NOT NULL, " +
                            "status VARCHAR(20) DEFAULT 'PENDING', " +
                            "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "payment_method VARCHAR(50), " +
                            "transaction_id VARCHAR(100), " +
                            "FOREIGN KEY (user_id) REFERENCES users(userId)" +
                            ")"
            );
        }
    }
}