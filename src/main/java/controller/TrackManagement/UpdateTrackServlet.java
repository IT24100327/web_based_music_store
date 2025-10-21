package controller.TrackManagement;

import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Track;
import model.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/artist/update-track")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50,      // 50MB
        maxRequestSize = 1024 * 1024 * 100)   // 100MB
public class UpdateTrackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");

        if (user == null || !(user.getUserType().name().equals("ARTIST") || user.getUserType().name().equals("ADMIN"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You do not have permission to perform this action.");
            return;
        }

        String redirectPath = request.getContextPath() + (user.getUserType().name().equals("ARTIST") ? "/artist/profile" : "/manage-tracks");

        try {
            int trackId = Integer.parseInt(request.getParameter("trackId"));

            Track existingTrack = TrackDAO.findTrackById(trackId);
            if (existingTrack == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Track not found.");
                return;
            }

            // Security Check: An ARTIST can only edit their own tracks. An ADMIN can edit any.
            if (user.getUserType().name().equals("ARTIST") && existingTrack.getArtistId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to edit this track.");
                return;
            }

            existingTrack.setTitle(request.getParameter("title"));
            existingTrack.setPrice(Double.parseDouble(request.getParameter("price")));
            existingTrack.setGenre(request.getParameter("genre"));
            existingTrack.setDuration(Integer.parseInt(request.getParameter("duration")));
            existingTrack.setReleaseDate(LocalDate.parse(request.getParameter("release_date")));

            Part audioPart = request.getPart("audioFile");
            if (audioPart != null && audioPart.getSize() > 0) {
                try (InputStream audioInputStream = audioPart.getInputStream()) {
                    existingTrack.setFullTrackData(audioInputStream.readAllBytes());
                    // Note: Snippet generation logic would go here if implemented
                }
            }

            Part coverArtPart = request.getPart("coverArtFile");
            if (coverArtPart != null && coverArtPart.getSize() > 0) {
                try (InputStream coverArtInputStream = coverArtPart.getInputStream()) {
                    existingTrack.setCoverArtData(coverArtInputStream.readAllBytes());
                    existingTrack.setCoverArtType(coverArtPart.getContentType());
                }
            }

            TrackDAO.updateTrack(existingTrack);

            response.sendRedirect(redirectPath + "?success=Track updated successfully");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(redirectPath + "?error=Invalid number format for price or duration.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(redirectPath + "?error=Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(redirectPath + "?error=An unexpected error occurred: " + e.getMessage());
        }
    }
}