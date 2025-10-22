package controller.community;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Post;
import model.User;
import model.enums.UserType;
import service.PostService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/community/edit")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1MB
        maxFileSize = 1024 * 1024 * 10,     // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class EditPostServlet extends HttpServlet {

    private final PostService postService = new PostService();

    /**
     * Handles GET requests by fetching the post to be edited and forwarding to the form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int postId = Integer.parseInt(request.getParameter("postId"));
            Post post = postService.getPostById(postId);

            // Security Check: The service layer will check again on POST, but it's good practice
            // to prevent users from even seeing the edit page if they don't have permission.
            if (post == null || (post.getUserId() != user.getUserId() && user.getUserType() != UserType.ADMIN)) {
                response.sendRedirect(request.getContextPath() + "/my-posts");
                return;
            }

            request.setAttribute("post", post);
            request.getRequestDispatcher("/community/create-edit-post.jsp").forward(request, response);
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            // Redirect if postId is invalid or a DB error occurs
            response.sendRedirect(request.getContextPath() + "/my-posts");
        }
    }

    /**
     * Handles POST requests by processing the updated post data.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int postId = Integer.parseInt(request.getParameter("postId"));
            Post postToUpdate = postService.getPostById(postId); // Get the existing post from DB

            if (postToUpdate == null) {
                // Handle case where post was deleted while user was editing
                response.sendRedirect(request.getContextPath() + "/my-posts?error=Post not found.");
                return;
            }

            // Update fields from form
            postToUpdate.setTitle(request.getParameter("title"));
            postToUpdate.setDescription(request.getParameter("description"));

            // File upload logic handles new/updated images
            handleFileUploads(request, postToUpdate);
            // The service handles permission checks and business rules (like status reset)
            postService.updatePost(postToUpdate, user);
            // Redirect based on user role
            if (user.getUserType() == UserType.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/admin/manage-posts");
            } else {
                response.sendRedirect(request.getContextPath() + "/my-posts");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/my-posts?error=Invalid post ID.");
        } catch (IllegalAccessException e) {
            // This catches the permission error from the service layer
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/my-posts?error=Database error during update.");
        }
    }

    private void handleFileUploads(HttpServletRequest request, Post post) throws IOException, ServletException {
        Part part1 = request.getPart("image1");
        if (part1 != null && part1.getSize() > 0) {
            try (InputStream is = part1.getInputStream()) {
                post.setImage1Data(is.readAllBytes());
                post.setImage1Type(part1.getContentType());
            }
        }

        Part part2 = request.getPart("image2");
        if (part2 != null && part2.getSize() > 0) {
            try (InputStream is = part2.getInputStream()) {
                post.setImage2Data(is.readAllBytes());
                post.setImage2Type(part2.getContentType());
            }
        }

        Part part3 = request.getPart("image3");
        if (part3 != null && part3.getSize() > 0) {
            try (InputStream is = part3.getInputStream()) {
                post.setImage3Data(is.readAllBytes());
                post.setImage3Type(part3.getContentType());
            }
        }
    }
}