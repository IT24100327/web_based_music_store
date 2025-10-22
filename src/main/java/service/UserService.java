package service;

import dao.UserDAO;
import factory.UserFactory;
import model.Admin;
import model.Artist;
import model.User;
import model.enums.AdminRole;
import model.enums.UserType;
import service.validators.UserValidation.AdminUserValidator;
import service.validators.UserValidation.UserValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserService {

    /**
     * Updates an artist's complete profile. This is the single entry point for this operation.
     */
    public void updateArtist(Artist artist, String newPassword) throws SQLException, IOException {
        // 1. Fetch existing user data to ensure it exists and is an artist.
        User existingUser = UserDAO.findUserById(artist.getUserId());
        if (existingUser == null || existingUser.getUserType() != UserType.ARTIST) {
            throw new IllegalArgumentException("Artist not found with ID: " + artist.getUserId());
        }

        // 2. Validate the complete, updated Artist object.
        UserValidator validator = UserValidator.forUser(artist);
        validator.validate(artist);

        // 3. Check for business rule violations (uniqueness constraints).
        if (!existingUser.getEmail().equalsIgnoreCase(artist.getEmail()) && UserDAO.findUserByEmail(artist.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + artist.getEmail());
        }
        if (UserDAO.isStageNameTaken(artist.getStageName(), artist.getUserId())) {
            throw new IllegalArgumentException("Stage name is already taken: " + artist.getStageName());
        }

        // 4. Persist the updated user and artist details in a single transaction.
        UserDAO.updateUser(artist);

        // 5. Update password only if a new one was provided.
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            this.updateUserPassword(artist.getUserId(), newPassword);
        }
    }

    /**
     * Updates a standard user or an admin's profile.
     */
    public void updateUser(User user, String newPassword) throws SQLException, IOException {
        // 1. Fetch existing user data to ensure it exists.
        User existingUser = UserDAO.findUserById(user.getUserId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with ID: " + user.getUserId());
        }
        // This method should not be used to update artists.
        if (user.getUserType() == UserType.ARTIST || existingUser.getUserType() == UserType.ARTIST) {
            throw new IllegalArgumentException("Use the updateArtist() method to modify artist profiles.");
        }

        // 2. Validate the incoming object.
        UserValidator validator = UserValidator.forUser(user);
        validator.validate(user);

        // 3. Check for email uniqueness.
        if (!existingUser.getEmail().equalsIgnoreCase(user.getEmail()) && UserDAO.findUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // 4. Persist the changes.
        UserDAO.updateUser(user);

        // 5. Handle Admin role update.
        if (user instanceof Admin admin) {
            UserDAO.updateAdminRole(admin.getUserId(), admin.getRole());
        } else if (existingUser.getUserType() == UserType.ADMIN && user.getUserType() != UserType.ADMIN) {
            // If user's role was changed FROM Admin, remove their entry.
            UserDAO.updateAdminRole(user.getUserId(), null);
        }

        // 6. Update password if provided.
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            this.updateUserPassword(user.getUserId(), newPassword);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        return UserDAO.getAllUsers();
    }

    public List<Artist> getArtists() throws SQLException {
        return UserDAO.getArtists();
    }

    public List<User> getUsers() throws SQLException {
        return UserDAO.getUsers();
    }

    public List<Admin> getAdmins() throws SQLException {
        return UserDAO.getAdmins();
    }

    public void addUser(User user) throws IOException, SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        UserValidator validator = UserValidator.forUser(user);
        validator.validate(user);
        if (findUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        UserDAO.addUser(user);
    }

    public void removeUser(User user) throws IOException, SQLException {
        if (user == null || user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user for removal");
        }
        UserDAO.removeUser(user);
    }

    public User findUserById(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        return UserDAO.findUserById(userId);
    }

    public User findUserByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return UserDAO.findUserByEmail(email.trim());
    }

    public void updateUserPassword(int userId, String newPassword) throws IOException, SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        User user = findUserById(userId);
        if (user == null) {
            throw new SQLException("User not found with ID: " + userId);
        }
        // Create a temporary user of the same type just for password validation
        User tempUser = UserFactory.createUser(user.getUserType().name().toLowerCase(), "dummy", "dummy", "dummy@email.com", newPassword);
        UserValidator validator = UserValidator.forUser(tempUser);
        validator.validate(tempUser); // This will check password length rules for the specific user type

        UserDAO.updateUserPassword(userId, newPassword.trim());
    }

    public void updateAdminRole(int userId, AdminRole newRole) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        User user = findUserById(userId);
        if (user == null) {
            throw new SQLException("User not found with ID: " + userId);
        }
        if (newRole != null && user.getUserType() == UserType.ADMIN) {
            UserValidator validator = UserValidator.forUser(user);
            if (validator.strategy instanceof AdminUserValidator) {
                ((AdminUserValidator) validator.strategy).validateRole(newRole);
            }
        }
        UserDAO.updateAdminRole(userId, newRole);
    }
}