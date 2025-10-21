package controller.PostManagement;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Post;
import model.User;
import model.enums.UserType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ManagePostServlet", urlPatterns = {"/admin/manage-posts"})
public class ManagePostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("USER") : null;

        if (user == null || !(user.getUserType() == UserType.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Post> pendingPosts = PostDAO.getPendingPosts();
            List<Post> allPosts = PostDAO.getAllPostsForAdmin();

            request.setAttribute("pendingPosts", pendingPosts);
            request.setAttribute("allPosts", allPosts);

            request.getRequestDispatcher("manage-posts.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
