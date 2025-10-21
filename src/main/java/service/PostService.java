package service;

import dao.PostDAO;
import model.Post;
import model.User;
import model.enums.UserType;
import java.sql.SQLException;
import java.util.List;

public class PostService {

    /**
     * Gets a specified number of recent posts for the home page.
     */
    public List<Post> getRecentPosts(int limit) throws SQLException {
        return PostDAO.getRecentApprovedPosts(limit);
    }

    /**
     * Gets all approved posts for the public community feed.
     */
    public List<Post> getAllApprovedPosts() throws SQLException {
        return PostDAO.getAllApprovedPosts();
    }

    /**
     * Gets all posts for a specific user (for the "My Posts" page).
     */
    public List<Post> getPostsForUser(int userId) throws SQLException {
        return PostDAO.getPostsByUserId(userId);
    }

    /**
     * Gets all posts for the admin panel.
     */
    public List<Post> getAllPostsForAdmin() throws SQLException {
        return PostDAO.getAllPostsForAdmin();
    }

    /**
     * Gets a single post by its ID.
     */
    public Post getPostById(int postId) throws SQLException {
        return PostDAO.getPostById(postId);
    }

    /**
     * Handles the creation of a new post, including setting its initial status.
     * @param post The post object to be created.
     * @param user The user creating the post.
     */
    public void createPost(Post post, User user) throws SQLException {
        // Business Rule: Admins posts are auto-approved. Others are pending.
        if (user.getUserType() == UserType.ADMIN) {
            post.setStatus("approved");
        } else {
            post.setStatus("pending");
        }
        PostDAO.createPost(post);
    }

    /**
     * Handles updating a post, checking permissions, and resetting status.
     * @param post The post with updated information.
     * @param user The user attempting the update.
     * @throws SQLException
     * @throws IllegalAccessException If the user does not have permission.
     */
    public void updatePost(Post post, User user) throws SQLException, IllegalAccessException {
        Post existingPost = PostDAO.getPostById(post.getPostId());
        if (existingPost == null) {
            throw new IllegalArgumentException("Post not found.");
        }

        // Security Check: User must be the owner or an admin.
        if (existingPost.getUserId() != user.getUserId() && user.getUserType() != UserType.ADMIN) {
            throw new IllegalAccessException("You do not have permission to edit this post.");
        }

        // Business Rule: If a non-admin edits, status goes back to pending.
        if (user.getUserType() != UserType.ADMIN) {
            post.setStatus("pending");
        }

        PostDAO.updatePost(post);
    }

    /**
     * Handles deleting a post, checking for permissions.
     * @param postId The ID of the post to delete.
     * @param user The user attempting the deletion.
     * @param webRootPath The absolute path of the web application for file deletion.
     * @throws SQLException
     * @throws IllegalAccessException If the user does not have permission.
     */
    public void deletePost(int postId, User user, String webRootPath) throws SQLException, IllegalAccessException {
        Post post = PostDAO.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found.");
        }

        // Security Check: User must be the owner or an admin.
        if (post.getUserId() != user.getUserId() && user.getUserType() != UserType.ADMIN) {
            throw new IllegalAccessException("You do not have permission to delete this post.");
        }

        PostDAO.deletePostAndFiles(postId, webRootPath);
    }

    /**
     * Updates a post's status. (For Admins)
     * @param postId The post ID.
     * @param status The new status ("approved", "rejected").
     */
    public void updatePostStatus(int postId, String status) throws SQLException {
        if (!status.equals("approved") && !status.equals("rejected")) {
            throw new IllegalArgumentException("Invalid status provided.");
        }
        PostDAO.updatePostStatus(postId, status);
    }
}