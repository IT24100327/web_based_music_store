package dao;

import model.Track;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.LinkedList;

public class TrackDAO {

    static {
        ensureTableExists();
    }

    public static void ensureTableExists() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {
            // Create tracks table
            stmt.executeUpdate(
                    "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'tracks') " +
                            "CREATE TABLE tracks (" +
                            "trackId INT IDENTITY(1,1) PRIMARY KEY, " +
                            "title VARCHAR(255), " +
                            "artist VARCHAR(255), " +
                            "price DECIMAL(10,2)" +
                            ")"
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tracks table", e);
        }
    }

    public static LinkedList<Track> getAllTracks() throws SQLException {
        LinkedList<Track> tracks = new LinkedList<>();
        String sql = "SELECT * FROM tracks";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Track track = new Track(
                        rs.getString("title"),
                        rs.getString("artist")
                );
                track.setTrackId(rs.getInt("trackId"));
                track.setPrice(rs.getDouble("price"));
                tracks.add(track);
            }
        }
        return tracks;
    }

    public static void addTrack(Track track) throws SQLException {
        if (track == null) return;

        String sql = "INSERT INTO tracks (title, artist, price) VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, track.getTitle());
            pstmt.setString(2, track.getArtist());
            pstmt.setDouble(3, track.getPrice());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    track.setTrackId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public static void removeTrack(int trackId) throws SQLException {
        String sql = "DELETE FROM tracks WHERE trackId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, trackId);
            pstmt.executeUpdate();
        }
    }

    public static Track findTrackById(int trackId) throws SQLException {
        String sql = "SELECT * FROM tracks WHERE trackId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, trackId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Track track = new Track(
                            rs.getString("title"),
                            rs.getString("artist")
                    );
                    track.setTrackId(rs.getInt("trackId"));
                    track.setPrice(rs.getDouble("price"));
                    return track;
                }
            }
        }
        return null;
    }

    public static void updateTrack(Track track) throws SQLException {
        if (track == null) return;

        String sql = "UPDATE tracks SET title = ?, artist = ?, price = ? WHERE trackId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, track.getTitle());
            pstmt.setString(2, track.getArtist());
            pstmt.setDouble(3, track.getPrice());
            pstmt.setInt(4, track.getTrackId());
            pstmt.executeUpdate();
        }
    }
}