package model;

import model.enums.UserType;

import java.util.List;

public class Artist extends User {
    private String stageName;
    private String bio;
    private List<String> specializedGenres;
    private List<Track> tracks;
    private int trackCount;

    public Artist() {
    }

    public Artist(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    public Artist(int userId, String firstName, String lastName, String email, String password) {
        super(userId, firstName, lastName, email, password);
    }

    // Artist-specific getters and setters
    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getSpecializedGenres() {
        return specializedGenres;
    }

    public void setSpecializedGenres(List<String> specializedGenres) {
        this.specializedGenres = specializedGenres;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
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