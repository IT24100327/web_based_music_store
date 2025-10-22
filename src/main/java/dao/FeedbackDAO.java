package dao;

import model.Feedback;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    /**
     * Submits new feedback to the database, including optional track and order IDs.
     * @param feedback The Feedback object to be saved.
     * @return true if the submission was successful, false otherwise.
     * @throws SQLException if a database error occurs.
     */
    public static boolean submitFeedback(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (userId, trackId, orderId, email, feedbackType, subject, message, rating, submittedDate, isRead) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Handle nullable userId for anonymous feedback
            if (feedback.getUserId() == 0) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, feedback.getUserId());
            }

            // Handle nullable trackId
            if (feedback.getTrackId() == 0) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, feedback.getTrackId());
            }

            // Handle nullable orderId
            if (feedback.getOrderId() == 0) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, feedback.getOrderId());
            }

            pstmt.setString(4, feedback.getEmail());
            pstmt.setString(5, feedback.getFeedbackType());
            pstmt.setString(6, feedback.getSubject());
            pstmt.setString(7, feedback.getMessage());
            pstmt.setInt(8, feedback.getRating());
            pstmt.setTimestamp(9, Timestamp.valueOf(feedback.getSubmittedDate()));
            pstmt.setBoolean(10, feedback.isRead());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        feedback.setFeedbackId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Retrieves all feedback entries, joining with user and track tables for context.
     * @return A list of all Feedback objects.
     * @throws SQLException if a database error occurs.
     */
    public static List<Feedback> getAllFeedback() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.*, u.firstName, u.lastName, t.title as trackTitle " +
                "FROM feedback f " +
                "LEFT JOIN users u ON f.userId = u.userId " +
                "LEFT JOIN tracks t ON f.trackId = t.trackId " +
                "ORDER BY f.submittedDate DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                feedbackList.add(createFeedbackFromResultSet(rs));
            }
        }
        return feedbackList;
    }

    /**
     * Retrieves all feedback entries of a specific type.
     * @param feedbackType The type of feedback to filter by (e.g., 'bug_report').
     * @return A list of matching Feedback objects.
     * @throws SQLException if a database error occurs.
     */
    public static List<Feedback> getFeedbackByType(String feedbackType) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.*, u.firstName, u.lastName, t.title as trackTitle " +
                "FROM feedback f " +
                "LEFT JOIN users u ON f.userId = u.userId " +
                "LEFT JOIN tracks t ON f.trackId = t.trackId " +
                "WHERE f.feedbackType = ? " +
                "ORDER BY f.submittedDate DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, feedbackType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(createFeedbackFromResultSet(rs));
                }
            }
        }
        return feedbackList;
    }

    /**
     * Retrieves all unread feedback entries.
     * @return A list of unread Feedback objects.
     * @throws SQLException if a database error occurs.
     */
    public static List<Feedback> getUnreadFeedback() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.*, u.firstName, u.lastName, t.title as trackTitle " +
                "FROM feedback f " +
                "LEFT JOIN users u ON f.userId = u.userId " +
                "LEFT JOIN tracks t ON f.trackId = t.trackId " +
                "WHERE f.isRead = 0 " +
                "ORDER BY f.submittedDate ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                feedbackList.add(createFeedbackFromResultSet(rs));
            }
        }
        return feedbackList;
    }

    /**
     * Retrieves all feedback entries submitted by a specific user.
     * @param userId The ID of the user.
     * @return A list of the user's Feedback objects.
     * @throws SQLException if a database error occurs.
     */
    public static List<Feedback> getFeedbackByUserId(int userId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.*, u.firstName, u.lastName, t.title as trackTitle " +
                "FROM feedback f " +
                "LEFT JOIN users u ON f.userId = u.userId " +
                "LEFT JOIN tracks t ON f.trackId = t.trackId " +
                "WHERE f.userId = ? " +
                "ORDER BY f.submittedDate DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(createFeedbackFromResultSet(rs));
                }
            }
        }
        return feedbackList;
    }

    /**
     * Finds a specific feedback entry by its ID.
     * @param feedbackId The ID of the feedback to find.
     * @return A Feedback object if found, otherwise null.
     * @throws SQLException if a database error occurs.
     */
    public static Feedback findFeedbackById(int feedbackId) throws SQLException {
        String sql = "SELECT f.*, u.firstName, u.lastName, t.title as trackTitle " +
                "FROM feedback f " +
                "LEFT JOIN users u ON f.userId = u.userId " +
                "LEFT JOIN tracks t ON f.trackId = t.trackId " +
                "WHERE f.feedbackId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, feedbackId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createFeedbackFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static boolean markFeedbackAsRead(int feedbackId) throws SQLException {
        String sql = "UPDATE feedback SET isRead = 1 WHERE feedbackId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, feedbackId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean addAdminNotes(int feedbackId, String adminNotes) throws SQLException {
        String sql = "UPDATE feedback SET adminNotes = ?, isRead = 1 WHERE feedbackId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, adminNotes);
            pstmt.setInt(2, feedbackId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean updateUserFeedback(int feedbackId, int userId, String feedbackType, String subject, String message, int rating) throws SQLException {
        String sql = "UPDATE feedback SET feedbackType = ?, subject = ?, message = ?, rating = ? WHERE feedbackId = ? AND userId = ? AND isRead = FALSE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, feedbackType);
            pstmt.setString(2, subject);
            pstmt.setString(3, message);
            pstmt.setInt(4, rating);
            pstmt.setInt(5, feedbackId);
            pstmt.setInt(6, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean deleteUserFeedback(int feedbackId, int userId) throws SQLException {
        String sql = "DELETE FROM feedback WHERE feedbackId = ? AND userId = ? AND isRead = FALSE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, feedbackId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean deleteFeedback(int feedbackId) throws SQLException {
        String sql = "DELETE FROM feedback WHERE feedbackId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, feedbackId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static int getFeedbackCountByType(String feedbackType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM feedback WHERE feedbackType = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, feedbackType);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public static int getUnreadFeedbackCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM feedback WHERE isRead = 0";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static double getAveragePlatformRating() throws SQLException {
        String sql = "SELECT AVG(CAST(rating AS FLOAT)) as avgRating FROM feedback WHERE rating IS NOT NULL AND rating > 0";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("avgRating");
            }
        }
        return 0.0;
    }

    /**
     * Helper method to construct a Feedback object from a ResultSet.
     * @param rs The ResultSet containing feedback data.
     * @return A populated Feedback object.
     * @throws SQLException if a database access error occurs.
     */
    private static Feedback createFeedbackFromResultSet(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getInt("feedbackId"));

        int userId = rs.getInt("userId");
        if (!rs.wasNull()) {
            feedback.setUserId(userId);
            feedback.setUserName(rs.getString("firstName") + " " + rs.getString("lastName"));
        }

        int trackId = rs.getInt("trackId");
        if (!rs.wasNull()) feedback.setTrackId(trackId);

        int orderId = rs.getInt("orderId");
        if (!rs.wasNull()) feedback.setOrderId(orderId);

        feedback.setEmail(rs.getString("email"));
        feedback.setFeedbackType(rs.getString("feedbackType"));
        feedback.setSubject(rs.getString("subject"));
        feedback.setMessage(rs.getString("message"));
        feedback.setRating(rs.getInt("rating"));

        Timestamp timestamp = rs.getTimestamp("submittedDate");
        if (timestamp != null) {
            feedback.setSubmittedDate(timestamp.toLocalDateTime());
        }

        feedback.setRead(rs.getBoolean("isRead"));
        feedback.setAdminNotes(rs.getString("adminNotes"));

        try {
            feedback.setTrackTitle(rs.getString("trackTitle"));
        } catch (SQLException e) {
            // Column might not be present in all queries, safely ignore.
        }

        return feedback;
    }
}