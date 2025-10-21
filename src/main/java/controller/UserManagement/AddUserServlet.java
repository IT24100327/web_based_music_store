package controller.UserManagement;

import factory.UserFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Admin;
import model.User;
import model.enums.AdminRole;
import model.enums.UserType;
import service.UserService;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/add-user")
public class AddUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String roleStr = request.getParameter("role");
        String adminRoleStr = request.getParameter("adminRole");

        try {
            UserType userType = UserType.valueOf(roleStr.toUpperCase());
            String passwordHash = PasswordUtil.hashPassword(password);

            // Create the appropriate user object using the factory
            User newUser = UserFactory.createUser(userType.name().toLowerCase(), firstName, lastName, email, passwordHash);

            // If the user is an Admin, set their specific role
            if (userType == UserType.ADMIN && newUser instanceof Admin) {
                if (adminRoleStr != null && !adminRoleStr.isEmpty()) {
                    ((Admin) newUser).setRole(AdminRole.valueOf(adminRoleStr));
                }
            }

            // A single call to the service handles validation and persistence
            userService.addUser(newUser);
            response.sendRedirect(request.getContextPath() + "/manage-users?success=User added successfully");

        } catch (IllegalArgumentException | SQLException e) {
            e.printStackTrace(); // Log the error for debugging
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Failed to add user: " + e.getMessage());
        }
    }
}