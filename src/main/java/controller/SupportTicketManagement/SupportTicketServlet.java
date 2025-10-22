package controller.SupportTicketManagement;

import dao.SupportTicketDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.SupportTicket;
import model.SupportResponse;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "SupportTicketServlet", urlPatterns = {"/SupportTicketServlet", "/admin/manage-support-tickets"})
public class SupportTicketServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String action = request.getParameter("action");

        if ("/admin/manage-support-tickets".equals(path)) {
            action = "adminAllTickets";
        } else if (action == null) {
            action = "userTickets";
        }

        try {
            switch (action) {
                case "viewTicket":
                    viewTicketDetails(request, response);
                    break;
                case "showCreateForm":
                    showCreateTicketForm(request, response);
                    break;
                case "adminAllTickets":
                    adminViewAllTickets(request, response);
                    break;
                default:
                    viewUserTickets(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error occurred in SupportTicketServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            switch (action) {
                case "createTicket":
                    createTicket(request, response);
                    break;
                case "addResponse":
                    addResponse(request, response);
                    break;
                case "updateStatus":
                    updateTicketStatus(request, response);
                    break;
                case "assignTicket":
                    assignTicket(request, response);
                    break;
                case "updatePriority":
                    updateTicketPriority(request, response);
                    break;
                case "deleteTicket":
                    deleteTicket(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error occurred in SupportTicketServlet", e);
        }
    }

    private void viewUserTickets(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<SupportTicket> userTickets = SupportTicketDAO.getTicketsByUserId(currentUser.getUserId());
        request.setAttribute("userTickets", userTickets);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/support/userTickets.jsp");
        dispatcher.forward(request, response);
    }

    private void viewTicketDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String ticketIdParam = request.getParameter("ticketId");
        if (ticketIdParam == null) {
            // Redirect to the user's ticket list by default
            String redirectUrl = request.getContextPath() + (currentUser.isAdmin() ? "/admin/manage-support-tickets" : "/SupportTicketServlet?action=userTickets");
            response.sendRedirect(redirectUrl);
            return;
        }

        int ticketId = Integer.parseInt(ticketIdParam);
        SupportTicket ticket = SupportTicketDAO.findTicketById(ticketId);

        if (ticket == null || (ticket.getUserId() != currentUser.getUserId() && !currentUser.isAdmin())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to view this ticket.");
            return;
        }

        request.setAttribute("ticket", ticket);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/support/ticketDetails.jsp");
        dispatcher.forward(request, response);
    }

    private void showCreateTicketForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/support/createTicket.jsp");
        dispatcher.forward(request, response);
    }

    private void createTicket(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            String ticketType = request.getParameter("ticketType");
            String subject = request.getParameter("subject");
            String description = request.getParameter("description");
            String priority = request.getParameter("priority");
            String orderIdParam = request.getParameter("orderId");
            String trackIdParam = request.getParameter("trackId");
            if (ticketType == null || subject == null || description == null ||
                    ticketType.trim().isEmpty() || subject.trim().isEmpty() || description.trim().isEmpty()) {
                request.setAttribute("error", "All fields are required.");
                showCreateTicketForm(request, response);
                return;
            }

            SupportTicket ticket = new SupportTicket(currentUser.getUserId(), ticketType, subject, description);
            ticket.setPriority(priority != null && !priority.trim().isEmpty() ? priority : "medium");

            if (orderIdParam != null && !orderIdParam.trim().isEmpty()) {
                ticket.setOrderId(Integer.parseInt(orderIdParam));
            }
            if (trackIdParam != null && !trackIdParam.trim().isEmpty()) {
                ticket.setTrackId(Integer.parseInt(trackIdParam));
            }

            ticket.setCreatedDate(LocalDateTime.now());
            ticket.setLastUpdated(LocalDateTime.now());

            boolean success = SupportTicketDAO.createTicket(ticket);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/SupportTicketServlet?action=userTickets&success=true");
            } else {
                request.setAttribute("error", "Failed to create support ticket. Please try again.");
                showCreateTicketForm(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred while creating your ticket.");
            e.printStackTrace();
            showCreateTicketForm(request, response);
        }
    }

    private void addResponse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        String responseText = request.getParameter("responseText");
        SupportTicket ticket = SupportTicketDAO.findTicketById(ticketId);
        if (ticket == null || (ticket.getUserId() != currentUser.getUserId() && !currentUser.isAdmin())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (responseText != null && !responseText.trim().isEmpty()) {
            SupportResponse supportResponse = new SupportResponse(ticketId, currentUser.getUserId(), responseText, currentUser.isAdmin());
            SupportTicketDAO.addResponseToTicket(supportResponse);
        }

        response.sendRedirect(request.getContextPath() + "/SupportTicketServlet?action=viewTicket&ticketId=" + ticketId);
    }

    private void adminViewAllTickets(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }

        List<SupportTicket> allTickets = SupportTicketDAO.getAllTickets();
        int openTickets = SupportTicketDAO.getTicketCountByStatus("open");
        int inProgressTickets = SupportTicketDAO.getTicketCountByStatus("in_progress");
        int resolvedTickets = SupportTicketDAO.getTicketCountByStatus("resolved");
        int closedTickets = SupportTicketDAO.getTicketCountByStatus("closed");

        request.setAttribute("allTickets", allTickets);
        request.setAttribute("openTickets", openTickets);
        request.setAttribute("inProgressTickets", inProgressTickets);
        request.setAttribute("resolvedTickets", resolvedTickets);
        request.setAttribute("closedTickets", closedTickets);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/manage-support-tickets.jsp");
        dispatcher.forward(request, response);
    }

    private void updateTicketStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        String status = request.getParameter("status");
        SupportTicketDAO.updateTicketStatus(ticketId, status);
        response.sendRedirect(request.getContextPath() + "/SupportTicketServlet?action=viewTicket&ticketId=" + ticketId);
    }

    private void assignTicket(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        int adminId = Integer.parseInt(request.getParameter("adminId"));
        SupportTicketDAO.assignTicketToAdmin(ticketId, adminId);
        response.sendRedirect(request.getContextPath() + "/SupportTicketServlet?action=viewTicket&ticketId=" + ticketId);
    }

    private void updateTicketPriority(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        String priority = request.getParameter("priority");
        SupportTicketDAO.updateTicketPriority(ticketId, priority);
        response.sendRedirect(request.getContextPath() + "/SupportTicketServlet?action=viewTicket&ticketId=" + ticketId);
    }

    private void deleteTicket(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        SupportTicketDAO.deleteTicket(ticketId);

        response.sendRedirect(request.getContextPath() + "/admin/manage-support-tickets?success=Ticket+deleted+successfully");
    }
}