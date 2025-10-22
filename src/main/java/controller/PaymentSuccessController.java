package controller;

import dao.OrderDAO;
import model.Order;
import model.Payment;
import model.User;
import model.Track;
import service.PaymentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/checkout")
public class PaymentSuccessController extends HttpServlet {
    
    private PaymentService paymentService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        paymentService = new PaymentService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("USER");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Only allow regular users to access payment success, not admins
        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdParam);
            
            // Get order details
            Connection con = null;
            try {
                con = utils.DatabaseConnection.getConnection();
                List<Order> userOrders = OrderDAO.getOrdersByUserId(user.getUserId());
                Order order = null;
                
                // Find the specific order
                for (Order o : userOrders) {
                    if (o.getOrderId() == orderId) {
                        order = o;
                        break;
                    }
                }
                
                if (order == null) {
                    response.sendRedirect(request.getContextPath() + "/");
                    return;
                }
                
                // Get payment details
                Payment payment = paymentService.getPaymentByOrderId(orderId);
                
                // Get order items (tracks)
                List<Track> orderItems = OrderDAO.getTracksByOrderId(orderId);
                
                request.setAttribute("order", order);
                request.setAttribute("payment", payment);
                request.setAttribute("orderItems", orderItems);
                
                request.getRequestDispatcher("/checkout.jsp").forward(request, response);
                
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("error", "Error loading payment details");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
