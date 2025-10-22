package model;

import java.sql.Timestamp;

public class Post {
    private int postId;
    private int userId;
    private String authorName;
    private String title;
    private String description;
    private byte[] image1Data;
    private String image1Type;
    private byte[] image2Data;
    private String image2Type;
    private byte[] image3Data;
    private String image3Type;
    private String status; // pending, approved, rejected
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor for creating new post
    public Post(int userId, String authorName, String title, String description) {
        this.userId = userId;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.status = "pending";
    }

    // Constructor for retrieving from database
    public Post(int postId, int userId, String authorName, String title, String description,
                byte[] image1Data, String image1Type, byte[] image2Data, String image2Type, byte[] image3Data, String image3Type,
                String status, Timestamp createdAt, Timestamp updatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.image1Data = image1Data;
        this.image1Type = image1Type;
        this.image2Data = image2Data;
        this.image2Type = image2Type;
        this.image3Data = image3Data;
        this.image3Type = image3Type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // Getters and Setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage1Data() {
        return image1Data;
    }

    public void setImage1Data(byte[] image1Data) {
        this.image1Data = image1Data;
    }

    public String getImage1Type() {
        return image1Type;
    }

    public void setImage1Type(String image1Type) {
        this.image1Type = image1Type;
    }

    public byte[] getImage2Data() {
        return image2Data;
    }

    public void setImage2Data(byte[] image2Data) {
        this.image2Data = image2Data;
    }

    public String getImage2Type() {
        return image2Type;
    }

    public void setImage2Type(String image2Type) {
        this.image2Type = image2Type;
    }

    public byte[] getImage3Data() {
        return image3Data;
    }

    public void setImage3Data(byte[] image3Data) {
        this.image3Data = image3Data;
    }

    public String getImage3Type() {
        return image3Type;
    }

    public void setImage3Type(String image3Type) {
        this.image3Type = image3Type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}