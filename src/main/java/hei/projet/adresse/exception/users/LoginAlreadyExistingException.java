package hei.projet.adresse.exception.users;

public class LoginAlreadyExistingException extends Exception {
    public LoginAlreadyExistingException(String errorMessage) {
        super(errorMessage);
    }
}
