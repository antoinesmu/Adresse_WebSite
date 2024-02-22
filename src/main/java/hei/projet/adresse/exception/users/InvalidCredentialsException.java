package hei.projet.adresse.exception.users;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String errorMessage) {
        super(errorMessage);
    }
}
