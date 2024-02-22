package hei.projet.adresse.controller;

import hei.projet.adresse.entity.ReviewWithLogin;
import hei.projet.adresse.exception.review.ReviewNotFoundException;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.ReviewService;
import org.apache.logging.log4j.LogManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/detail/review/delete")
public class DeleteReviewDetailController extends HttpServlet {
    private final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();
    private final ReviewService reviewService = ReviewService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer reviewId = Integer.parseInt(req.getParameter("id"));

        ReviewWithLogin review = null;
        try {
            review = reviewService.getReviewById(reviewId);
        } catch (ReviewNotFoundException | UserNotFoundException e) {
            LOG.info("User or review not found");
        }

        reviewService.deleteReview(review);
    }
}
