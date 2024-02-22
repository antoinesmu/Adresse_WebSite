package hei.projet.adresse.service;

import hei.projet.adresse.dao.RestaurantsDao;
import hei.projet.adresse.dao.impl.RestaurantsDaoImpl;

import java.util.List;

import hei.projet.adresse.entity.Restaurant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestaurantsService {
    private final Logger LOG = LogManager.getLogger();

    protected static class InfoLibraryHolder {
        protected static RestaurantsService instance = new RestaurantsService();
    }

    public static RestaurantsService getInstance() {
        return RestaurantsService.InfoLibraryHolder.instance;
    }

    private RestaurantsDao restaurantsDao = new RestaurantsDaoImpl();

    private RestaurantsService() {
    }

    public List<Restaurant> listRestaurants() {
        LOG.info("Listing all restaurants:");
        return restaurantsDao.listRestaurants();
    }

    public Restaurant getRestaurantById(Integer id) {
        LOG.info("Getting restaurant with id #{}", id);
        return restaurantsDao.getRestaurantById(id);
    }

    public List<Restaurant> searchResult(String keyword) {
        LOG.info("Listing result of a search with keyword : ", keyword);
        return restaurantsDao.searchResult(keyword);
    }

    public Restaurant addRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("The restaurant can not be null.");
        }
        if (restaurant.getName() == null || "".equals(restaurant.getName())) {
            throw new IllegalArgumentException("A restaurant needs a name.");
        }
        if (restaurant.getSpecialty() == null || "".equals(restaurant.getSpecialty())) {
            throw new IllegalArgumentException("A restaurant needs a speciality.");
        }
        if (restaurant.getAddress() == null || "".equals(restaurant.getAddress())) {
            throw new IllegalArgumentException("A restaurant needs an address.");
        }
        if (restaurant.getPhone() == null) {
            throw new IllegalArgumentException("A restaurant needs a phone number.");
        }
        if (restaurant.getWebsite() == null || "".equals(restaurant.getWebsite())) {
            throw new IllegalArgumentException("A restaurant needs a website address.");
        }
        if (restaurant.getPrice() == null || restaurant.getPrice() < 0) {
            throw new IllegalArgumentException("A restaurant needs a price. (can bo 0)");
        }

        LOG.info("Adding new restaurant with attributes");
        LOG.info("  -id: {}", restaurant.getId());
        LOG.info("  -name: {}", restaurant.getName());
        LOG.info("  -specialty: {}", restaurant.getSpecialty());
        LOG.info("  -address: {}", restaurant.getAddress());
        LOG.info("  -phone: {}", restaurant.getPhone());
        LOG.info("  -website: {}", restaurant.getWebsite());
        LOG.info("  -price: {}", restaurant.getPrice());
        LOG.info("  -moreInfo: {}", restaurant.getMoreInfo());
        LOG.info(" -image: {}", restaurant.getImage());

        return restaurantsDao.addRestaurant(restaurant);
    }

    public void editRestaurant(Restaurant restaurant, String name, String specialty, String address, Integer price, String moreInfo, String phone, String website) {
        LOG.info("Editing a restaurant (restaurant id #{})", restaurant.getId());
        restaurantsDao.editRestaurant(restaurant, name, specialty, address, price, moreInfo, phone, website);
    }
}
