package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Payment;
import service.PaymentService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/manage-payments")
public class ManagePaymentsServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Payment> allPayments = paymentService.getAllPayments();
            String searchQuery = req.getParameter("searchQuery");

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                String query = searchQuery.toLowerCase().trim();
                allPayments = allPayments.stream()
                        .filter(p -> String.valueOf(p.getPaymentId()).contains(query) ||
                                String.valueOf(p.getOrderId()).contains(query) ||
                                (p.getTransactionId() != null && p.getTransactionId().toLowerCase().contains(query)))
                        .collect(Collectors.toList());
            }

            req.setAttribute("allPayments", allPayments);
            req.getRequestDispatcher("/admin/manage-payments.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Could not retrieve payments", e);
        }
    }
}