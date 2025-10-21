package controller.PostManagement;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import model.enums.UserType;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ApprovePostServlet", urlPatterns = {"/posts/approve"})
public class ApprovePostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("USER") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Only admins can approve/reject posts
        if (!(user.getUserType() == UserType.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String postIdStr = request.getParameter("postId");
        String action = request.getParameter("action");

        if (postIdStr == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/manage-posts");
            return;
        }

        try {
            int postId = Integer.parseInt(postIdStr);
            if ("approve".equalsIgnoreCase(action)) {
                PostDAO.updatePostStatus(postId, "approved");
                // session.setAttribute("FLASH_SUCCESS", "Post approved successfully!");
            } else if ("reject".equalsIgnoreCase(action)) {
                PostDAO.updatePostStatus(postId, "rejected");
                // session.setAttribute("FLASH_SUCCESS", "Post rejected successfully!");
            } else {
                // session.setAttribute("FLASH_ERROR", "Unknown action.");
            }
            // PRG back to admin list
            response.sendRedirect(request.getContextPath() + "/admin/manage-posts");
        } catch (SQLException | NumberFormatException e) {
            // session.setAttribute("FLASH_ERROR", "Error updating post status.");
            response.sendRedirect(request.getContextPath() + "/admin/manage-posts");
        }
    }
}
