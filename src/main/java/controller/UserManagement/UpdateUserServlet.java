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

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/update-user")
public class UpdateUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=User ID is missing");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String newPassword = request.getParameter("editPassword");
            String roleStr = request.getParameter("role");
            String adminRoleStr = request.getParameter("adminRole");
            UserType userType = UserType.valueOf(roleStr.toUpperCase());

            // 1. Create the appropriate User object from the request.
            // We use a dummy password here because it's handled separately.
            User userToUpdate = UserFactory.createUser(roleStr.toLowerCase(), firstName, lastName, email, "dummyPassword");
            userToUpdate.setUserId(userId);

            if (userType == UserType.ADMIN && userToUpdate instanceof Admin) {
                if (adminRoleStr != null && !adminRoleStr.isEmpty()) {
                    ((Admin) userToUpdate).setRole(AdminRole.valueOf(adminRoleStr));
                }
            }

            // 2. Make a single, unified call to the UserService.
            // A corresponding method needs to be created in UserService to handle this.
            userService.updateUser(userToUpdate, newPassword);

            response.sendRedirect(request.getContextPath() + "/manage-users?success=User updated successfully");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Invalid User ID format.");
        } catch (IllegalArgumentException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Update failed: " + e.getMessage());
        }
    }
}