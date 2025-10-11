package controller.OrderManagement;

import service.OrderService;  // Use Service instead of DAO
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

    private final OrderService orderService = new OrderService();  // Delegate to Service

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        LinkedList<Order> allOrders = new LinkedList<>();
        try {
            allOrders = orderService.getOrders();  // Use Service
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }

        req.setAttribute("allOrders", allOrders);
        RequestDispatcher rd = req.getRequestDispatcher("admin/manage-orders.jsp");
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
            orderService.updateOrderStatus(orderId, status);  // Use Service
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Order Status Update Failed. Error: " + e.getMessage());
        }
    }

    private void deleteOrder(int orderId) {
        try {
            orderService.removeOrder(orderId);  // Use Service
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Order Delete Failed. Error: " + e.getMessage());
        }
    }
}