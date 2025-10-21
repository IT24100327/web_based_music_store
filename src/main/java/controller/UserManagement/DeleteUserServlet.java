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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=User ID is missing");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);

            // The existing logic is correct and aligns with the consolidated service.
            User user = userService.findUserById(userId);
            if (user != null) {
                userService.removeUser(user);
            }

            response.sendRedirect(request.getContextPath() + "/manage-users?success=User deleted successfully");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Invalid User ID format.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/manage-users?error=Failed to delete user: " + e.getMessage());
        }
    }
}