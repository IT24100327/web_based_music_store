package controller.UserManagement;

import factory.UserFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Artist;
import model.User;
import model.enums.UserType;
import service.UserService;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Gather all potential parameters from the request
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String likedGenre = request.getParameter("signup-genre");
        String userTypeParam = request.getParameter("user-type");
        boolean isArtist = "artist".equals(userTypeParam);

        try {
            // 2. Create the appropriate User or Artist object
            String passwordHash = PasswordUtil.hashPassword(password);
            UserType userType = isArtist ? UserType.ARTIST : UserType.USER;
            User user = UserFactory.createUser(userType.name().toLowerCase(), firstName, lastName, email, passwordHash);

            user.addLikedGenre(likedGenre);

            // If the user is an artist, populate the artist-specific fields
            if (isArtist && user instanceof Artist) {
                Artist artist = (Artist) user;
                String stageName = request.getParameter("stage-name");
                String bio = request.getParameter("bio");
                String[] genres = request.getParameterValues("specialized-genres");

                artist.setStageName(stageName);
                artist.setBio(bio);

                if (genres != null) {
                    artist.setSpecializedGenres(Arrays.asList(genres));
                }
            }

            // 3. Make a single call to the service layer to handle validation and persistence
            userService.addUser(user);

            // 4. Redirect on success
            response.sendRedirect(request.getContextPath() + "/login.jsp?success=Registration successful! Please login.");

        } catch (IllegalArgumentException | SQLException e) {
            // 5. Catch validation or database errors from the service and forward back to the form
            e.printStackTrace(); // Log the error for debugging
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/signup.jsp");
    }
}