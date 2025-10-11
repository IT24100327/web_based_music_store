package controller;

import dao.constants.TrackSQLConstants;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Track;
import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/trackPaginate")
public class TrackPaginationServlet extends HttpServlet {
    private final int RECORDS_PER_PAGE = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Track> tracks = new ArrayList<>();
        int noOfRecords = 0;
        int noOfPages = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Fetch limited songs - use full query constant and correct param order for MySQL (LIMIT first, then OFFSET)
            PreparedStatement ps = conn.prepareStatement(TrackSQLConstants.SELECT_TRACKS_PAGINATED);
            ps.setInt(1, RECORDS_PER_PAGE);  // LIMIT: number of records per page
            ps.setInt(2, (page - 1) * RECORDS_PER_PAGE);  // OFFSET: starting position

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Track track = new Track(
                            rs.getString("title"),
                            rs.getString("artist")
                    );
                    track.setTrackId(rs.getInt("trackId"));
                    track.setPrice(rs.getDouble("price"));
                    tracks.add(track);
                }
            }

            // Get total rows using constant
            try (PreparedStatement countPs = conn.prepareStatement(TrackSQLConstants.COUNT_ALL_TRACKS);
                 ResultSet countRs = countPs.executeQuery()) {
                if (countRs.next()) {
                    noOfRecords = countRs.getInt(1);
                }
            }
            noOfPages = (int) Math.ceil(noOfRecords * 1.0 / RECORDS_PER_PAGE);

        } catch (Exception e) {
            e.printStackTrace();  // TODO: Improve error handling (e.g., log and redirect to error page)
        }

        request.setAttribute("trackList", tracks);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);

        // Check for AJAX request
        String ajax = request.getParameter("ajax");
        if ("true".equals(ajax)) {
            // Forward to fragment for partial render
            RequestDispatcher rd = request.getRequestDispatcher("/includes/track-cards.jsp");
            rd.forward(request, response);
        } else {
            // Full page forward (initial load)
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
}