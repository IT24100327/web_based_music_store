// in java/controller/community/CommunityServlet.java
package controller.PostManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Post;
import service.PostService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/community")
public class CommunityServlet extends HttpServlet {
    private final PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Post> posts = postService.getAllApprovedPosts();
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("/community/community.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load community feed", e);
        }
    }
}