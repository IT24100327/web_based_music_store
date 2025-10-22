package controller.TrackManagement;

import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.enums.TrackStatus;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/update-track-status")
public class UpdateTrackStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to perform this action.");
            return;
        }

        String trackIdStr = request.getParameter("trackId");
        String action = request.getParameter("action");
        String redirectUrl = request.getContextPath() + "/manage-tracks";

        if (trackIdStr == null || action == null) {
            response.sendRedirect(redirectUrl + "?error=Missing parameters.");
            return;
        }

        try {
            int trackId = Integer.parseInt(trackIdStr);
            TrackStatus newStatus;

            if ("approve".equals(action)) {
                newStatus = TrackStatus.APPROVED;
            } else if ("reject".equals(action)) {
                newStatus = TrackStatus.REJECTED;
            } else {
                response.sendRedirect(redirectUrl + "?error=Invalid action.");
                return;
            }

            TrackDAO.updateTrackStatus(trackId, newStatus);
            response.sendRedirect(redirectUrl + "?success=Track status updated successfully.");

        } catch (NumberFormatException e) {
            response.sendRedirect(redirectUrl + "?error=Invalid Track ID.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(redirectUrl + "?error=Database error: " + e.getMessage());
        }
    }
}