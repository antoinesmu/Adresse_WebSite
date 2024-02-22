package hei.projet.adresse.exception.restaurants;

public class RestaurantAlreadyExistingException extends Exception {
    public RestaurantAlreadyExistingException(String errorMessage) {
        super(errorMessage);
    }
}
