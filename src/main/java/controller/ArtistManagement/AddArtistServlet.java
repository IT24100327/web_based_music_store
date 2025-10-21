package controller.ArtistManagement;

import factory.UserFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Artist;
import model.User;
import service.UserService;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/add-artist")
public class AddArtistServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String stageName = request.getParameter("stageName");
        String bio = request.getParameter("bio");

        String passwordHash = PasswordUtil.hashPassword(password);

        try {
            // The UserFactory still correctly creates an Artist instance
            User newArtist = UserFactory.createUser("artist", firstName, lastName, email, passwordHash);

            if (newArtist instanceof Artist artist) {
                // Set default values if parameters are empty
                artist.setStageName(stageName != null && !stageName.trim().isEmpty() ? stageName : firstName + " " + lastName);
                artist.setBio(bio != null && !bio.trim().isEmpty() ? bio : "A new artist.");
            }

            // The consolidated UserService's addUser method handles the logic for all user types
            userService.addUser(newArtist);
            response.sendRedirect(request.getContextPath() + "/manage-artists?success=Artist added successfully");

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/manage-artists?error=Failed to add artist: " + e.getMessage());
        }
    }
}