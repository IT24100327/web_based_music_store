package controller.UserAuth.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Admin;
import model.enums.AdminRole;

@WebFilter(urlPatterns = {"/manageUsers", "/marketing"})
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Admin user = (Admin) req.getSession().getAttribute("USER");

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

    private static boolean isAuthorized(HttpServletRequest req, Admin user) {
        String path = req.getServletPath();
        AdminRole role = user.getRole();
        boolean authorized = false;

        if ("/manageUsers".equals(path) && role == AdminRole.SUPER_ADMIN) {
            authorized = true;
        } else if ("/marketing".equals(path) && (role == AdminRole.SUPER_ADMIN || role == AdminRole.MARKETING_MANAGER)) {
            authorized = true;
        }

        return authorized;
    }
}