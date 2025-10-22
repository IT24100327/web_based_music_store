// DatabaseInitializer.java
package controller.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import utils.DatabaseConnection;

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

            // Create users table (Must be created before tracks and other dependent tables)
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
            // Create tracks table (Updated with BLOB fields)
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS tracks (" +
                            "trackId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "title VARCHAR(100), " +
                            "genre VARCHAR(50), " +
                            "rating FLOAT, " +
                            "price DECIMAL(10,2), " +
                            "artist_id INT NOT NULL, " +
                            "full_track_data LONGBLOB, " +
                            "snippet_data LONGBLOB, " +
                            "cover_art_data LONGBLOB, " +
                            "cover_art_type VARCHAR(50), " +
                            "duration INT, " +
                            "release_date DATE, " +
                            "status VARCHAR(20) NOT NULL DEFAULT 'PENDING', " +
                            "FOREIGN KEY (artist_id) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );
            // Create orders table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS orders (" +
                            "order_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "total_amount DECIMAL(10,2) NOT NULL, " +
                            "discount_amount DECIMAL(10,2) DEFAULT 0.00, " + // New
                            "final_amount DECIMAL(10,2) NOT NULL, " +        // New
                            "promotion_code VARCHAR(50), " +                 // New
                            "status VARCHAR(20) DEFAULT 'PENDING', " +
                            "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "payment_method VARCHAR(50), " +
                            "transaction_id VARCHAR(100), " +
                            "FOREIGN KEY (user_id) REFERENCES users(userId)" +
                            ")"
            );
            // Create purchased_tracks table (New table for tracking downloads)
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS purchased_tracks (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "order_id INT NOT NULL, " + // <-- ADDED COLUMN
                            "track_id INT NOT NULL, " +
                            "purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE, " +
                            "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, " + // <-- ADDED FOREIGN KEY
                            "FOREIGN KEY (track_id) REFERENCES tracks(trackId) ON DELETE CASCADE " +
                            ")"
            );
            // Create UserGenres table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS user_genres (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "userId INT, " +
                            "genre VARCHAR(50), " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );
            // Create AdminRoles table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS admin_roles (" +
                            "userId INT PRIMARY KEY, " +
                            "role VARCHAR(50), " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );
            // Create ArtistDetails table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS artist_details (" +
                            "user_id INT PRIMARY KEY, " +
                            "stage_name VARCHAR(100) NOT NULL, " +
                            "bio TEXT, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS artist_genres (" +
                    "user_id INT NOT NULL, " +
                    "genre VARCHAR(50) NOT NULL, " +
                    "PRIMARY KEY (user_id, genre), " +
                    "FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE" +
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

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS payments (" +
                            "payment_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "order_id INT NOT NULL, " +
                            "amount DECIMAL(10,2) NOT NULL, " +
                            "payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "payment_method VARCHAR(50) NOT NULL, " +
                            "transaction_id VARCHAR(100) UNIQUE, " +
                            "status VARCHAR(20) NOT NULL, " + // e.g., COMPLETED, FAILED, REFUNDED
                            "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE" +
                            ")"
            );
            // Create posts table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS posts (" +
                            "postId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "userId INT NOT NULL, " +
                            "authorName VARCHAR(100) NOT NULL, " +
                            "title VARCHAR(200) NOT NULL, " +
                            "description TEXT NOT NULL, " +
                            "image1Data LONGBLOB, " +
                            "image1Type VARCHAR(50), " +
                            "image2Data LONGBLOB, " +
                            "image2Type VARCHAR(50), " +
                            "image3Data LONGBLOB, " +
                            "image3Type VARCHAR(50), " +
                            "status VARCHAR(20) DEFAULT 'pending', " +
                            "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            "updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE" +
                            ")"
            );
            // Create feedback table - UPDATED
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS feedback (" +
                            "feedbackId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "userId INT NULL, " +
                            "trackId INT NULL, " +
                            "orderId INT NULL, " +
                            "email VARCHAR(255) NULL, " +
                            "feedbackType VARCHAR(50) NOT NULL, " +
                            "subject VARCHAR(200) NOT NULL, " +
                            "message TEXT NOT NULL, " +
                            "rating INT CHECK (rating >= 1 AND rating <= 5), " +
                            "submittedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "isRead BOOLEAN DEFAULT FALSE, " +
                            "adminNotes TEXT NULL, " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE SET NULL, " +
                            "FOREIGN KEY (trackId) REFERENCES tracks(trackId) ON DELETE SET NULL, " +
                            "FOREIGN KEY (orderId) REFERENCES orders(order_id) ON DELETE SET NULL" +
                            ")"
            );
            // Create reviews table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS reviews (" +
                            "reviewId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "userId INT NOT NULL, " +
                            "trackId INT NOT NULL, " +
                            "rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5), " +
                            "reviewText TEXT, " +
                            "reviewDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "isApproved BOOLEAN DEFAULT FALSE, " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE, " +
                            "FOREIGN KEY (trackId) REFERENCES tracks(trackId) ON DELETE CASCADE, " +
                            "UNIQUE(userId, trackId)" +
                            ")"
            );
            // Create supportTickets table - UPDATED
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS supportTickets (" +
                            "ticketId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "userId INT NOT NULL, " +
                            "orderId INT NULL, " +
                            "trackId INT NULL, " +
                            "ticketType VARCHAR(50) NOT NULL, " +
                            "subject VARCHAR(200) NOT NULL, " +
                            "description VARCHAR(2000) NOT NULL, " +
                            "status VARCHAR(20) DEFAULT 'open', " +
                            "priority VARCHAR(10) DEFAULT 'medium', " +
                            "createdDate DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            "lastUpdated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "assignedToAdmin INT NULL, " +
                            "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE, " +
                            "FOREIGN KEY (orderId) REFERENCES orders(order_id) ON DELETE SET NULL, " +
                            "FOREIGN KEY (trackId) REFERENCES tracks(trackId) ON DELETE SET NULL" +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS supportResponses (" +
                            "responseId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "ticketId INT NOT NULL, " +
                            "responderId INT NOT NULL, " +
                            "responseText VARCHAR(2000) NOT NULL, " +
                            "responseDate DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            "isAdminResponse TINYINT(1) DEFAULT 1, " +
                            "FOREIGN KEY (ticketId) REFERENCES supportTickets(ticketId) ON DELETE CASCADE, " +
                            "FOREIGN KEY (responderId) REFERENCES users(userId)" +
                            ")"
            );
        }
    }
}