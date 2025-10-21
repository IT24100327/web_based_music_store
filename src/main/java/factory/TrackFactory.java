package factory;

import model.Track;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackFactory {

    /**
     * Creates a Track instance from a ResultSet record.
     * This method assumes the ResultSet is the result of a query that joins the
     * tracks, users, and artist_details tables to get all necessary information.
     *
     * @param rs The ResultSet from which to create the Track object.
     * @return A populated Track object.
     * @throws SQLException if a database access error occurs.
     */
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

        // --- Read BLOB data ---
        track.setFullTrackData(rs.getBytes("full_track_data"));
        track.setCoverArtData(rs.getBytes("cover_art_data"));
        track.setCoverArtType(rs.getString("cover_art_type"));

        try {
            String artistName = rs.getString("stage_name");
            if (artistName == null || artistName.trim().isEmpty()) {
                artistName = rs.getString("firstName") + " " + rs.getString("lastName");
            }
            track.setArtistName(artistName.trim());
        } catch (SQLException e) {
            // This column might not exist in all queries, so we catch the error
            track.setArtistName("Unknown Artist");
        }

        return track;
    }
}