package controller.PostManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import model.enums.UserType;
import service.PostService;
import java.io.IOException;

@WebServlet("/community/delete")
public class DeletePostServlet extends HttpServlet {

    private final PostService postService = new PostService();

    /**
     * Handles POST requests to delete a post.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("USER") : null;

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to perform this action.");
            return;
        }

        String redirectPath = (user.getUserType() == UserType.ADMIN)
                ? request.getContextPath() + "/admin/manage-posts"
                : request.getContextPath() + "/my-posts";

        try {
            int postId = Integer.parseInt(request.getParameter("postId"));

            // Get the web application's real path to pass to the service for file deletion
            String webRootPath = getServletContext().getRealPath("");

            // The service layer handles the permission check and deletion logic
            postService.deletePost(postId, user, webRootPath);

            // Optional: Add a success flash message to the session
            session.setAttribute("successMessage", "Post deleted successfully.");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid Post ID provided.");
        } catch (IllegalAccessException e) {
            // This is thrown by the service if the user doesn't have permission
            session.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while deleting the post.");
        }

        response.sendRedirect(redirectPath);
    }
}