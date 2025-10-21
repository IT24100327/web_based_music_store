package controller;

import dao.constants.TrackSQLConstants;
import factory.TrackFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Track;
import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/trackPaginate")
public class TrackPaginationServlet extends HttpServlet {
    private final int RECORDS_PER_PAGE = 12;

    // src/main/java/controller/TrackPaginationServlet.java

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

            String sql = "SELECT t.*, u.firstName, u.lastName, ad.stage_name " +
                    "FROM tracks t " +
                    "JOIN users u ON t.artist_id = u.userId " +
                    "LEFT JOIN artist_details ad ON t.artist_id = ad.user_id " +
                    "ORDER BY t.trackId LIMIT ? OFFSET ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, RECORDS_PER_PAGE);  // LIMIT: number of records per page
            ps.setInt(2, (page - 1) * RECORDS_PER_PAGE);  // OFFSET: starting position

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Track track = TrackFactory.createTrackFromResultSet(rs);
                    tracks.add(track);
                }
            }

            // Get total rows using constant (this part remains the same)
            try (PreparedStatement countPs = conn.prepareStatement(TrackSQLConstants.COUNT_ALL_TRACKS);
                 ResultSet countRs = countPs.executeQuery()) {
                if (countRs.next()) {
                    noOfRecords = countRs.getInt(1);
                }
            }
            noOfPages = (int) Math.ceil(noOfRecords * 1.0 / RECORDS_PER_PAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("trackList", tracks);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);

        String ajax = request.getParameter("ajax");
        if ("true".equals(ajax)) {
            RequestDispatcher rd = request.getRequestDispatcher("/includes/track-cards.jsp");
            rd.forward(request, response);
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
}