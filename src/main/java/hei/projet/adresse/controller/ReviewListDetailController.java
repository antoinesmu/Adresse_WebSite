package hei.projet.adresse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.projet.adresse.entity.ReviewWithLogin;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.ReviewService;
import org.apache.logging.log4j.LogManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/detail/reviews")
public class ReviewListDetailController extends HttpServlet {
    private final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String establishmentId = req.getParameter("id");
        Integer restaurant_Id = Integer.parseInt(establishmentId);

        List<ReviewWithLogin> reviews = null;

        try {
            reviews = ReviewService.getInstance().listReviewByRestaurant(restaurant_Id);
        } catch (UserNotFoundException e) {
            LOG.info("User not found");
        }

        String reviewsJson = MAPPER.writeValueAsString(reviews);
        resp.getWriter().print(reviewsJson);
        resp.setContentType("application/json; utf-8");
    }
}