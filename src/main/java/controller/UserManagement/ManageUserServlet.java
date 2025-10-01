package controller.UserManagement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Admin;
import model.User;
import model.enums.AdminRole;
import dao.UserDAO;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

@WebServlet("/manageUsers")
public class ManageUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String newPassword = request.getParameter("editPassword");
        String adminRole = request.getParameter("adminRole");

        try {
            switch (action) {
                case "add":
                    addUser(firstName, lastName, email, password, role, adminRole, request, response);
                    break;
                case "update":
                    String userIdStr = request.getParameter("userId");
                    if (userIdStr == null || userIdStr.trim().isEmpty()) {
                        throw new IllegalArgumentException("User ID is missing");
                    }
                    try {
                        int userId = Integer.parseInt(userIdStr);
                        updateUser(userId, firstName, lastName, email, newPassword, role, adminRole, request, response);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid User ID format: " + userIdStr);
                    }
                    break;
                case "delete":
                    int userId = Integer.parseInt(request.getParameter("userId"));
                    deleteUser(userId);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid action: " + action);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response); // Forward to JSP to display error
            return;
        }

        response.sendRedirect(request.getContextPath() + "/manageUsers");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LinkedList<User> allUsers = new LinkedList<>();

        try {
            allUsers = UserDAO.getUsers();
        } catch (SQLException e) {
            req.setAttribute("error", "Failed to fetch users: " + e.getMessage());
        }

        // Set admin roles for JSP dropdowns
        req.setAttribute("adminRoles", Arrays.stream(AdminRole.values())
                .map(AdminRole::getRoleName)
                .collect(Collectors.toList()));
        req.setAttribute("allUsers", allUsers);
        RequestDispatcher rd = req.getRequestDispatcher("/admin/ManageUsers.jsp");
        rd.forward(req, resp);
    }

    private void addUser(String firstName, String lastName, String email, String password, String role, String adminRole, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String passwordHash = PasswordUtil.hashPassword(password);

        try {
            switch (role) {
                case "admin":
                    Admin admin = new Admin(firstName, lastName, email, passwordHash);
                    if (adminRole != null && !adminRole.isEmpty()) {
                        admin.setRole(AdminRole.fromRoleName(adminRole));
                    }
                    UserDAO.addUser(admin);
                    break;
                case "user":
                    User user = new User(firstName, lastName, email, passwordHash);
                    UserDAO.addUser(user);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid role: " + role);
            }
        } catch (SQLException | IOException e) {
            request.setAttribute("error", "Failed to add user: " + e.getMessage());
            doGet(request, response); // Forward to JSP to display error
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid admin role: " + e.getMessage());
            doGet(request, response); // Forward to JSP to display error
        }
    }

    private void updateUser(int userId, String firstName, String lastName, String email, String newPassword, String role, String adminRole, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            UserDAO.updateUser(userId, firstName, lastName, email, role);

            if (newPassword != null && !newPassword.isEmpty()) {
                UserDAO.updateUserPassword(userId, newPassword);
            }

            if ("admin".equals(role) && adminRole != null && !adminRole.isEmpty()) {
                UserDAO.updateAdminRole(userId, AdminRole.fromRoleName(adminRole));
            } else if ("user".equals(role)) {
                // Remove admin role if user is downgraded to non-admin
                UserDAO.updateAdminRole(userId, null);
            }

        } catch (SQLException | IOException e) {
            request.setAttribute("error", "Failed to update user: " + e.getMessage());
            doGet(request, response); // Forward to JSP to display error
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid admin role: " + e.getMessage());
            doGet(request, response); // Forward to JSP to display error
        }
    }

    private void deleteUser(int userId) throws IOException, ServletException {
        try {
            User user = UserDAO.findUserById(userId);
            if (user != null) {
                UserDAO.removeUser(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}