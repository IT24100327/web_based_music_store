package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CartDAO;
import dao.TrackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Track;
import model.User;
import utils.json.LocalDateAdapter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        try {
            String action = request.getParameter("action");
            String trackId = request.getParameter("trackId");

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("USER");

            // Get cart items - try database first, then session
            List<Track> cartItems = getCartItems(session, user);

            // Handle getState action
            if ("getState".equals(action)) {
                double cartTotal = calculateTotal(cartItems);
                int itemCount = cartItems.size();

                CartResponse cartResponse = new CartResponse(
                        cartItems != null ? cartItems : new ArrayList<>(),
                        cartTotal,
                        itemCount
                );

                out.print(gson.toJson(cartResponse));
                out.flush();
                return;
            }

            // Validate parameters for other actions
            if (trackId == null || trackId.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Track ID is required")));
                out.flush();
                return;
            }

            if (action == null || (!action.equals("add") && !action.equals("remove"))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Invalid or missing action")));
                out.flush();
                return;
            }

            // Parse trackId and fetch track
            Track track;
            try {
                track = TrackDAO.findTrackById(Integer.parseInt(trackId));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Invalid track ID format")));
                out.flush();
                return;
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(new ErrorResponse("Database error occurred")));
                out.flush();
                return;
            }

            if (track == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(new ErrorResponse("Track not found")));
                out.flush();
                return;
            }

            Double cartTotal = 0.0;

            // Perform cart action
            switch (action) {
                case "add":
                    addItemToCart(cartItems, track, user);
                    cartTotal = calculateTotal(cartItems);
                    System.out.println("Item Added to Cart: " + trackId);
                    break;
                case "remove":
                    removeFromCart(cartItems, track, user);
                    cartTotal = calculateTotal(cartItems);
                    System.out.println("Item Removed from Cart: " + trackId);
                    break;
            }

            // Update cart in session
            session.setAttribute("cartItems", cartItems);
            session.setAttribute("cartTotal", cartTotal);

            // Respond with JSON
            CartResponse cartResponse = new CartResponse(cartItems, cartTotal, cartItems.size());
            out.print(gson.toJson(cartResponse));
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse("Unexpected error: " + e.getMessage())));
            out.flush();
            e.printStackTrace();
        }
    }

    private List<Track> getCartItems(HttpSession session, User user) {
        List<Track> cartItems = (List<Track>) session.getAttribute("cartItems");

        // If user is logged in and session cart is empty, try to load from database
        if (user != null && (cartItems == null || cartItems.isEmpty())) {
            cartItems = CartDAO.getCartItems(user.getUserId());
            session.setAttribute("cartItems", cartItems);
        } else if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        return cartItems;
    }

    private void addItemToCart(List<Track> cartItems, Track track, User user) throws SQLException {
        // Check if track already exists in cart
        boolean exists = false;
        for (Track item : cartItems) {
            if (item.getTrackId() == track.getTrackId()) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            cartItems.add(track);

            if (user != null) {
                CartDAO.addToCart(user.getUserId(), track.getTrackId());
            }
        }
    }

    private void removeFromCart(List<Track> cartItems, Track track, User user) throws SQLException {
        // Find and remove the track by ID
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getTrackId() == track.getTrackId()) {
                cartItems.remove(i);

                // If user is logged in, remove from database
                if (user != null) {
                    CartDAO.removeFromCart(user.getUserId(), track.getTrackId());
                }
                break;
            }
        }
    }

    private double calculateTotal(List<Track> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (Track track : cartItems) {
                total += track.getPrice();
            }
        }
        return total;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    // Inner class for JSON response
    private static class CartResponse {
        private List<Track> cartItems;
        private double cartTotal;
        private int itemCount;

        public CartResponse(List<Track> cartItems, double cartTotal, int itemCount) {
            this.cartItems = cartItems;
            this.cartTotal = cartTotal;
            this.itemCount = itemCount;
        }

        // Add getters for JSON serialization
        public List<Track> getCartItems() {
            return cartItems;
        }

        public double getCartTotal() {
            return cartTotal;
        }

        public int getItemCount() {
            return itemCount;
        }
    }

    // Inner class for error response
    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}