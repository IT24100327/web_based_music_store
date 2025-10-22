package controller.PostManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Post;
import model.User;
import service.PostService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/community/create")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1MB
        maxFileSize = 1024 * 1024 * 10,     // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class CreatePostServlet extends HttpServlet {

    private final PostService postService = new PostService();

    /**
     * Handles GET requests by forwarding to the post creation form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ensure user is logged in before showing the form
        if (request.getSession(false) == null || request.getSession(false).getAttribute("USER") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // The form is located in the /community/ directory now
        request.getRequestDispatcher("/community/create-edit-post.jsp").forward(request, response);
    }

    /**
     * Handles POST requests by processing the submitted post data and file uploads.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("USER") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired. Please log in.");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        // Basic validation
        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()) {
            request.setAttribute("error", "Title and description are required.");
            request.getRequestDispatcher("/community/create-edit-post.jsp").forward(request, response);
            return;
        }

        Post post = new Post(user.getUserId(), user.getFullName(), title, description);
        try {
            // Handle file uploads and set bytes/types on the post object
            handleFileUploads(request, post);
            // Use the service to handle business logic and save the post
            postService.createPost(post, user);
            // MODIFIED: Redirect to the user's profile post list on success
            response.sendRedirect(request.getContextPath() + "/profile?view=my-posts&success=Post+created+successfully+and+is+pending+approval.");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "A database error occurred while creating the post: " + e.getMessage());
            request.setAttribute("post", post);
            // Repopulate form with entered data
            request.getRequestDispatcher("/community/create-edit-post.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.setAttribute("post", post);
            request.getRequestDispatcher("/community/create-edit-post.jsp").forward(request, response);
        }
    }

    /**
     * A helper method to process uploaded files into byte arrays.
     */
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