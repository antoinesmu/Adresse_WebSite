package hei.projet.adresse.dao;

import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.LoginAlreadyExistingException;
import hei.projet.adresse.exception.users.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

public interface UsersDao {
    List<Users> listUsers();

    Users getUserById(Integer id) throws UserNotFoundException;

    Users getUserByLogin(String login) throws UserNotFoundException;

    Users addUser(Users user);

    String getPassword(String login);

    ArrayList<Users> getUserInfo(String login);

    void deleteUser(Users user);

    void changePassword(Users user, String newPassword);

    void changePrivilege(int userId);

    boolean checkSimilarLogin(Users users) throws LoginAlreadyExistingException;
}
