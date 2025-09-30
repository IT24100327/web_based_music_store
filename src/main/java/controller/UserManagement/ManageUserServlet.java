package controller.UserManagement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Admin;
import model.User;
import dao.UserDAO;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

@WebServlet("/manageUsers")
public class ManageUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        String newPassword = request.getParameter("editPassword");

        switch (action) {
            case "add":
                addUser(firstName, lastName, email, password, role);
                break;
            case "update":
                updateUser(firstName, lastName, email, newPassword, role);
                break;
            case "delete":
                // TO DO
                break;
        }

        response.sendRedirect(request.getContextPath() + "/manageUsers");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LinkedList<User> allUsers = new LinkedList<>();

        try {
            allUsers = UserDAO.getUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        req.setAttribute("allUsers", allUsers);
        RequestDispatcher rd = req.getRequestDispatcher("/admin/ManageUsers.jsp");
        rd.forward(req, resp);
    }

    private void addUser(String firstName,
                         String lastName,
                         String email,
                         String password,
                         String role) {

        String passwordHash = PasswordUtil.hashPassword(password);

        switch (role) {
            case "admin" :
                try {
                    Admin admin = new Admin(firstName, lastName, email, passwordHash);
                    UserDAO.addUser(admin);
                    break;
                } catch (SQLException | IOException e) {
                    System.out.println("SQL Issue! Admin User not Added! ");
                }
            case "user":
                try {
                    User user = new User(firstName, lastName, email, passwordHash);
                    UserDAO.addUser(user);
                    break;
                } catch (SQLException | IOException e){
                    System.out.println("SQL Issue! User not Added! ");
            }
        }

    }

    private void updateUser(String firstName,
                         String lastName,
                         String email,
                         String newPassword,
                         String role) {

        try {

            User user = UserDAO.findUserByEmail(email);

            if (user != null) {
                UserDAO.updateUser(user.getUserId(), firstName, lastName, email, role);

                if (!(newPassword.isEmpty())) {
                    UserDAO.updateUserPassword(user.getUserId(), newPassword);
                }
            }

        } catch (SQLException | IOException e) {
            System.out.println("User Update Failed. SQL Error");
        }
    }
}
