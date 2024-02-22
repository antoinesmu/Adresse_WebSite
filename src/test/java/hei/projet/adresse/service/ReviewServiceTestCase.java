package hei.projet.adresse.service;

import hei.projet.adresse.dao.ReviewDao;
import hei.projet.adresse.entity.Review;
import hei.projet.adresse.entity.ReviewWithLogin;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.review.ReviewNotFoundException;
import hei.projet.adresse.exception.users.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTestCase {

    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewDao reviewDao;
    @Mock
    private UsersService usersService;
    @Mock
    private ReviewService.ReviewServiceHolder reviewServiceHolder;


    @Test
    public void shouldListReviewByRestaurant() throws UserNotFoundException {
        //Given
        Integer restaurant_id = 1;

        List<Review> reviewListDao = new ArrayList<Review>();
        List<ReviewWithLogin> reviewList = new ArrayList<ReviewWithLogin>();
        ReviewWithLogin review1 = new ReviewWithLogin(1,1,1,"loginTest1","titreTest1","champTest1",5);
        ReviewWithLogin review2 = new ReviewWithLogin(2,2,1,"loginTest2","titreTest2","champTest2",4);
        Review reviewDao1 = new Review(1,1,1,"titreTest1","champTest1",5);
        Review reviewDao2 = new Review(2,2,1,"titreTest2","champTest2",4);
        Users user1 = new Users(1, "loginTest1","pwd", "firstName", "lastName", "test.case@adresse.fr", false);
        Users user2 = new Users(2, "loginTest2","pwd", "firstName", "lastName", "test.case@adresse.fr", false);

        reviewList.add(review1);
        reviewList.add(review2);
        reviewListDao.add(reviewDao1);
        reviewListDao.add(reviewDao2);

        when(reviewDao.listReviewByRestaurant(restaurant_id)).thenReturn(reviewListDao);
        when(usersService.getUserById(1)).thenReturn(user1);
        when(usersService.getUserById(2)).thenReturn(user2);

        //When
        List<ReviewWithLogin> result = reviewService.listReviewByRestaurant(restaurant_id);
        ReviewWithLogin result1 = result.get(0);
        ReviewWithLogin result2 = result.get(1);

        //Then
        verify(usersService, times(1)).getUserById(1);
        verify(usersService, times(1)).getUserById(2);
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(result1.getId()).isEqualTo(review1.getId());
        Assertions.assertThat(result1.getId_user()).isEqualTo(review1.getId_user());
        Assertions.assertThat(result1.getId_restaurant()).isEqualTo(review1.getId_restaurant());
        Assertions.assertThat(result1.getUserLogin()).isEqualTo(review1.getUserLogin());
        Assertions.assertThat(result1.getTitre()).isEqualTo(review1.getTitre());
        Assertions.assertThat(result1.getChamp()).isEqualTo(review1.getChamp());
        Assertions.assertThat(result1.getNote()).isEqualTo(review1.getNote());

        Assertions.assertThat(result2.getId()).isEqualTo(review2.getId());
        Assertions.assertThat(result2.getId_user()).isEqualTo(review2.getId_user());
        Assertions.assertThat(result2.getId_restaurant()).isEqualTo(review2.getId_restaurant());
        Assertions.assertThat(result2.getUserLogin()).isEqualTo(review2.getUserLogin());
        Assertions.assertThat(result2.getTitre()).isEqualTo(review2.getTitre());
        Assertions.assertThat(result2.getChamp()).isEqualTo(review2.getChamp());
        Assertions.assertThat(result2.getNote()).isEqualTo(review2.getNote());
    }

    @Test (expected = UserNotFoundException.class)
    public void shouldListReviewByRestaurantAndThrowUserNotFoundException() throws UserNotFoundException {
        //GIVEN
        Integer restaurant_id = 1;
        List<Review> reviewList = new ArrayList<>();
        Review reviewDao1 = new Review(1,1,1,"titreTest1","champTest1",5);
        reviewList.add(reviewDao1);

        when(reviewDao.listReviewByRestaurant(restaurant_id)).thenReturn(reviewList);
        when(usersService.getUserById(reviewDao1.getId_user())).thenThrow(UserNotFoundException.class);

        //WHEN
        reviewService.listReviewByRestaurant(restaurant_id);

        //THEN
        fail("Should thorw an UserNotFoundException");
    }

    @Test
    public void shouldListReviewByUserId() throws UserNotFoundException {
        //Given
        Integer user_id = 1;

        List<Review> reviewListDao = new ArrayList<Review>();
        List<ReviewWithLogin> reviewList = new ArrayList<ReviewWithLogin>();
        ReviewWithLogin review1 = new ReviewWithLogin(1,1,1,"loginTest1","titreTest1","champTest1",5);
        ReviewWithLogin review2 = new ReviewWithLogin(2,1,2,"loginTest1","titreTest2","champTest2",4);
        Review reviewDao1 = new Review(1,1,1,"titreTest1","champTest1",5);
        Review reviewDao2 = new Review(2,1,2,"titreTest2","champTest2",4);
        Users user1 = new Users(1, "loginTest1","pwd", "firstName", "lastName", "test.case@adresse.fr", false);

        reviewList.add(review1);
        reviewList.add(review2);
        reviewListDao.add(reviewDao1);
        reviewListDao.add(reviewDao2);

        when(reviewDao.listReviewByUserId(user_id)).thenReturn(reviewListDao);
        when(usersService.getUserById(1)).thenReturn(user1);

        //When
        List<ReviewWithLogin> result = reviewService.listReviewByUserId(user_id);
        ReviewWithLogin result1 = result.get(0);
        ReviewWithLogin result2 = result.get(1);

        //Then
        verify(usersService, times(2)).getUserById(1);
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(result1.getId()).isEqualTo(review1.getId());
        Assertions.assertThat(result1.getId_user()).isEqualTo(review1.getId_user());
        Assertions.assertThat(result1.getId_restaurant()).isEqualTo(review1.getId_restaurant());
        Assertions.assertThat(result1.getUserLogin()).isEqualTo(review1.getUserLogin());
        Assertions.assertThat(result1.getTitre()).isEqualTo(review1.getTitre());
        Assertions.assertThat(result1.getChamp()).isEqualTo(review1.getChamp());
        Assertions.assertThat(result1.getNote()).isEqualTo(review1.getNote());

        Assertions.assertThat(result2.getId()).isEqualTo(review2.getId());
        Assertions.assertThat(result2.getId_user()).isEqualTo(review2.getId_user());
        Assertions.assertThat(result2.getId_restaurant()).isEqualTo(review2.getId_restaurant());
        Assertions.assertThat(result2.getUserLogin()).isEqualTo(review2.getUserLogin());
        Assertions.assertThat(result2.getTitre()).isEqualTo(review2.getTitre());
        Assertions.assertThat(result2.getChamp()).isEqualTo(review2.getChamp());
        Assertions.assertThat(result2.getNote()).isEqualTo(review2.getNote());
    }

    @Test (expected = UserNotFoundException.class)
    public void shouldListReviewByUserIdAndThrowUserNotFoundException() throws UserNotFoundException {
        //GIVEN
        Integer user_id = 1;
        List<Review> reviewList = new ArrayList<>();
        Review reviewDao1 = new Review(1,1,1,"titreTest1","champTest1",5);
        reviewList.add(reviewDao1);

        when(reviewDao.listReviewByUserId(user_id)).thenReturn(reviewList);
        when(usersService.getUserById(reviewDao1.getId_user())).thenThrow(UserNotFoundException.class);

        //WHEN
        reviewService.listReviewByUserId(user_id);

        //THEN
        fail("Should thorw an UserNotFoundException");
    }

    @Test
    public void shouldGetReviewById() throws UserNotFoundException, ReviewNotFoundException {
        //Given
        Integer reviewId = 1;
        Integer userId = 1;
        ReviewWithLogin review = new ReviewWithLogin(1,1,1,"loginTest1","titreTest1","champTest6",5);
        Review reviewDao = new Review(1,1,1,"titreTest1","champTest6",5);
        Users user = new Users(1, "loginTest1","pwd", "firstName", "lastName", "test.case@adresse.fr", false);

        Mockito.doReturn(reviewDao).when(this.reviewDao).getReviewById(reviewId);
        Mockito.doReturn(user).when(usersService).getUserById(userId);

        //When
        ReviewWithLogin result = reviewService.getReviewById(reviewId);

        //Then
        Assertions.assertThat(result.getId()).isEqualTo(review.getId());
        Assertions.assertThat(result.getId_user()).isEqualTo(review.getId_user());
        Assertions.assertThat(result.getId_restaurant()).isEqualTo(review.getId_restaurant());
        Assertions.assertThat(result.getUserLogin()).isEqualTo(review.getUserLogin());
        Assertions.assertThat(result.getTitre()).isEqualTo(review.getTitre());
        Assertions.assertThat(result.getChamp()).isEqualTo(review.getChamp());
        Assertions.assertThat(result.getNote()).isEqualTo(review.getNote());
    }

    @Test (expected = UserNotFoundException.class)
    public void shouldGetReviewByUserIdAndThrowUserNotFoundException() throws UserNotFoundException, ReviewNotFoundException {
        //GIVEN
        Integer review_id = 1;
        Review reviewDao1 = new Review(1,1,1,"titreTest1","champTest1",5);

        when(reviewDao.getReviewById(review_id)).thenReturn(reviewDao1);
        when(usersService.getUserById(reviewDao1.getId_user())).thenThrow(UserNotFoundException.class);

        //WHEN
        reviewService.getReviewById(review_id);

        //THEN
        fail("Should thorw an UserNotFoundException");
    }

    @Test
    public void shouldAddReview(){
        //GIVEN
        Review review = new Review(1,1,1,"titreTest1","champTest6",5);

        //WHEN
        reviewService.addReview(review);

        //THEN
        verify(reviewDao, times(1)).addReview(review);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIdUser(){
        //GIVEN
        Review review = new Review(null,null,null,null,null,null);

        //WHEN
        reviewService.addReview(review);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIdRestaurant(){
        //GIVEN
        Review review = new Review(1,1,null,null,null,null);

        //WHEN
        reviewService.addReview(review);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionTitre(){
        //GIVEN
        Review review = new Review(1,1,1,null,null,null);

        //WHEN
        reviewService.addReview(review);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionChamp(){
        //GIVEN
        Review review = new Review(1,1,1,"null",null,null);

        //WHEN
        reviewService.addReview(review);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionNote(){
        //GIVEN
        Review review = new Review(1,1,1,"null","null",null);

        //WHEN
        reviewService.addReview(review);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test
    public void shouldDeleteReview(){
        //GIVEN
        ReviewWithLogin reviewBis = new ReviewWithLogin(1,1,1,"loginTest1","titreTest1","champTest6",5);

        //WHEN
        reviewService.deleteReview(reviewBis);

        //THEN
        verify(reviewDao, times(1)).deleteReview(reviewBis);
    }
}
