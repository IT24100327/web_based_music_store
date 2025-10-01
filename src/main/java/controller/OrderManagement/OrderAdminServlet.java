package controller.OrderManagement;

import dao.OrderDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

@WebServlet("/manageOrders")
public class OrderAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        LinkedList<Order> allOrders = new LinkedList<>();
        try {
            allOrders = OrderDAO.getOrders();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }

        req.setAttribute("allOrders", allOrders);
        RequestDispatcher rd = req.getRequestDispatcher("admin/ManageOrders.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "update_status":
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");
                updateOrderStatus(orderId, status);
                break;
            case "delete":
                int deleteOrderId = Integer.parseInt(request.getParameter("orderId"));
                deleteOrder(deleteOrderId);
                break;
        }

        response.sendRedirect(request.getContextPath() + "/manageOrders");
    }

    private void updateOrderStatus(int orderId, String status) {
        try {
            OrderDAO.updateOrderStatus(orderId, status);
        } catch (SQLException e) {
            System.out.println("Order Status Update Failed. SQL Error: " + e.getMessage());
        }
    }

    private void deleteOrder(int orderId) {
        try {
            Order order = OrderDAO.findOrderById(orderId);
            if (order != null) {
                OrderDAO.removeOrder(order);
            }
        } catch (SQLException e) {
            System.out.println("Order Delete Failed. SQL Error: " + e.getMessage());
        }
    }
}