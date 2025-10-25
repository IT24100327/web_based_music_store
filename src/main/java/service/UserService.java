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


    public void updateArtist(Artist artist, String newPassword) throws SQLException, IOException {
        User existingUser = UserDAO.findUserById(artist.getUserId());
        if (existingUser == null || existingUser.getUserType() != UserType.ARTIST) {
            throw new IllegalArgumentException("Artist not found with ID: " + artist.getUserId());
        }

        UserValidator validator = UserValidator.forUser(artist);
        validator.validate(artist);

        if (!existingUser.getEmail().equalsIgnoreCase(artist.getEmail()) && UserDAO.findUserByEmail(artist.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + artist.getEmail());
        }
        if (UserDAO.isStageNameTaken(artist.getStageName(), artist.getUserId())) {
            throw new IllegalArgumentException("Stage name is already taken: " + artist.getStageName());
        }

        UserDAO.updateUser(artist);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            this.updateUserPassword(artist.getUserId(), newPassword);
        }
    }

    public void updateUser(User user, String newPassword) throws SQLException, IOException {

        User existingUser = UserDAO.findUserById(user.getUserId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with ID: " + user.getUserId());
        }

        UserValidator validator = UserValidator.forUser(user);
        validator.validate(user);


        if (!existingUser.getEmail().equalsIgnoreCase(user.getEmail()) && UserDAO.findUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        if (user instanceof Artist artist) {
            if (UserDAO.isStageNameTaken(artist.getStageName(), artist.getUserId())) {
                throw new IllegalArgumentException("Stage name is already taken: " + artist.getStageName());
            }
        }

        UserDAO.updateUser(user);

        if (user instanceof Admin admin) {
            UserDAO.updateAdminRole(admin.getUserId(), admin.getRole());
        } else if (existingUser.getUserType() == UserType.ADMIN && user.getUserType() != UserType.ADMIN) {
            UserDAO.updateAdminRole(user.getUserId(), null);
        }

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

        User tempUser = UserFactory.createUser(user.getUserType().name().toLowerCase(), "dummy", "dummy", "dummy@email.com", newPassword);
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
}