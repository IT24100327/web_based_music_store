package dao;

import model.SupportTicket;
import model.SupportResponse;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupportTicketDAO {

    /**
     * Creates a new support ticket in the database.
     * @param ticket The SupportTicket object to save.
     * @return true if creation is successful, false otherwise.
     * @throws SQLException if a database error occurs.
     */
    public static boolean createTicket(SupportTicket ticket) throws SQLException {
        String sql = "INSERT INTO supportTickets (userId, orderId, trackId, ticketType, subject, description, status, priority, createdDate, lastUpdated) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, ticket.getUserId());

            if (ticket.getOrderId() == 0) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, ticket.getOrderId());
            }

            if (ticket.getTrackId() == 0) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, ticket.getTrackId());
            }

            pstmt.setString(4, ticket.getTicketType());
            pstmt.setString(5, ticket.getSubject());
            pstmt.setString(6, ticket.getDescription());
            pstmt.setString(7, ticket.getStatus());
            pstmt.setString(8, ticket.getPriority());
            pstmt.setTimestamp(9, Timestamp.valueOf(ticket.getCreatedDate()));
            pstmt.setTimestamp(10, Timestamp.valueOf(ticket.getLastUpdated()));

            int result = pstmt.executeUpdate();
            if (result > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticket.setTicketId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Retrieves all support tickets for a specific user.
     * @param userId The ID of the user.
     * @return A list of the user's support tickets.
     * @throws SQLException if a database error occurs.
     */
    public static List<SupportTicket> getTicketsByUserId(int userId) throws SQLException {
        List<SupportTicket> tickets = new ArrayList<>();
        String sql = "SELECT st.*, u.firstName, u.lastName, u.email, " +
                "a.firstName as adminFirstName, a.lastName as adminLastName, t.title as trackTitle " +
                "FROM supportTickets st " +
                "JOIN users u ON st.userId = u.userId " +
                "LEFT JOIN users a ON st.assignedToAdmin = a.userId " +
                "LEFT JOIN tracks t ON st.trackId = t.trackId " +
                "WHERE st.userId = ? " +
                "ORDER BY st.lastUpdated DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(createTicketFromResultSet(rs));
                }
            }
        }
        return tickets;
    }

    /**
     * Retrieves all support tickets for the admin view.
     * @return A list of all support tickets.
     * @throws SQLException if a database error occurs.
     */
    public static List<SupportTicket> getAllTickets() throws SQLException {
        List<SupportTicket> tickets = new ArrayList<>();
        String sql = "SELECT st.*, u.firstName, u.lastName, u.email, " +
                "a.firstName as adminFirstName, a.lastName as adminLastName, t.title as trackTitle " +
                "FROM supportTickets st " +
                "JOIN users u ON st.userId = u.userId " +
                "LEFT JOIN users a ON st.assignedToAdmin = a.userId " +
                "LEFT JOIN tracks t ON st.trackId = t.trackId " +
                "ORDER BY " +
                "CASE st.priority WHEN 'urgent' THEN 1 WHEN 'high' THEN 2 WHEN 'medium' THEN 3 ELSE 4 END, st.lastUpdated DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                tickets.add(createTicketFromResultSet(rs));
            }
        }
        return tickets;
    }

    /**
     * Retrieves all support tickets with a specific status.
     * @param status The status to filter by.
     * @return A list of matching support tickets.
     * @throws SQLException if a database error occurs.
     */
    public static List<SupportTicket> getTicketsByStatus(String status) throws SQLException {
        List<SupportTicket> tickets = new ArrayList<>();
        String sql = "SELECT st.*, u.firstName, u.lastName, u.email, " +
                "a.firstName as adminFirstName, a.lastName as adminLastName, t.title as trackTitle " +
                "FROM supportTickets st " +
                "JOIN users u ON st.userId = u.userId " +
                "LEFT JOIN users a ON st.assignedToAdmin = a.userId " +
                "LEFT JOIN tracks t ON st.trackId = t.trackId " +
                "WHERE st.status = ? " +
                "ORDER BY st.lastUpdated DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(createTicketFromResultSet(rs));
                }
            }
        }
        return tickets;
    }

    /**
     * Finds a single support ticket by its ID, including related user and track info.
     * @param ticketId The ID of the ticket to find.
     * @return A SupportTicket object if found, otherwise null.
     * @throws SQLException if a database error occurs.
     */
    public static SupportTicket findTicketById(int ticketId) throws SQLException {
        String sql = "SELECT st.*, u.firstName, u.lastName, u.email, " +
                "a.firstName as adminFirstName, a.lastName as adminLastName, t.title as trackTitle " +
                "FROM supportTickets st " +
                "JOIN users u ON st.userId = u.userId " +
                "LEFT JOIN users a ON st.assignedToAdmin = a.userId " +
                "LEFT JOIN tracks t ON st.trackId = t.trackId " +
                "WHERE st.ticketId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SupportTicket ticket = createTicketFromResultSet(rs);
                    ticket.setResponses(getResponsesByTicketId(ticketId));
                    return ticket;
                }
            }
        }
        return null;
    }

    public static boolean updateTicketStatus(int ticketId, String status) throws SQLException {
        String sql = "UPDATE supportTickets SET status = ?, lastUpdated = CURRENT_TIMESTAMP WHERE ticketId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, ticketId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean assignTicketToAdmin(int ticketId, int adminId) throws SQLException {
        String sql = "UPDATE supportTickets SET assignedToAdmin = ?, status = 'in_progress', lastUpdated = CURRENT_TIMESTAMP WHERE ticketId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            pstmt.setInt(2, ticketId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean updateTicketPriority(int ticketId, String priority) throws SQLException {
        String sql = "UPDATE supportTickets SET priority = ?, lastUpdated = CURRENT_TIMESTAMP WHERE ticketId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, priority);
            pstmt.setInt(2, ticketId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static List<SupportResponse> getResponsesByTicketId(int ticketId) throws SQLException {
        List<SupportResponse> responses = new ArrayList<>();
        String sql = "SELECT sr.*, u.firstName, u.lastName " +
                "FROM supportResponses sr " +
                "JOIN users u ON sr.responderId = u.userId " +
                "WHERE sr.ticketId = ? " +
                "ORDER BY sr.responseDate ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SupportResponse response = new SupportResponse();
                    response.setResponseId(rs.getInt("responseId"));
                    response.setTicketId(rs.getInt("ticketId"));
                    response.setResponderId(rs.getInt("responderId"));
                    response.setResponseText(rs.getString("responseText"));
                    Timestamp timestamp = rs.getTimestamp("responseDate");
                    if (timestamp != null) {
                        response.setResponseDate(timestamp.toLocalDateTime());
                    }
                    response.setAdminResponse(rs.getBoolean("isAdminResponse"));
                    response.setResponderName(rs.getString("firstName") + " " + rs.getString("lastName"));
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    public static boolean addResponseToTicket(SupportResponse response) throws SQLException {
        String sql = "INSERT INTO supportResponses (ticketId, responderId, responseText, responseDate, isAdminResponse) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, response.getTicketId());
            pstmt.setInt(2, response.getResponderId());
            pstmt.setString(3, response.getResponseText());
            pstmt.setTimestamp(4, Timestamp.valueOf(response.getResponseDate()));
            pstmt.setBoolean(5, response.isAdminResponse());
            int result = pstmt.executeUpdate();
            if (result > 0) {
                updateTicketLastUpdated(response.getTicketId());
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        response.setResponseId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    private static void updateTicketLastUpdated(int ticketId) throws SQLException {
        String sql = "UPDATE supportTickets SET lastUpdated = CURRENT_TIMESTAMP WHERE ticketId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            pstmt.executeUpdate();
        }
    }

    public static int getTicketCountByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM supportTickets WHERE status = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Helper method to construct a SupportTicket object from a ResultSet.
     * @param rs The ResultSet containing the ticket data.
     * @return A populated SupportTicket object.
     * @throws SQLException if a database error occurs.
     */
    private static SupportTicket createTicketFromResultSet(ResultSet rs) throws SQLException {
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketId(rs.getInt("ticketId"));
        ticket.setUserId(rs.getInt("userId"));

        int orderId = rs.getInt("orderId");
        if (!rs.wasNull()) ticket.setOrderId(orderId);

        int trackId = rs.getInt("trackId");
        if (!rs.wasNull()) ticket.setTrackId(trackId);

        ticket.setTicketType(rs.getString("ticketType"));
        ticket.setSubject(rs.getString("subject"));
        ticket.setDescription(rs.getString("description"));
        ticket.setStatus(rs.getString("status"));
        ticket.setPriority(rs.getString("priority"));

        Timestamp createdTimestamp = rs.getTimestamp("createdDate");
        if (createdTimestamp != null) ticket.setCreatedDate(createdTimestamp.toLocalDateTime());

        Timestamp updatedTimestamp = rs.getTimestamp("lastUpdated");
        if (updatedTimestamp != null) ticket.setLastUpdated(updatedTimestamp.toLocalDateTime());

        int assignedAdminId = rs.getInt("assignedToAdmin");
        if (!rs.wasNull()) ticket.setAssignedToAdmin(assignedAdminId);

        // Set display names from JOINed tables
        ticket.setUserName(rs.getString("firstName") + " " + rs.getString("lastName"));
        ticket.setUserEmail(rs.getString("email"));

        String adminFirstName = rs.getString("adminFirstName");
        if (adminFirstName != null) {
            ticket.setAssignedAdminName(adminFirstName + " " + rs.getString("adminLastName"));
        }

        try {
            ticket.setTrackTitle(rs.getString("trackTitle"));
        } catch (SQLException e) {
            // Safely ignore if the column isn't in this specific query's result set
        }

        return ticket;
    }

    public static boolean deleteTicket(int ticketId) throws SQLException {
        String sql = "DELETE FROM supportTickets WHERE ticketId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            return pstmt.executeUpdate() > 0;
        }
    }
}