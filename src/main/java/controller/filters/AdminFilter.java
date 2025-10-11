package controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import model.enums.UserType;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("USER") == null) {
            System.out.println("No user session found - redirecting to login");
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("USER");

        // Check if user is admin (now via UserType)
        if (user != null && user.getUserType() == UserType.ADMIN) {
            System.out.println("User is Admin: " + user.getFirstName() + " is Logged");
            chain.doFilter(request, response);
        } else {
            System.out.println("User is not Admin or user is null");
            res.sendRedirect(req.getContextPath() + "/");
        }
    }
}