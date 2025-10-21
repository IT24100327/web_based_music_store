package controller.UserManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.UserService;

import java.io.IOException;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("USER") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("USER");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String newPassword = req.getParameter("newPassword");

        try {
            // Update user details
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setEmail(email);

            // The service layer handles validation and persistence
            userService.updateUser(currentUser, newPassword);

            // CRITICAL: Update the user object in the session
            session.setAttribute("USER", currentUser);

            resp.sendRedirect(req.getContextPath() + "/profile?view=settings&success=Profile updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile?view=settings&error=" + e.getMessage());
        }
    }
}