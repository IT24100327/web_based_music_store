package controller.UserManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.enums.AdminRole;
import model.enums.UserType;
import service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/update-user")
public class UpdateUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

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
            String adminRole = request.getParameter("adminRole");

            try {
                UserType userType = UserType.valueOf(roleStr.toUpperCase());
                updateUser(userId, firstName, lastName, email, newPassword, userType, adminRole, request, response);
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath() + "/manage-users?error=Invalid role: " + roleStr);
                return;
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Invalid User ID format");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/manage-users");
    }

    private void updateUser(int userId, String firstName, String lastName, String email, String newPassword, UserType userType, String adminRole, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            userService.updateUser(userId, firstName, lastName, email, userType.name().toLowerCase());

            if (newPassword != null && !newPassword.isEmpty()) {
                userService.updateUserPassword(userId, newPassword);
            }

            if (userType == UserType.ADMIN && adminRole != null && !adminRole.isEmpty()) {
                userService.updateAdminRole(userId, AdminRole.fromRoleName(adminRole));
            } else if (userType == UserType.USER || userType == UserType.ARTIST) {
                // Remove admin role if downgraded
                userService.updateAdminRole(userId, null);
            }

            // Artist-specific update example
            if (userType == UserType.ARTIST) {
                String bio = request.getParameter("bio");
                String[] genres = request.getParameterValues("specializedGenres");
                if (bio != null || genres != null) {
                    userService.updateArtistDetails(userId, bio, List.of(genres != null ? genres : new String[0]));
                }
            }

        } catch (SQLException | IOException e) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Failed to update user: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Invalid details for " + userType + ": " + e.getMessage());
        }
    }
}