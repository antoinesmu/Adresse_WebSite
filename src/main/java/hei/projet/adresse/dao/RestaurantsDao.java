package hei.projet.adresse.dao;

import hei.projet.adresse.entity.Restaurant;

import java.util.List;

public interface RestaurantsDao {
    List<Restaurant> listRestaurants();

    Restaurant getRestaurantById(Integer id);

    Restaurant addRestaurant(Restaurant restaurant);

    void editRestaurant(Restaurant restaurant, String name, String specialty, String address, Integer price, String moreInfo, String phone, String website);

    List<Restaurant> searchResult(String keyword);

    List<String> listParams();

    List<Restaurant> searchRequest(String keyword, String param);

    Restaurant getRandomRestaurant();

    void deleteRestaurant(Restaurant restaurant);
}
