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

    private static final String UPLOAD_DIR = "uploads/posts";
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
            // Handle file uploads and set paths on the post object
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
     * A helper method to process uploaded files from a multipart request.
     */
    private void handleFileUploads(HttpServletRequest request, Post post) throws IOException, ServletException {
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Get all parts that are file inputs
        List<Part> fileParts = request.getParts().stream()
                .filter(part -> "image1".equals(part.getName()) || "image2".equals(part.getName()) || "image3".equals(part.getName()))
                .filter(part -> part.getSize() > 0)
                .collect(Collectors.toList());
        if (fileParts.size() > 0) {
            Part part1 = request.getPart("image1");
            if(part1 != null && part1.getSize() > 0) {
                String fileName1 = System.currentTimeMillis() + "_1_" + part1.getSubmittedFileName();
                part1.write(uploadPath + File.separator + fileName1);
                post.setImage1Path(UPLOAD_DIR + "/" + fileName1);
            }
        }
        if (fileParts.size() > 1) {
            Part part2 = request.getPart("image2");
            if(part2 != null && part2.getSize() > 0) {
                String fileName2 = System.currentTimeMillis() + "_2_" + part2.getSubmittedFileName();
                part2.write(uploadPath + File.separator + fileName2);
                post.setImage2Path(UPLOAD_DIR + "/" + fileName2);
            }
        }
        if (fileParts.size() > 2) {
            Part part3 = request.getPart("image3");
            if(part3 != null && part3.getSize() > 0) {
                String fileName3 = System.currentTimeMillis() + "_3_" + part3.getSubmittedFileName();
                part3.write(uploadPath + File.separator + fileName3);
                post.setImage3Path(UPLOAD_DIR + "/" + fileName3);
            }
        }
    }
}