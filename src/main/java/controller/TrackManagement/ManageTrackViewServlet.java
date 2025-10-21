package controller.TrackManagement;

import dao.TrackDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Track;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

@WebServlet("/manage-tracks")
public class ManageTrackViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LinkedList<Track> allTracks = new LinkedList<>();

        try {
            allTracks = TrackDAO.getAllTracks();  // Delegate to Service
        } catch (SQLException e) {
            req.setAttribute("error", "Failed to fetch users: " + e.getMessage());
        }

        req.setAttribute("allTracks", allTracks);

        RequestDispatcher rd = req.getRequestDispatcher("/admin/manage-tracks.jsp");
        rd.forward(req, resp);
    }
}