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

@WebServlet("/cover-art")
public class CoverArtServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trackIdStr = request.getParameter("trackId");
        if (trackIdStr == null || trackIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Track ID is required.");
            return;
        }

        try {
            int trackId = Integer.parseInt(trackIdStr);
            Track track = TrackDAO.findTrackById(trackId);

            if (track == null || track.getCoverArtData() == null || track.getCoverArtType() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cover art not found.");
                return;
            }

            response.setContentType(track.getCoverArtType());
            response.setContentLength(track.getCoverArtData().length);

            try (OutputStream out = response.getOutputStream()) {
                out.write(track.getCoverArtData());
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Track ID format.");
        } catch (SQLException e) {
            throw new ServletException("Database error retrieving cover art", e);
        }
    }
}