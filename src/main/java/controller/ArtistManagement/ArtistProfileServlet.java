package controller.ArtistManagement;

import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Artist;
import model.Track;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/artist/profile")
public class ArtistProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        // Security check: Ensure the user is an artist
        if (user == null || !(user instanceof Artist)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in as an artist to view this page.");
            return;
        }

        Artist artist = (Artist) user;
        String view = request.getParameter("view");
        if (view == null || view.trim().isEmpty()) {
            view = "dashboard"; // Default to the dashboard view
        }

        try {
            // Fetch tracks for the logged-in artist, which are needed for dashboard and my-tracks views
            List<Track> artistTracks = TrackDAO.getTracksByArtistId(artist.getUserId());
            request.setAttribute("artistTracks", artistTracks);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load your tracks: " + e.getMessage());
        }

        request.setAttribute("view", view); // Pass the current view to the JSP
        request.getRequestDispatcher("/artist/profile.jsp").forward(request, response);
    }
}