// in java/controller/community/MyPostsServlet.java
package controller.PostManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Post;
import model.User;
import service.PostService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/my-posts") // URL remains the same
public class MyPostsServlet extends HttpServlet {

    private final PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("USER") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // SINGLE call to the service layer
            List<Post> myPosts = postService.getPostsForUser(user.getUserId());
            request.setAttribute("myPosts", myPosts);
            request.getRequestDispatcher("/community/my-posts.jsp").forward(request, response);
        } catch (SQLException e) {
            // Log the error and show a user-friendly error page
            e.printStackTrace();
            request.setAttribute("error", "Failed to load your posts due to a database error.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}