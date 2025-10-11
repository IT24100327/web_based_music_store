package dao;

import model.Track;
import utils.DatabaseConnection;
import dao.constants.TrackSQLConstants;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrackDAO {

    public static LinkedList<Track> getAllTracks() throws SQLException {
        LinkedList<Track> tracks = new LinkedList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(TrackSQLConstants.SELECT_ALL_TRACKS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Track track = new Track(
                        rs.getString("title"),
                        rs.getString("artist")
                );
                track.setTrackId(rs.getInt("trackId"));
                track.setPrice(rs.getDouble("price"));
                track.setGenre(rs.getString("genre"));
                track.setRating(rs.getDouble("rating"));
                tracks.add(track);
            }
        }
        return tracks;
    }

    public static List<Track> getAllTracksPaginated(int page, int pageSize) throws SQLException {
        List<Track> tracks = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(TrackSQLConstants.SELECT_TRACKS_PAGINATED)) {

            pstmt.setInt(1, pageSize);  // LIMIT
            pstmt.setInt(2, (page - 1) * pageSize);  // OFFSET

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Track track = new Track(
                            rs.getString("title"),
                            rs.getString("artist")
                    );
                    track.setTrackId(rs.getInt("trackId"));
                    track.setPrice(rs.getDouble("price"));
                    track.setGenre(rs.getString("genre"));
                    track.setRating(rs.getDouble("rating"));
                    tracks.add(track);
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

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     TrackSQLConstants.INSERT_TRACK, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, track.getTitle());
            pstmt.setString(2, track.getArtist());
            pstmt.setDouble(3, track.getPrice());
            pstmt.setString(4, track.getGenre());
            pstmt.setDouble(5, track.getRating());
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
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(TrackSQLConstants.SELECT_TRACK_BY_ID)) {

            pstmt.setInt(1, trackId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Track track = new Track(
                            rs.getString("title"),
                            rs.getString("artist")
                    );
                    track.setTrackId(rs.getInt("trackId"));
                    track.setPrice(rs.getDouble("price"));
                    track.setGenre(rs.getString("genre"));
                    track.setRating(rs.getDouble("rating"));
                    return track;
                }
            }
        }
        return null;
    }

    public static void updateTrack(Track track) throws SQLException {
        if (track == null) return;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(TrackSQLConstants.UPDATE_TRACK)) {

            pstmt.setString(1, track.getTitle());
            pstmt.setString(2, track.getArtist());
            pstmt.setDouble(3, track.getPrice());
            pstmt.setString(4, track.getGenre());
            pstmt.setDouble(5, track.getRating());
            pstmt.setInt(6, track.getTrackId());
            pstmt.executeUpdate();
        }
    }

    public static List<Track> searchProducts(String title, String genre, Double minPrice,
                                             Double maxPrice, Double rating, int page, int pageSize) {
        List<Track> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(TrackSQLConstants.SEARCH_TRACKS_BASE);
        List<Object> params = new ArrayList<>();

        if (title != null && !title.trim().isEmpty()) {
            sql.append(TrackSQLConstants.TITLE_LIKE_CONDITION);
            params.add("%" + title.trim() + "%");
        }

        if (genre != null && !genre.isEmpty()) {
            sql.append(TrackSQLConstants.GENRE_EQUAL_CONDITION);
            params.add(genre);
        }

        if (minPrice != null) {
            sql.append(TrackSQLConstants.MIN_PRICE_CONDITION);
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(TrackSQLConstants.MAX_PRICE_CONDITION);
            params.add(maxPrice);
        }

        if (rating != null) {
            sql.append(TrackSQLConstants.RATING_CONDITION);
            params.add(rating);
        }

        sql.append(TrackSQLConstants.PAGINATION);

        params.add(pageSize);  // LIMIT
        params.add((page - 1) * pageSize);  // OFFSET

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Track(
                        rs.getInt("trackId"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getDouble("price"),
                        rs.getString("genre"),
                        rs.getDouble("rating")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int countProducts(String title, String genre, Double minPrice, Double maxPrice, Double minRating) {
        StringBuilder sql = new StringBuilder(TrackSQLConstants.COUNT_SEARCH_TRACKS_BASE);
        List<Object> params = new ArrayList<>();

        if (title != null && !title.trim().isEmpty()) {
            sql.append(TrackSQLConstants.TITLE_LIKE_CONDITION);
            params.add("%" + title.trim() + "%");
        }
        if (genre != null && !genre.isEmpty()) {
            sql.append(TrackSQLConstants.GENRE_EQUAL_CONDITION);
            params.add(genre);
        }

        if (minPrice != null) {
            sql.append(TrackSQLConstants.MIN_PRICE_CONDITION);
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(TrackSQLConstants.MAX_PRICE_CONDITION);
            params.add(maxPrice);
        }

        if (minRating != null) {  // Added rating condition
            sql.append(TrackSQLConstants.RATING_CONDITION);
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