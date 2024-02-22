package hei.projet.adresse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.projet.adresse.entity.Review;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.ReviewService;
import hei.projet.adresse.service.UsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/detail/reviews/add")
public class AddReviewDetailController extends HttpServlet {

    private UsersService usersService = UsersService.getInstance();
    private final Logger LOG = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userLogin = (String) req.getSession().getAttribute("user");
        Integer userId = 0;

        try {
            userId = usersService.getUserByLogin(userLogin).getId();
        } catch (UserNotFoundException e) {
            LOG.info("User not found.");
        }

        Integer restaurantId = Integer.parseInt(req.getParameter("id"));
        Integer note = Integer.parseInt(req.getParameter("note"));
        String titre = req.getParameter("titre");
        String champ = req.getParameter("champ");

        Review review = new Review(0, userId, restaurantId, titre, champ, note);

        ReviewService.getInstance().addReview(review);

        ObjectMapper mapper = new ObjectMapper();
        String jsonReview = mapper.writeValueAsString(review);
        resp.getWriter().print(jsonReview);
        resp.setContentType("application/json; utf-8");
    }
}
