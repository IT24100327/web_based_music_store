package controller.OrderManagement;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/my-orders")
public class UserOrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("USER");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            List<Order> orders = OrderDAO.getOrdersByUserId(user.getUserId());
            req.setAttribute("userOrders", orders);
            req.getRequestDispatcher("/my-orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error fetching user orders.", e);
        }
    }
}