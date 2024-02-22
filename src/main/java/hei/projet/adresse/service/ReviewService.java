package hei.projet.adresse.service;

import hei.projet.adresse.dao.ReviewDao;
import hei.projet.adresse.entity.Review;
import hei.projet.adresse.entity.ReviewWithLogin;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.review.ReviewNotFoundException;
import hei.projet.adresse.exception.users.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ReviewService {

    protected static class ReviewServiceHolder {
        protected static ReviewService instance = new ReviewService();
    }

    private final Logger LOG = LogManager.getLogger();

    private ReviewDao reviewDao;

    private UsersService usersService = new UsersService();


    public ReviewService() {
        this.reviewDao = new ReviewDao();
    }

    public static ReviewService getInstance() {
        return ReviewServiceHolder.instance;
    }

    public List<ReviewWithLogin> listReviewByUserId(Integer id_user) throws UserNotFoundException {
        LOG.info("Getting reviews from user : {}", id_user);
        List<Review> listReviewWithoutLogin = reviewDao.listReviewByUserId(id_user);
        List<ReviewWithLogin> listReview = new ArrayList<>();

        for (Review review : listReviewWithoutLogin) {
            Users user = usersService.getUserById(review.getId_user());
            listReview.add(new ReviewWithLogin(review, user.getLogin()));
        }
        return listReview;
    }

    public List<ReviewWithLogin> listReviewByRestaurant(Integer id_restaurant) throws UserNotFoundException {
        LOG.info("Getting reviews of restaurant : {}", id_restaurant);
        List<Review> listReviewWithoutLogin = reviewDao.listReviewByRestaurant(id_restaurant);
        List<ReviewWithLogin> listReview = new ArrayList<>();

        for (Review review : listReviewWithoutLogin) {
            Users user = usersService.getUserById(review.getId_user());
            listReview.add(new ReviewWithLogin(review, user.getLogin()));
        }
        return listReview;
    }

    public ReviewWithLogin getReviewById(Integer id_review) throws UserNotFoundException, ReviewNotFoundException {
        LOG.info("Getting review with id : {}", id_review);
        Review review = reviewDao.getReviewById(id_review);
        Integer userId = review.getId_user();
        Users user = usersService.getUserById(userId);
        return new ReviewWithLogin(review, user.getLogin());
    }

    public void addReview(Review review) {
        if (review.getId_user() == null || review.getId_user() < 0) {
            throw new IllegalArgumentException("A review needs a valid id_user.");
        }
        if (review.getId_restaurant() == null || review.getId_restaurant() < 0) {
            throw new IllegalArgumentException("A review needs a valid id_restaurant.");
        }
        if (review.getTitre() == null || review.getTitre().equals("")) {
            throw new IllegalArgumentException("A review needs a title.");
        }
        if (review.getChamp() == null || review.getChamp().equals("")) {
            throw new IllegalArgumentException("A review needs a comment.");
        }
        if (review.getNote() == null || 0 > review.getNote() || 5 < review.getNote()) {
            throw new IllegalArgumentException("A review needs a note between 0 and 5.");
        }
        LOG.info("Adding new review with attributes:");
        LOG.info("  -id_user: {}", review.getId_user());
        LOG.info("  -id_restaurant: {}", review.getId_restaurant());
        LOG.info("  -titre: {}", review.getTitre());
        LOG.info("  -champ: {}", review.getChamp());
        LOG.info("  -note: {}", review.getNote());

        reviewDao.addReview(review);
    }

    public void deleteReview(ReviewWithLogin review) {
        LOG.info("Deleting review id : {}", review.getId());
        reviewDao.deleteReview(review);
    }
}
