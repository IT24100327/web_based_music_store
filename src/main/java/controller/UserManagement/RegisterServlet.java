package controller.UserManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Artist;
import model.User;
import model.enums.UserType;
import dao.UserDAO;
import factory.UserFactory;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String likedGenre = request.getParameter("signup-genre");
        // New: Optional artist signup flag
        boolean isArtist = "artist".equals(request.getParameter("user-type"));

        // Hashing the Password using Bcrypt
        String passwordHash = PasswordUtil.hashPassword(password);

        UserType userType = isArtist ? UserType.ARTIST : UserType.USER;
        User user = UserFactory.createUser(userType.name().toLowerCase(), firstName, lastName, email, passwordHash);
        if (likedGenre != null) {
            user.addLikedGenre(likedGenre);
        }
        if (isArtist && user instanceof Artist) {
            ((Artist) user).setBio(request.getParameter("bio"));
            String[] genres = request.getParameterValues("specialized-genres");
            if (genres != null) {
                ((Artist) user).setSpecializedGenres(List.of(genres));
            }
        }

        try {
            UserDAO.addUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("User added Successfully");

        response.sendRedirect("login.jsp");
    }
}