package hei.projet.adresse.dao;

import hei.projet.adresse.dao.impl.DataSourceProvider;
import hei.projet.adresse.entity.Review;
import hei.projet.adresse.entity.ReviewWithLogin;
import hei.projet.adresse.exception.review.ReviewNotFoundException;
import org.apache.logging.log4j.LogManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    private final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();

    public ReviewDao() {
    }


    private Review createReviewFromResult(ResultSet resultSelect) throws SQLException {
        return new Review(
                resultSelect.getInt("id"),
                resultSelect.getInt("id_user"),
                resultSelect.getInt("id_restaurant"),
                resultSelect.getString("titre"),
                resultSelect.getString("champ"),
                resultSelect.getInt("note"));
    }

    public List<Review> listReview() {
        List<Review> result = new ArrayList<>();
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 Statement statement = cnx.createStatement();
                 ResultSet resultSelect = statement.executeQuery("SELECT id,id_user,id_restaurant,titre,champ,note FROM commentaire")) {
                while (resultSelect.next()) {
                    result.add(createReviewFromResult(resultSelect));
                }
            }
        } catch (SQLException e) {
            LOG.info("Connection to database failed");
        }
        return result;
    }


    public List<Review> listReviewByUserId(Integer id_user) {
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT id,id_user,id_restaurant,titre,champ,note FROM commentaire WHERE id_user = ?";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.setInt(1, id_user);
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while (result.next()) {
                        reviewList.add(createReviewFromResult(result));
                    }
                }
            }
        } catch (SQLException e) {
            LOG.info("Connection to database failed");
        }
        return reviewList;
    }

    public List<Review> listReviewByRestaurant(Integer id_restaurant) {
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT * FROM commentaire WHERE id_restaurant = ?";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.setInt(1, id_restaurant);
                ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    reviewList.add(createReviewFromResult(result));
                }
            }
        } catch (SQLException e) {
            LOG.info("Connection to database failed");
        }
        return reviewList;
    }

    public Review getReviewById(Integer id_review) throws ReviewNotFoundException {
        Review review = null;
        String sql = "SELECT id,id_user,id_restaurant,titre,champ,note FROM commentaire WHERE id = '" + id_review + "'";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        review = createReviewFromResult(result);
                    } else {
                        throw new ReviewNotFoundException("Review not found");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.info("Connection to database failed");
        }
        return review;
    }

    public void deleteReview(ReviewWithLogin review) {
        String sql = "DELETE FROM commentaire WHERE id = ?";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.setString(1, review.getId().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.info("Connection to database failed");
        }
    }

    public void addReview(Review review) {
        String sql = "INSERT INTO commentaire (id, id_user, id_restaurant, titre, champ, note) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, review.getId().toString());
                preparedStatement.setString(2, review.getId_user().toString());
                preparedStatement.setString(3, review.getId_restaurant().toString());
                preparedStatement.setString(4, review.getTitre());
                preparedStatement.setString(5, review.getChamp());
                preparedStatement.setString(6, review.getNote().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.info("Connection to database failed");
        }
    }
}
