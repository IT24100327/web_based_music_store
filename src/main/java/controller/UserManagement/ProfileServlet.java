package controller.UserManagement;

import dao.OrderDAO;
import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import model.Post;
import model.Track;
import model.User;
import service.PostService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("USER");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Determine which view to show (dashboard, music, orders, settings)
        String view = req.getParameter("view");
        if (view == null || view.trim().isEmpty()) {
            view = "dashboard";
        }

        try {
            // Fetch all necessary data for the profile sections
            List<Track> purchasedTracks = TrackDAO.getPurchasedTracksByUserId(user.getUserId());
            List<Order> userOrders = OrderDAO.getOrdersByUserId(user.getUserId());

            // Fetch user's posts if viewing the 'my-posts' section
            if ("my-posts".equals(view)) {
                List<Post> myPosts = postService.getPostsForUser(user.getUserId());
                req.setAttribute("myPosts", myPosts);
            }

            req.setAttribute("purchasedTracks", purchasedTracks);
            req.setAttribute("userOrders", userOrders);
            req.setAttribute("view", view);

            req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error loading profile data.", e);
        }
    }
}