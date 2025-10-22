package controller.FeedbackManagement;

import dao.FeedbackDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Feedback;
import model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FeedbackServlet", urlPatterns = {"/FeedbackServlet", "/admin/manage-feedback"})
public class FeedbackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String action = request.getParameter("action");

        if ("/admin/manage-feedback".equals(path)) {
            action = "adminAllFeedback";
        } else if (action == null) {
            action = "showFeedbackForm";
        }

        try {
            switch (action) {
                case "showFeedbackForm":
                    showFeedbackForm(request, response);
                    break;
                case "userFeedback":
                    viewUserFeedback(request, response);
                    break;
                case "adminAllFeedback":
                    adminViewAllFeedback(request, response);
                    break;
                case "adminFeedbackDetails":
                    adminViewFeedbackDetails(request, response);
                    break;
                default:
                    showFeedbackForm(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error occurred", e);
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
                case "submitFeedback":
                    submitFeedback(request, response);
                    break;
                case "addAdminNotes":
                    addAdminNotes(request, response);
                    break;
                case "deleteFeedback": // Moved from doGet to handle POST request
                    deleteFeedback(request, response);
                    break;
                case "updateFeedback":
                    updateUserFeedback(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error occurred", e);
        }
    }

    private void showFeedbackForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/feedback/feedbackForm.jsp");
        dispatcher.forward(request, response);
    }

    private void submitFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try {
            String feedbackType = request.getParameter("feedbackType");
            String subject = request.getParameter("subject");
            String message = request.getParameter("message");
            String ratingParam = request.getParameter("rating");
            String email = request.getParameter("email");

            String orderIdParam = request.getParameter("orderId");
            String trackIdParam = request.getParameter("trackId");

            if (feedbackType == null || subject == null || message == null ||
                    feedbackType.trim().isEmpty() || subject.trim().isEmpty() || message.trim().isEmpty()) {
                request.setAttribute("error", "Feedback type, subject, and message are required fields.");
                showFeedbackForm(request, response);
                return;
            }

            int rating = 0;
            if (ratingParam != null && !ratingParam.trim().isEmpty()) {
                rating = Integer.parseInt(ratingParam);
            }

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("USER");

            Feedback feedback;

            if (currentUser != null) {
                feedback = new Feedback(currentUser.getUserId(), feedbackType, subject, message, rating);
            } else {
                if (email == null || email.trim().isEmpty()) {
                    request.setAttribute("error", "Email is required for anonymous feedback.");
                    showFeedbackForm(request, response);
                    return;
                }
                feedback = new Feedback(email, feedbackType, subject, message, rating);
            }

            if (trackIdParam != null && !trackIdParam.trim().isEmpty()) {
                feedback.setTrackId(Integer.parseInt(trackIdParam));
            }
            if (orderIdParam != null && !orderIdParam.trim().isEmpty()) {
                feedback.setOrderId(Integer.parseInt(orderIdParam));
            }

            feedback.setSubmittedDate(LocalDateTime.now());
            feedback.setRead(false);

            boolean success = FeedbackDAO.submitFeedback(feedback);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/FeedbackServlet?action=showFeedbackForm&success=true");
            } else {
                request.setAttribute("error", "Failed to submit feedback. Please try again.");
                showFeedbackForm(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred while submitting your feedback.");
            e.printStackTrace();
            showFeedbackForm(request, response);
        }
    }

    private void viewUserFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Feedback> userFeedback = FeedbackDAO.getFeedbackByUserId(currentUser.getUserId());
        request.setAttribute("userFeedback", userFeedback);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/feedback/userFeedback.jsp");
        dispatcher.forward(request, response);
    }

    private void adminViewAllFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }

        List<Feedback> allFeedback = FeedbackDAO.getAllFeedback();
        int totalFeedback = allFeedback.size();
        int unreadCount = FeedbackDAO.getUnreadFeedbackCount();
        double averageRating = FeedbackDAO.getAveragePlatformRating();

        request.setAttribute("allFeedback", allFeedback);
        request.setAttribute("totalFeedback", totalFeedback);
        request.setAttribute("unreadCount", unreadCount);
        request.setAttribute("averageRating", averageRating);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/manage-feedback.jsp");
        dispatcher.forward(request, response);
    }

    private void adminViewFeedbackDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String feedbackIdParam = request.getParameter("feedbackId");
        if (feedbackIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/FeedbackServlet?action=adminAllFeedback");
            return;
        }

        try {
            int feedbackId = Integer.parseInt(feedbackIdParam);
            Feedback feedback = FeedbackDAO.findFeedbackById(feedbackId);

            if (feedback == null) {
                response.sendRedirect(request.getContextPath() + "/FeedbackServlet?action=adminAllFeedback&error=notfound");
                return;
            }

            if (!feedback.isRead()) {
                FeedbackDAO.markFeedbackAsRead(feedbackId);
                feedback.setRead(true);
            }

            request.setAttribute("feedback", feedback);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/feedbackDetails.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/FeedbackServlet?action=adminAllFeedback&error=invalidid");
        }
    }

    private void addAdminNotes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");
        String feedbackId = request.getParameter("feedbackId");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            int fbId = Integer.parseInt(feedbackId);
            String adminNotes = request.getParameter("adminNotes");

            if (adminNotes != null && !adminNotes.trim().isEmpty()) {
                FeedbackDAO.addAdminNotes(fbId, adminNotes);
            }
        } catch (NumberFormatException e) {
            // Handle error
        }
        response.sendRedirect(request.getContextPath() + "/FeedbackServlet?action=adminFeedbackDetails&feedbackId=" + feedbackId);
    }

    private void deleteFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String feedbackIdParam = request.getParameter("feedbackId");
        if (feedbackIdParam != null) {
            try {
                int feedbackId = Integer.parseInt(feedbackIdParam);
                FeedbackDAO.deleteFeedback(feedbackId);
            } catch (NumberFormatException e) {
                // handle error
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/manage-feedback?success=Feedback+deleted");
    }

    private void updateUserFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Implementation for users updating their own feedback would go here...
    }
}