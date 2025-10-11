package controller.UserManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete-user")
public class DeleteUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=User ID is missing");
            return;
        }
        try {
            int userId = Integer.parseInt(userIdStr);
            deleteUser(userId, request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Invalid User ID format");
        }
    }

    private void deleteUser(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            User user = userService.findUserById(userId);
            if (user != null) {
                userService.removeUser(user);
            }
            response.sendRedirect(request.getContextPath() + "/manage-users");
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Failed to delete user: " + e.getMessage());
        }
    }
}