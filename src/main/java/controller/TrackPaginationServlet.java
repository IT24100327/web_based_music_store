package controller;

import dao.TrackDAO;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Track> tracks = new ArrayList<>();
        int noOfRecords = 0;
        int noOfPages = 0;

        try {
            // UPDATED: Call the new methods that filter by APPROVED status
            tracks = TrackDAO.getApprovedTracksPaginated(page, RECORDS_PER_PAGE);
            noOfRecords = TrackDAO.countApprovedTracks();

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