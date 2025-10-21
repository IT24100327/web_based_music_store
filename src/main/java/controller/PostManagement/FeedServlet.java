package controller.PostManagement;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "FeedServlet", urlPatterns = {"/feed"})
public class FeedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Post> posts = PostDAO.getAllApprovedPosts();
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("community/feed.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load community feed", e);
        }
    }
}
