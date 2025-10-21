package controller.filters;

import dao.CartDAO;
import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Track;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/admin/css/")) {
            chain.doFilter(request, response); // Bypass the filter
            return; // Stop further processing by this filter
        }

        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // If no session but cookie exists
        if (session == null || session.getAttribute("USER") == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("userId".equals(cookie.getName())) {
                        session = req.getSession(true);

                        try {
                            User user = UserDAO.findUserById(Integer.parseInt(cookie.getValue()));
                            if (user != null) {
                                session.setAttribute("USER", user);

                                // Load user's cart from database
                                List<Track> cartItems = CartDAO.getCartItems(user.getUserId());
                                session.setAttribute("cartItems", cartItems);

                                double cartTotal = calculateCartTotal(cartItems);
                                session.setAttribute("cartTotal", cartTotal);

                                System.out.println("AuthFilter: Loaded cart for user " + user.getUserId() +
                                        " with " + cartItems.size() + " items (Type: " + user.getUserType() + ")");
                            }
                        } catch (SQLException e) {
                            System.err.println("AuthFilter: Error loading user cart: " + e.getMessage());
                            // Continue without cart data
                            session.setAttribute("cartItems", new ArrayList<>());
                            session.setAttribute("cartTotal", 0.0);
                        }
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    private double calculateCartTotal(List<Track> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (Track track : cartItems) {
                total += track.getPrice();
            }
        }
        return total;
    }
}