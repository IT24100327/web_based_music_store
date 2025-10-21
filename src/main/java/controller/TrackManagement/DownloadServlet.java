package controller.TrackManagement;

import dao.OrderDAO;
import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Track;
import model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("USER") : null;

        String trackIdStr = request.getParameter("trackId");
        if (trackIdStr == null || trackIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Track ID is required.");
            return;
        }

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to download tracks.");
            return;
        }

        try {
            int trackId = Integer.parseInt(trackIdStr);

            // **CRITICAL SECURITY CHECK** (This logic remains the same)
            boolean hasPurchased = OrderDAO.hasUserPurchasedTrack(user.getUserId(), trackId);

            if (!hasPurchased) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. You have not purchased this track.");
                return;
            }

            Track track = TrackDAO.findTrackById(trackId);

            // **MODIFIED**: Check for the byte array instead of a file path
            if (track == null || track.getFullTrackData() == null || track.getFullTrackData().length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Downloadable file not found for this track.");
                return;
            }

            byte[] audioData = track.getFullTrackData();

            String fileName = track.getTitle().replaceAll("[^a-zA-Z0-9.-]", "_") + ".mp3";

            response.setContentType("audio/mpeg"); // Assuming MP3 format
            response.setContentLength(audioData.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            try (OutputStream out = response.getOutputStream()) {
                out.write(audioData);
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Track ID format.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A database error occurred.");
        }
    }
}