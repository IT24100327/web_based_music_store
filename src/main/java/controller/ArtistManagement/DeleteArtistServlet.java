package controller.ArtistManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete-artist")
public class DeleteArtistServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-artists?error=Artist ID is missing");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);

            User artist = userService.findUserById(userId);
            if (artist != null) {
                userService.removeUser(artist);
            }

            response.sendRedirect(request.getContextPath() + "/manage-artists?success=Artist deleted successfully");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-artists?error=Invalid Artist ID format");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/manage-artists?error=Failed to delete artist: " + e.getMessage());
        }
    }
}