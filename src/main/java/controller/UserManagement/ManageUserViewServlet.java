package controller.UserManagement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.enums.AdminRole;
import model.enums.UserType;
import service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/manage-users")
public class ManageUserViewServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LinkedList<User> allUsers = new LinkedList<>();

        try {
            allUsers = userService.getAllUsers();  // Delegate to Service
        } catch (SQLException e) {
            req.setAttribute("error", "Failed to fetch users: " + e.getMessage());
        }

        // Set user types for JSP dropdowns
        List<String> userTypes = Arrays.stream(UserType.values())
                .map(Enum::name)
                .toList();
        req.setAttribute("userTypes", userTypes);

        // Set admin roles for JSP dropdowns (only for admin type)
        req.setAttribute("adminRoles", Arrays.stream(AdminRole.values())
                .map(AdminRole::getRoleName)
                .collect(Collectors.toList()));

        req.setAttribute("allUsers", allUsers);

        RequestDispatcher rd = req.getRequestDispatcher("/admin/manage-users.jsp");
        rd.forward(req, resp);
    }
}