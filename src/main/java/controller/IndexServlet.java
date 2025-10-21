package controller;

import dao.AdvertisementDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Advertisement;
import model.Post;
import service.PostService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "IndexServlet", value = {"", "/index"}, loadOnStartup = 1)
public class IndexServlet extends HttpServlet {
    private final PostService postService = new PostService(); // Add PostService

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ... (your existing code for advertisements)
        try {
            LinkedList<Advertisement> activeAds = AdvertisementDAO.getActiveAdvertisements();
            request.setAttribute("activeAds", activeAds);

            // NEW: Fetch recent posts
            List<Post> recentPosts = postService.getRecentPosts(3); // Get 3 most recent
            request.setAttribute("recentPosts", recentPosts);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to fetch page data: " + e.getMessage());
        }

        RequestDispatcher rd = request.getRequestDispatcher("/trackPaginate"); // Your existing forward
        rd.forward(request, response);
    }
}