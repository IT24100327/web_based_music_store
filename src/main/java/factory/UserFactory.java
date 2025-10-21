package factory;

import dao.UserDAO;
import model.Admin;
import model.Artist;
import model.StandardUser;
import model.User;
import model.enums.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFactory {

    public static User createUser(String role, String firstName, String lastName, String email, String password) {
        UserType userType;
        try {
            userType = UserType.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role + ". Must be 'USER', 'ADMIN', or 'ARTIST'.");
        }

        return switch (userType) {
            case ADMIN -> new Admin(firstName, lastName, email, password);
            case ARTIST -> new Artist(firstName, lastName, email, password);
            default -> new StandardUser(firstName, lastName, email, password);
        };
    }

    public static User createUserFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("userId");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String roleStr = rs.getString("role");

        UserType userType = UserType.valueOf(roleStr);

        User user = switch (userType) {
            case ADMIN -> {
                Admin admin = new Admin(userId, firstName, lastName, email, password);
                admin.setRole(UserDAO.getAdminRole(userId));
                yield admin;
            }
            // Update the createUserFromResultSet method for ARTIST case
            case ARTIST -> {
                Artist artist = new Artist(userId, firstName, lastName, email, password);
                // Load artist details
                Artist artistDetails = UserDAO.getArtistDetails(userId);
                if (artistDetails != null) {
                    artist.setStageName(artistDetails.getStageName());
                    artist.setBio(artistDetails.getBio());
                    artist.setTrackCount(artistDetails.getTrackCount());
                }
                yield artist;
            }

            default -> new StandardUser(userId, firstName, lastName, email, password);
        };

        user.setLikedGenres(UserDAO.getUserLikedGenres(userId));
        return user;
    }
}