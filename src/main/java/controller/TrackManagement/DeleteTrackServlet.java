package controller.TrackManagement;

import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Track;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/artist/delete-track")
public class DeleteTrackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");

        if (user == null || !(user.getUserType().name().equals("ARTIST") || user.getUserType().name().equals("ADMIN"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You do not have permission to perform this action.");
            return;
        }

        String trackIdStr = request.getParameter("trackId");
        if (trackIdStr == null || trackIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Track ID is missing.");
            return;
        }

        String redirectPath = request.getContextPath() + (user.getUserType().name().equals("ARTIST") ? "/artist/profile" : "/manage-tracks");

        try {
            int trackId = Integer.parseInt(trackIdStr);

            Track trackToDelete = TrackDAO.findTrackById(trackId);

            if (trackToDelete == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Track not found.");
                return;
            }

            // Security Check: An ARTIST can only delete their own tracks. An ADMIN can delete any track.
            if (user.getUserType().name().equals("ARTIST") && trackToDelete.getArtistId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to delete this track.");
                return;
            }

            TrackDAO.removeTrack(trackId);

            response.sendRedirect(redirectPath + "?success=Track deleted successfully");

        } catch (NumberFormatException e) {
            response.sendRedirect(redirectPath + "?error=Invalid track ID format.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(redirectPath + "?error=Database error: " + e.getMessage());
        }
    }
}