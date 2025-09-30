package model;

import java.time.LocalDate;

public class Advertisement {
    private int adId;
    private String title;
    private String content;
    private byte[] imageData;
    private String imageUrl; // Optional, for external URLs
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public Advertisement(String title, String content, byte[] imageData, String imageUrl, LocalDate startDate, LocalDate endDate, boolean active) {
        this.title = title;
        this.content = content;
        this.imageData = imageData;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public Advertisement(int adId, String title, String content, byte[] imageData, String imageUrl, LocalDate startDate, LocalDate endDate, boolean active) {
        this.adId = adId;
        this.title = title;
        this.content = content;
        this.imageData = imageData;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    // Getters and Setters
    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}