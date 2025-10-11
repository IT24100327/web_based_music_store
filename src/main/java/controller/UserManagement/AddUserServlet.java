package controller.UserManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Admin;
import model.Artist;
import model.User;
import model.enums.AdminRole;
import model.enums.UserType;
import service.UserService;
import utils.PasswordUtil;
import factory.UserFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/add-user")
public class AddUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String roleStr = request.getParameter("role");
        String adminRole = request.getParameter("adminRole");

        try {
            UserType userType;
            try {
                userType = UserType.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + roleStr);
            }

            addUser(firstName, lastName, email, password, userType, adminRole, request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            // Forward to list or error page
            response.sendRedirect(request.getContextPath() + "/manage-users?error=" + e.getMessage());
            return;
        }

        response.sendRedirect(request.getContextPath() + "/manage-users");
    }

    private void addUser(String firstName, String lastName, String email, String password, UserType userType, String adminRole, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String passwordHash = PasswordUtil.hashPassword(password);

        try {
            User newUser = UserFactory.createUser(userType.name().toLowerCase(), firstName, lastName, email, passwordHash);

            if (userType == UserType.ADMIN && adminRole != null && !adminRole.isEmpty()) {
                if (newUser instanceof Admin) {
                    ((Admin) newUser).setRole(AdminRole.fromRoleName(adminRole));
                }
            } else if (userType == UserType.ARTIST) {
                // Optional: Set default artist details
                if (newUser instanceof Artist) {
                    ((Artist) newUser).setBio("New Artist Bio");
                    ((Artist) newUser).setSpecializedGenres(List.of("Pop", "Rock"));
                }
            }

            userService.addUser(newUser);
        } catch (SQLException | IOException e) {
            request.setAttribute("error", "Failed to add user: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/manage-users?error=" + e.getMessage());
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid details for " + userType + ": " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/manage-users?error=" + e.getMessage());
        }
    }
}