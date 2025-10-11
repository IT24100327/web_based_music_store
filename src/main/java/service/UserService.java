package service;

import dao.UserDAO;
import factory.UserFactory;
import model.User;
import model.enums.AdminRole;
import model.enums.UserType;
import service.validators.UserValidation.AdminUserValidator;
import service.validators.UserValidation.ArtistUserValidator;
import service.validators.UserValidation.UserValidator;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserService {

    public LinkedList<User> getAllUsers() throws SQLException {
        return UserDAO.getUsers();
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
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + user.getUserId());
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

    public void updateUser(int userId, String firstName, String lastName, String email, String roleStr) throws IOException, SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        User currentUser = findUserById(userId);
        if (currentUser == null) {
            throw new SQLException("User not found with ID: " + userId);
        }
        UserType newType = UserType.valueOf(roleStr.toUpperCase());

        User tempUser = UserFactory.createUser(roleStr, firstName, lastName, email, currentUser.getPassword());
        tempUser.setUserId(userId);
        UserValidator validator = UserValidator.forUser(tempUser);
        validator.validate(tempUser);
        if (!currentUser.getEmail().equalsIgnoreCase(email)) {
            if (findUserByEmail(email) != null) {
                throw new IllegalArgumentException("Email already exists: " + email);
            }
        }
        UserDAO.updateUser(userId, firstName.trim(), lastName.trim(), email.trim(), roleStr);
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
        // Create temp user of the same type for password validation only (dummy fields)
        String roleStr = user.getUserType().name().toLowerCase();
        User tempUser = UserFactory.createUser(roleStr, "dummy", "dummy", "dummy@email.com", newPassword);
        tempUser.setUserId(userId);
        // Set liked genres to empty to avoid validation issues
        tempUser.setLikedGenres(new ArrayList<>());
        UserValidator validator = UserValidator.forUser(tempUser);
        validator.validate(tempUser);
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

    // Update artist-specific fields
    public void updateArtistDetails(int userId, String bio, List<String> specializedGenres) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        User user = findUserById(userId);
        if (user == null) {
            throw new SQLException("User not found with ID: " + userId);
        }
        if (user.getUserType() != UserType.ARTIST) {
            throw new IllegalArgumentException("User is not an Artist");
        }
        // Validation via ArtistUserValidator
        UserValidator validator = UserValidator.forUser(user);
        if (validator.strategy instanceof ArtistUserValidator) {
            ((ArtistUserValidator) validator.strategy).validateDetails(bio, specializedGenres);
        }
        // Delegate to DAO for persistence
        UserDAO.updateArtistDetails(userId, bio, specializedGenres);
    }
}