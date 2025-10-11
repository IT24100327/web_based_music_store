package controller.MarketingManagement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Advertisement;
import model.Promotion;
import service.AdvertisementService;
import service.PromotionService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

@WebServlet("/manage-marketing")
public class ManageMarketingViewServlet extends HttpServlet {

    private final AdvertisementService adService = new AdvertisementService();
    private final PromotionService promoService = new PromotionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LinkedList<Promotion> allPromotions = new LinkedList<>();
        LinkedList<Advertisement> allAdvertisements = new LinkedList<>();

        try {
            allPromotions = promoService.getPromotions();
            allAdvertisements = adService.getAdvertisements();
        } catch (SQLException e) {
            req.setAttribute("error", "Failed to fetch marketing data: " + e.getMessage());
        }

        // Prepare view data
        req.setAttribute("allPromotions", allPromotions);
        req.setAttribute("allAdvertisements", allAdvertisements);
        req.setAttribute("currentDate", LocalDate.now().toString());  // For date comparisons in JSP

        RequestDispatcher rd = req.getRequestDispatcher("/admin/manage-marketing.jsp");
        rd.forward(req, resp);
    }
}