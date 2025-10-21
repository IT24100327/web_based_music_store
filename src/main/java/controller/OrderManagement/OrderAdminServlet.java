package controller.OrderManagement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import model.enums.OrderStatus;
import service.OrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/manageOrders")
public class OrderAdminServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();  // Delegate to Service

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Order> allOrders = orderService.getOrders(); // Use List instead of LinkedList
            String searchQuery = req.getParameter("searchQuery");

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                String query = searchQuery.toLowerCase().trim();
                allOrders = allOrders.stream()
                        .filter(order -> String.valueOf(order.getOrderId()).contains(query) ||
                                String.valueOf(order.getUserId()).contains(query) ||
                                order.getStatus().name().toLowerCase().contains(query))
                        .collect(Collectors.toList());
            }

            req.setAttribute("allOrders", allOrders);
            RequestDispatcher rd = req.getRequestDispatcher("/admin/manage-orders.jsp");
            rd.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "update_status":
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                OrderStatus status = OrderStatus.valueOf(request.getParameter("status"));
                updateOrderStatus(orderId, status);
                break;
            case "delete":
                int deleteOrderId = Integer.parseInt(request.getParameter("orderId"));
                deleteOrder(deleteOrderId);
                break;
        }

        response.sendRedirect(request.getContextPath() + "/manageOrders");
    }

    private void updateOrderStatus(int orderId, OrderStatus status) {
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