package model;

import model.enums.UserType;

import java.util.List;

public class Artist extends User {
    private List<String> specializedGenres;
    private String bio;

    public Artist(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    public Artist(int userId, String firstName, String lastName, String email, String password) {
        super(userId, firstName, lastName, email, password);
    }

    // Artist-specific getters and setters
    public List<String> getSpecializedGenres() {
        return specializedGenres;
    }

    public void setSpecializedGenres(List<String> specializedGenres) {
        this.specializedGenres = specializedGenres;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public UserType getUserType() {
        return UserType.ARTIST;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}