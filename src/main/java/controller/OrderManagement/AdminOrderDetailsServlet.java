package controller.OrderManagement;

import dao.OrderDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import model.Track;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/orderDetails")
public class AdminOrderDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is required.");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = OrderDAO.findOrderById(orderId);
            if (order == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
                return;
            }

            User customer = UserDAO.findUserById(order.getUserId());
            List<Track> tracks = OrderDAO.getTracksByOrderId(orderId);

            req.setAttribute("order", order);
            req.setAttribute("customer", customer);
            req.setAttribute("tracks", tracks);

            req.getRequestDispatcher("/admin/manage-orders-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Order ID format.");
        } catch (SQLException e) {
            throw new ServletException("Database error retrieving order details.", e);
        }
    }
}