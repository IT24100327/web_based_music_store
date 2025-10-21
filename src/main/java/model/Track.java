package model;

import java.time.LocalDate;

public class Track {
    private int trackId;
    private String title;
    private double price;
    private String genre;
    private double rating;
    private int artistId;
    private String artistName;
    private byte[] snippetData;      // Replaces snippetPath
    private byte[] fullTrackData;    // Replaces fullTrackPath
    private byte[] coverArtData;     // Replaces coverArtUrl
    private String coverArtType;     // New field for MIME type
    private int duration;
    private LocalDate releaseDate;

    public Track() {
    }

    public Track(int trackId, String title, double price, String genre, double rating, int artistId, String artistName, int duration, LocalDate releaseDate, byte[] coverArtData, byte[] snippetData, byte[] fullTrackData) {
        this.trackId = trackId;
        this.title = title;
        this.price = price;
        this.genre = genre;
        this.rating = rating;
        this.artistId = artistId;
        this.artistName = artistName;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.coverArtData = coverArtData;
        this.snippetData = snippetData;
        this.fullTrackData = fullTrackData;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public byte[] getSnippetData() {
        return snippetData;
    }

    public void setSnippetData(byte[] snippetData) {
        this.snippetData = snippetData;
    }

    public byte[] getFullTrackData() {
        return fullTrackData;
    }

    public void setFullTrackData(byte[] fullTrackData) {
        this.fullTrackData = fullTrackData;
    }

    public byte[] getCoverArtData() {
        return coverArtData;
    }

    public void setCoverArtData(byte[] coverArtData) {
        this.coverArtData = coverArtData;
    }

    public String getCoverArtType() {
        return coverArtType;
    }

    public void setCoverArtType(String coverArtType) {
        this.coverArtType = coverArtType;
    }
}