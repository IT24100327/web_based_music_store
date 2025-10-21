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
import utils.ImageUploadUtil;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@WebServlet("/artist/add-track")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50,      // 50MB
        maxRequestSize = 1024 * 1024 * 100)   // 100MB
public class AddTrackServlet extends HttpServlet {

    private final ImageUploadUtil imageUtil = new ImageUploadUtil();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to upload tracks.");
            return;
        }

        try {
            String title = request.getParameter("title");
            double price = Double.parseDouble(request.getParameter("price"));
            String genre = request.getParameter("genre");
            int duration = Integer.parseInt(request.getParameter("duration"));
            LocalDate releaseDate = LocalDate.parse(request.getParameter("release_date"));

            Part audioPart = request.getPart("audioFile");
            Part coverArtPart = request.getPart("coverArtFile");

            byte[] audioData = null;
            if (audioPart != null && audioPart.getSize() > 0) {
                try (InputStream audioInputStream = audioPart.getInputStream()) {
                    audioData = audioInputStream.readAllBytes();
                }
            }

            byte[] coverArtData = null;
            String coverArtType = null;
            if (coverArtPart != null && coverArtPart.getSize() > 0) {
                try (InputStream coverArtInputStream = coverArtPart.getInputStream()) {
                    coverArtData = coverArtInputStream.readAllBytes();
                    coverArtType = coverArtPart.getContentType(); // Get the MIME type (e.g., "image/jpeg")
                }
            }

            Track newTrack = new Track();
            newTrack.setTitle(title);
            newTrack.setPrice(price);
            newTrack.setGenre(genre);
            newTrack.setDuration(duration);
            newTrack.setReleaseDate(releaseDate);
            newTrack.setArtistId(user.getUserId());
            newTrack.setRating(0.0);

            newTrack.setFullTrackData(audioData);
            newTrack.setCoverArtData(coverArtData);
            newTrack.setCoverArtType(coverArtType);

            // Note: Snippet generation is a complex task. For now, we are omitting it
            // to ensure the core functionality of saving and streaming works.
            // newTrack.setSnippetData(generateSnippet(audioData));

            TrackDAO.addTrack(newTrack);

            String destination = request.getContextPath() + (user.getUserType().name().equals("ARTIST") ? "/artist/profile" : "/manage-tracks") + "?success=Track added successfully";
            response.sendRedirect(destination);

        } catch (Exception e) {
            e.printStackTrace();
            String destination = request.getContextPath() + (user.getUserType().name().equals("ARTIST") ? "/artist/profile" : "/manage-tracks") + "?error=An error occurred: " + e.getMessage();
            response.sendRedirect(destination);
        }
    }
}