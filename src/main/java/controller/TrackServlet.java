package controller;

import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Track;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

@WebServlet(name = "TrackServlet", value = "/TrackServlet")
public class TrackServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LinkedList<Track> allTracks = new LinkedList<>();
        try {
            allTracks = TrackDAO.getAllTracks();
            request.setAttribute("allTracks", allTracks);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.getRequestDispatcher("TrackList.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}