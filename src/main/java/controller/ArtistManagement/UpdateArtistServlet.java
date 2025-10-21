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

@WebServlet("/update-artist")
public class UpdateArtistServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-artists?error=Artist ID is missing");
            return;
        }

        try {
            // 1. Create an Artist object from the request parameters.
            Artist artist = new Artist();
            artist.setUserId(Integer.parseInt(userIdStr));
            artist.setFirstName(request.getParameter("firstName"));
            artist.setLastName(request.getParameter("lastName"));
            artist.setEmail(request.getParameter("email"));
            artist.setStageName(request.getParameter("stageName"));
            artist.setBio(request.getParameter("bio"));

            String newPassword = request.getParameter("editPassword");

            // 2. Make a single, unified call to the UserService.
            userService.updateArtist(artist, newPassword);

            response.sendRedirect(request.getContextPath() + "/manage-artists?success=Artist updated successfully");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-artists?error=Invalid Artist ID format.");
        } catch (IllegalArgumentException | SQLException e) {
            // This catches validation errors (like an empty bio) from the service.
            e.printStackTrace(); // Log the full error for debugging.
            response.sendRedirect(request.getContextPath() + "/manage-artists?error=Update failed: " + e.getMessage());
        }
    }
}