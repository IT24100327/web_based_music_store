package dao;

import dao.constants.TrackSQLConstants;
import factory.TrackFactory;
import model.Track;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrackDAO {

    private static final String SELECT_BASE = "SELECT t.*, u.firstName, u.lastName, ad.stage_name FROM tracks t JOIN users u ON t.artist_id = u.userId LEFT JOIN artist_details ad ON t.artist_id = ad.user_id";

    public static LinkedList<Track> getAllTracks() throws SQLException {
        LinkedList<Track> tracks = new LinkedList<>();
        String sql = SELECT_BASE; // Use the base query

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                tracks.add(TrackFactory.createTrackFromResultSet(rs));
            }
        }
        return tracks;
    }

    public static List<Track> getAllTracksPaginated(int page, int pageSize) throws SQLException {
        List<Track> tracks = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY t.trackId LIMIT ? OFFSET ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, pageSize);  // LIMIT
            pstmt.setInt(2, (page - 1) * pageSize);  // OFFSET

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tracks.add(TrackFactory.createTrackFromResultSet(rs));
                }
            }
        }
        return tracks;
    }

    public static int countAllTracks() throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(TrackSQLConstants.COUNT_ALL_TRACKS);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static void addTrack(Track track) throws SQLException {
        if (track == null) return;
        String sql = "INSERT INTO tracks (title, price, genre, rating, artist_id, full_track_data, cover_art_data, cover_art_type, duration, release_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, track.getTitle());
            pstmt.setDouble(2, track.getPrice());
            pstmt.setString(3, track.getGenre());
            pstmt.setDouble(4, track.getRating());
            pstmt.setInt(5, track.getArtistId());
            pstmt.setBytes(6, track.getFullTrackData());
            pstmt.setBytes(7, track.getCoverArtData());
            pstmt.setString(8, track.getCoverArtType());
            pstmt.setInt(9, track.getDuration());
            pstmt.setDate(10, track.getReleaseDate() != null ? Date.valueOf(track.getReleaseDate()) : null);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    track.setTrackId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public static void removeTrack(int trackId) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(TrackSQLConstants.DELETE_TRACK)) {
            pstmt.setInt(1, trackId);
            pstmt.executeUpdate();
        }
    }

    public static Track findTrackById(int trackId) throws SQLException {
        String sql = SELECT_BASE + " WHERE t.trackId = ?"; // Use base query and add condition
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, trackId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return TrackFactory.createTrackFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static void updateTrack(Track track) throws SQLException {
        if (track == null) return;
        // Build SQL dynamically based on whether new file data is present
        String sql = "UPDATE tracks SET title = ?, price = ?, genre = ?, rating = ?, artist_id = ?, duration = ?, release_date = ? " +
                (track.getFullTrackData() != null ? ", full_track_data = ? " : "") +
                (track.getCoverArtData() != null ? ", cover_art_data = ?, cover_art_type = ? " : "") +
                "WHERE trackId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            int paramIndex = 1;
            pstmt.setString(paramIndex++, track.getTitle());
            pstmt.setDouble(paramIndex++, track.getPrice());
            pstmt.setString(paramIndex++, track.getGenre());
            pstmt.setDouble(paramIndex++, track.getRating());
            pstmt.setInt(paramIndex++, track.getArtistId());
            pstmt.setInt(paramIndex++, track.getDuration());
            pstmt.setDate(paramIndex++, track.getReleaseDate() != null ? Date.valueOf(track.getReleaseDate()) : null);

            if (track.getFullTrackData() != null) {
                pstmt.setBytes(paramIndex++, track.getFullTrackData());
            }
            if (track.getCoverArtData() != null) {
                pstmt.setBytes(paramIndex++, track.getCoverArtData());
                pstmt.setString(paramIndex++, track.getCoverArtType());
            }

            pstmt.setInt(paramIndex, track.getTrackId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Searches for tracks based on various filters with pagination.
     */
    public static List<Track> searchProducts(String title, String genre, Double minPrice, Double maxPrice, Double rating, int page, int pageSize) {
        List<Track> list = new ArrayList<>();
        // Start with the base query that already includes joins
        StringBuilder sql = new StringBuilder(SELECT_BASE + " WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (title != null && !title.trim().isEmpty()) {
            sql.append(" AND t.title LIKE ?");
            params.add("%" + title.trim() + "%");
        }
        if (genre != null && !genre.isEmpty()) {
            sql.append(" AND t.genre = ?");
            params.add(genre);
        }
        if (minPrice != null) {
            sql.append(" AND t.price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND t.price <= ?");
            params.add(maxPrice);
        }
        if (rating != null) {
            sql.append(" AND t.rating >= ?");
            params.add(rating);
        }

        sql.append(" ORDER BY t.trackId LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(TrackFactory.createTrackFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Track> getTracksByArtistId(int artistId) throws SQLException {
        List<Track> tracks = new LinkedList<>();
        String sql = SELECT_BASE + " WHERE t.artist_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tracks.add(TrackFactory.createTrackFromResultSet(rs));
                }
            }
        }
        return tracks;
    }

    public static List<Track> getPurchasedTracksByUserId(int userId) throws SQLException {
        List<Track> purchasedTracks = new ArrayList<>();

        String sql = "SELECT t.*, u.firstName, u.lastName, ad.stage_name " +
                "FROM purchased_tracks pt " +
                "JOIN tracks t ON pt.track_id = t.trackId " +
                "JOIN users u ON t.artist_id = u.userId " +
                "LEFT JOIN artist_details ad ON u.userId = ad.user_id " +
                "WHERE pt.user_id = ? " +
                "ORDER BY pt.purchase_date DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    purchasedTracks.add(TrackFactory.createTrackFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return purchasedTracks;
    }

    public static int countProducts(String title, String genre, Double minPrice, Double maxPrice, Double minRating) {
        // This query does not need joins, which is more efficient for just counting.
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM tracks WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (title != null && !title.trim().isEmpty()) {
            sql.append(" AND title LIKE ?");
            params.add("%" + title.trim() + "%");
        }
        if (genre != null && !genre.isEmpty()) {
            sql.append(" AND genre = ?");
            params.add(genre);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }
        if (minRating != null) {
            sql.append(" AND rating >= ?");
            params.add(minRating);
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}