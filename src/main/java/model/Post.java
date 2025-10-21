package model;

import java.sql.Timestamp;

public class Post {
    private int postId;
    private int userId;
    private String authorName;
    private String title;
    private String description;
    private String image1Path;
    private String image2Path;
    private String image3Path;
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
                String image1Path, String image2Path, String image3Path,
                String status, Timestamp createdAt, Timestamp updatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.image1Path = image1Path;
        this.image2Path = image2Path;
        this.image3Path = image3Path;
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

    public String getImage1Path() {
        return image1Path;
    }

    public void setImage1Path(String image1Path) {
        this.image1Path = image1Path;
    }

    public String getImage2Path() {
        return image2Path;
    }

    public void setImage2Path(String image2Path) {
        this.image2Path = image2Path;
    }

    public String getImage3Path() {
        return image3Path;
    }

    public void setImage3Path(String image3Path) {
        this.image3Path = image3Path;
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
