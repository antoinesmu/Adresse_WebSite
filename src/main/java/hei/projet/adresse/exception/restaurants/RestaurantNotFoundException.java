package hei.projet.adresse.exception.restaurants;

public class RestaurantNotFoundException extends Exception {
    public RestaurantNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
