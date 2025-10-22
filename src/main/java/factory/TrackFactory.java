// In main/java/factory/TrackFactory.java
package factory;

import model.Track;
import model.enums.TrackStatus; // Import the enum

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackFactory {

    public static Track createTrackFromResultSet(ResultSet rs) throws SQLException {
        Track track = new Track();
        track.setTrackId(rs.getInt("trackId")); 
        track.setTitle(rs.getString("title")); 
        track.setPrice(rs.getDouble("price")); 
        track.setGenre(rs.getString("genre")); 
        track.setRating(rs.getDouble("rating")); 
        track.setArtistId(rs.getInt("artist_id")); 
        track.setDuration(rs.getInt("duration")); 
        track.setReleaseDate(rs.getDate("release_date") != null ? rs.getDate("release_date").toLocalDate() : null); 
        track.setFullTrackData(rs.getBytes("full_track_data")); 
        track.setCoverArtData(rs.getBytes("cover_art_data")); 
        track.setCoverArtType(rs.getString("cover_art_type")); 

        // NEW: Read and set the status
        try {
            String statusStr = rs.getString("status");
            if (statusStr != null) {
                track.setStatus(TrackStatus.valueOf(statusStr.toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            track.setStatus(TrackStatus.PENDING); // Default if value is invalid
        }

        try {
            String artistName = rs.getString("stage_name"); 
            if (artistName == null || artistName.trim().isEmpty()) {
                artistName = rs.getString("firstName") + " " + rs.getString("lastName");
            }
            track.setArtistName(artistName.trim());
        } catch (SQLException e) {
            track.setArtistName("Unknown Artist");
        }

        return track;
    }
}