package controller.PostManagement;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Post;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

@WebServlet("/post-image")
public class PostImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postIdStr = request.getParameter("postId");
        String indexStr = request.getParameter("index");

        if (postIdStr == null || indexStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters.");
            return;
        }

        try {
            int postId = Integer.parseInt(postIdStr);
            int index = Integer.parseInt(indexStr);

            Post post = PostDAO.getPostById(postId);

            byte[] imageData = null;
            String imageType = null;

            switch (index) {
                case 1:
                    imageData = post.getImage1Data();
                    imageType = post.getImage1Type();
                    break;
                case 2:
                    imageData = post.getImage2Data();
                    imageType = post.getImage2Type();
                    break;
                case 3:
                    imageData = post.getImage3Data();
                    imageType = post.getImage3Type();
                    break;
            }

            if (post == null || imageData == null || imageType == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found.");
                return;
            }

            response.setContentType(imageType);
            response.setContentLength(imageData.length);

            try (OutputStream out = response.getOutputStream()) {
                out.write(imageData);
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter format.");
        } catch (SQLException e) {
            throw new ServletException("Database error retrieving post image.", e);
        }
    }
}