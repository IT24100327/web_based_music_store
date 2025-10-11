package controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Admin;
import model.User;
import model.enums.AdminRole;
import model.enums.UserType;

import java.io.IOException;

@WebFilter(urlPatterns = {"/manageUsers", "/marketing"})
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        User user = (User) req.getSession().getAttribute("USER");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        boolean authorized = isAuthorized(req, user);

        if (authorized) {
            chain.doFilter(request, response);
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }

    private static boolean isAuthorized(HttpServletRequest req, User user) {
        String path = req.getServletPath();
        if (user.getUserType() != UserType.ADMIN) {
            return false; // Non-admins denied
        }
        AdminRole role = ((Admin) user).getRole();
        return switch (path) {
            case "/manageUsers" -> role == AdminRole.SUPER_ADMIN;
            case "/marketing" -> role == AdminRole.SUPER_ADMIN || role == AdminRole.MARKETING_MANAGER;
            default -> false;
        };
    }
}