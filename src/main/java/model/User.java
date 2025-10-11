package model;

import java.util.ArrayList;
import java.util.List;

import model.enums.UserType;

public abstract class User {
    protected int userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected List<String> likedGenres = new ArrayList<>();

    public User() {

    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(int userId, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Common methods
    public void addLikedGenre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            likedGenres.add(genre.trim());
        }
    }

    public List<String> getLikedGenres() {
        return new ArrayList<>(likedGenres); // Return copy to prevent external modification
    }

    public void setLikedGenres(List<String> genres) {
        if (genres != null) {
            this.likedGenres = new ArrayList<>(genres);
        } else {
            this.likedGenres = new ArrayList<>();
        }
    }

    // Abstract methods - must be implemented by subclasses
    public abstract UserType getUserType();
    public abstract boolean isAdmin();

    // Common getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}