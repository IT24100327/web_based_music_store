package controller;

import dao.AdvertisementDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Advertisement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

@WebServlet(name = "IndexServlet", value = {"", "/index"}, loadOnStartup = 1)
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LinkedList<Advertisement> activeAds = new LinkedList<>();
        try {
            activeAds = AdvertisementDAO.getActiveAdvertisements();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to fetch advertisements: " + e.getMessage());
        }
        request.setAttribute("activeAds", activeAds); // Use request scope
        RequestDispatcher rd = request.getRequestDispatcher("/trackPaginate");
        rd.forward(request, response);
    }
}