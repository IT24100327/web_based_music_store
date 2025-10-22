package controller.ArtistManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Artist;
import model.User;
import service.UserService;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/artist/update-profile")
public class ArtistProfileUpdateServlet extends HttpServlet {

    private final UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Security check: ensure a logged-in user exists and is an artist
        if (session == null || session.getAttribute("USER") == null || !(session.getAttribute("USER") instanceof Artist)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Unauthorized access");
            return;
        }

        Artist currentArtist = (Artist) session.getAttribute("USER");
        String userIdStr = request.getParameter("userId");
        try {
            int userId = Integer.parseInt(userIdStr);
            // Security check: ensure the artist is only updating their own profile
            if (userId != currentArtist.getUserId()) {
                response.sendRedirect(request.getContextPath() + "/artist/profile?view=settings&error=You can only update your own profile.");
                return;
            }

            // 1. Create an Artist object from the request parameters.
            Artist artistToUpdate = new Artist();
            artistToUpdate.setUserId(userId);
            artistToUpdate.setFirstName(request.getParameter("firstName"));
            artistToUpdate.setLastName(request.getParameter("lastName"));
            artistToUpdate.setEmail(request.getParameter("email"));
            artistToUpdate.setStageName(request.getParameter("stageName"));
            artistToUpdate.setBio(request.getParameter("bio"));

            String newPassword = request.getParameter("editPassword");
            // 2. Make a single, unified call to the UserService.
            userService.updateArtist(artistToUpdate, newPassword);

            // 3. Update the session with the latest user details from the database to reflect changes.
            User updatedUser = userService.findUserById(userId);
            session.setAttribute("USER", updatedUser);

            // 4. Redirect back to the profile page with a success message
            response.sendRedirect(request.getContextPath() + "/artist/profile?view=settings&success=Profile updated successfully!");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/artist/profile?view=settings&error=Invalid User ID format.");
        } catch (IllegalArgumentException | SQLException e) {
            e.printStackTrace(); // Log the full error for debugging.
            response.sendRedirect(request.getContextPath() + "/artist/profile?view=settings&error=Update failed: " + e.getMessage());
        }
    }
}