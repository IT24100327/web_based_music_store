package controller.OrderManagement;

import dao.CartDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Track;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderDetailsServlet", value = "/orderDetails")
public class OrderDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Track> cartItems = new ArrayList<>();
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("USER");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        cartItems = (List<Track>) session.getAttribute("cartItems");

        if (cartItems.isEmpty()) {
            try {
                cartItems = CartDAO.getCartItems(user.getUserId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("/order_details.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}