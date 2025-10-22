package dao;

import model.Post;
import utils.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    // Create a new post
    public static void createPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (userId, authorName, title, description, image1Data, image1Type, image2Data, image2Type, image3Data, image3Type, status, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getAuthorName());
            pstmt.setString(3, post.getTitle());
            pstmt.setString(4, post.getDescription());
            pstmt.setBytes(5, post.getImage1Data());
            pstmt.setString(6, post.getImage1Type());
            pstmt.setBytes(7, post.getImage2Data());
            pstmt.setString(8, post.getImage2Type());
            pstmt.setBytes(9, post.getImage3Data());
            pstmt.setString(10, post.getImage3Type());
            pstmt.setString(11, post.getStatus());

            int result = pstmt.executeUpdate();
            System.out.println("Post created. Rows affected: " + result);

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setPostId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Get all approved posts for public view
    public static List<Post> getAllApprovedPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE status = 'approved' ORDER BY createdAt DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                posts.add(createPostFromResultSet(rs));
            }
        }

        return posts;
    }

    // Get all posts for admin (all statuses)
    public static List<Post> getAllPostsForAdmin() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY createdAt DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                posts.add(createPostFromResultSet(rs));
            }
        }

        return posts;
    }

    // Get pending posts for admin approval
    public static List<Post> getPendingPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE status = 'pending' ORDER BY createdAt DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                posts.add(createPostFromResultSet(rs));
            }
        }

        return posts;
    }

    // Get posts by user ID
    public static List<Post> getPostsByUserId(int userId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE userId = ? ORDER BY createdAt DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(createPostFromResultSet(rs));
                }
            }
        }

        return posts;
    }

    // Get post by ID
    public static Post getPostById(int postId) throws SQLException {
        String sql = "SELECT * FROM posts WHERE postId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPostFromResultSet(rs);
                }
            }
        }

        return null;
    }

    // Update post
    public static void updatePost(Post post) throws SQLException {
        String sql = "UPDATE posts SET title = ?, description = ?, image1Data = ?, image1Type = ?, image2Data = ?, image2Type = ?, image3Data = ?, image3Type = ?, status = ?, updatedAt = CURRENT_TIMESTAMP WHERE postId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getDescription());
            pstmt.setBytes(3, post.getImage1Data());
            pstmt.setString(4, post.getImage1Type());
            pstmt.setBytes(5, post.getImage2Data());
            pstmt.setString(6, post.getImage2Type());
            pstmt.setBytes(7, post.getImage3Data());
            pstmt.setString(8, post.getImage3Type());
            pstmt.setString(9, post.getStatus());
            pstmt.setInt(10, post.getPostId());

            int result = pstmt.executeUpdate();
            System.out.println("Post updated. Rows affected: " + result);
        }
    }

    // Update post status (for admin approval/rejection)
    public static void updatePostStatus(int postId, String status) throws SQLException {
        String sql = "UPDATE posts SET status = ?, updatedAt = CURRENT_TIMESTAMP WHERE postId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, postId);

            int result = pstmt.executeUpdate();
            System.out.println("Post status updated to " + status + ". Rows affected: " + result);
        }
    }

    // Delete post (DB only - kept for backward compatibility)
    public static void deletePost(int postId) throws SQLException {
        String sql = "DELETE FROM posts WHERE postId = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            int result = pstmt.executeUpdate();
            System.out.println("Post deleted. Rows affected: " + result);
        }
    }

    public static List<Post> getRecentApprovedPosts(int limit) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE status = 'approved' ORDER BY createdAt DESC LIMIT ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(createPostFromResultSet(rs));
                }
            }
        }
        return posts;
    }

    /**
     * New: Delete a post and attempt to remove its image files from the webapp uploads directory.
     * @param postId the post to delete
     * @param webRootAbsolutePath absolute path to the deployed webapp root (e.g., getServletContext().getRealPath(""))
     */
    public static void deletePostAndFiles(int postId, String webRootAbsolutePath) throws SQLException {
        String selectSql = "SELECT image1Path, image2Path, image3Path FROM posts WHERE postId = ?";
        String deleteSql = "DELETE FROM posts WHERE postId = ?";

        String img1 = null, img2 = null, img3 = null;
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            // 1) Read image paths
            try (PreparedStatement psSel = con.prepareStatement(selectSql)) {
                psSel.setInt(1, postId);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        img1 = rs.getString("image1Path");
                        img2 = rs.getString("image2Path");
                        img3 = rs.getString("image3Path");
                    } else {
                        // Nothing to delete
                        con.rollback();
                        return;
                    }
                }
            }

            // 2) Delete DB row
            try (PreparedStatement psDel = con.prepareStatement(deleteSql)) {
                psDel.setInt(1, postId);
                psDel.executeUpdate();
            }

            con.commit();
            // 3) Try deleting files on disk (best-effort, outside DB transaction)
            safeDelete(webRootAbsolutePath, img1);
            safeDelete(webRootAbsolutePath, img2);
            safeDelete(webRootAbsolutePath, img3);
        } catch (SQLException e) {
            throw e;
        }
    }

    // Helper method to create Post object from ResultSet
    private static Post createPostFromResultSet(ResultSet rs) throws SQLException {
        return new Post(
                rs.getInt("postId"),
                rs.getInt("userId"),
                rs.getString("authorName"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBytes("image1Data"),
                rs.getString("image1Type"),
                rs.getBytes("image2Data"),
                rs.getString("image2Type"),
                rs.getBytes("image3Data"),
                rs.getString("image3Type"),
                rs.getString("status"),
                rs.getTimestamp("createdAt"),
                rs.getTimestamp("updatedAt")
        );
    }

    // File deletion helpers

    /**
     * Deletes a relative path like "uploads/posts/abc.jpg" under the web root.
     * Ensures we only delete inside the /uploads directory to prevent traversal issues.
     */
    private static void safeDelete(String webRootAbsolutePath, String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) return;
        try {
            File webRoot = new File(webRootAbsolutePath);
            File uploadsRoot = new File(webRoot, "uploads"); // only allow deletions under /uploads

            File target = new File(webRoot, relativePath);
            String targetCanon = target.getCanonicalPath();
            String uploadsCanon = uploadsRoot.getCanonicalPath();

            // Only delete if target is actually inside /uploads
            if (targetCanon.startsWith(uploadsCanon) && target.exists() && target.isFile()) {
                boolean ok = target.delete();
                System.out.println("[PostDAO] Delete file " + targetCanon + " -> " + ok);
            } else {
                System.out.println("[PostDAO] Skip delete (outside uploads or not found): " + targetCanon);
            }
        } catch (IOException e) {
            System.out.println("[PostDAO] Error resolving file for deletion: " + e.getMessage());
        }
    }
}