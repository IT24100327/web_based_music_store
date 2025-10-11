package factory;

import model.Admin;
import model.Artist;
import model.StandardUser;
import model.User;
import model.enums.UserType;
import dao.UserDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        UserType userType = UserType.fromDbValue(roleStr);

        User user = switch (userType) {
            case ADMIN -> {
                Admin admin = new Admin(userId, firstName, lastName, email, password);
                admin.setRole(UserDAO.getAdminRole(userId));
                yield admin;
            }
            case ARTIST -> {
                Artist artist = new Artist(userId, firstName, lastName, email, password);
                artist.setSpecializedGenres(UserDAO.getArtistSpecializedGenres(userId));
                artist.setBio(UserDAO.getArtistBio(userId));
                yield artist;
            }
            default -> new StandardUser(userId, firstName, lastName, email, password);
        };

        user.setLikedGenres(UserDAO.getUserLikedGenres(userId));
        return user;
    }
}