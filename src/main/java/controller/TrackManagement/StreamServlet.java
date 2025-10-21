package controller.TrackManagement;

import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Track;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

@WebServlet("/stream") // The URL mapping remains the same
public class StreamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trackIdStr = request.getParameter("trackId");
        if (trackIdStr == null || trackIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Track ID is required.");
            return;
        }

        try {
            int trackId = Integer.parseInt(trackIdStr);
            Track track = TrackDAO.findTrackById(trackId);

            if (track == null || track.getFullTrackData() == null || track.getFullTrackData().length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Streamable audio data not found for this track.");
                return;
            }

            byte[] audioData = track.getFullTrackData();

            response.setContentType("audio/mpeg"); // Assuming MP3, adjust if needed
            response.setContentLength(audioData.length);
            response.setHeader("Accept-Ranges", "bytes");

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