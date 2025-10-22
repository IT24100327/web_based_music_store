package controller.ArtistManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Artist;
import service.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/manage-artists")
public class ManageArtistViewServlet extends HttpServlet {
    // Dependency is now the consolidated UserService
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Artist> allArtists = new ArrayList<>();

        try {
            // Call the method that is now located in UserService
            allArtists = userService.getArtists();
        } catch (SQLException e) {
            e.printStackTrace(); // It's better to log the error than to throw a RuntimeException
            req.setAttribute("error", "Failed to retrieve artists from the database.");
        }

        req.setAttribute("allArtists", allArtists);
        req.getRequestDispatcher("/admin/manage-artists.jsp").forward(req, resp);
    }
}