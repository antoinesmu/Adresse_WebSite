package hei.projet.adresse.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import hei.projet.adresse.dao.UsersDao;
import hei.projet.adresse.dao.impl.UsersDaoImpl;
import hei.projet.adresse.entity.ReviewWithLogin;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.LoginAlreadyExistingException;
import hei.projet.adresse.exception.users.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class UsersService {
    private final Logger LOG = LogManager.getLogger();

    private final Properties configuration = Configuration.loadProperties();

    private final Argon2 argon2 = Argon2Factory.create();

    private final int argon2_iterations = Integer.parseInt(configuration.getProperty("argon2.iterations"));
    private final int argon2_memory = Integer.parseInt(configuration.getProperty("argon2.memory"));
    private final int argon2_parallelism = Integer.parseInt(configuration.getProperty("argon2.parallelism"));

    private UsersDao userDao = new UsersDaoImpl();
    private ReviewService reviewService = ReviewService.getInstance();

    public UsersService() {
    }

    private static class InfoLibraryHolder {
        private final static UsersService instance = new UsersService();
    }

    public static UsersService getInstance() {
        return UsersService.InfoLibraryHolder.instance;
    }


    public String encode(String password) {
        if (password == null || password.equals("")) {
            throw new IllegalArgumentException("A user needs a password.");
        } else {
            LOG.info("Hashing {} with parameters: i:{}, m:{}, p:{}", password, argon2_iterations, argon2_memory, argon2_parallelism);
            char[] pwd = password.toCharArray();
            String hash = argon2.hash(argon2_iterations, argon2_memory, argon2_parallelism, pwd);
            argon2.wipeArray(pwd);
            LOG.info("{} hashed into {}", password, hash);
            return hash;
        }
    }

    public boolean verifyPassword(Users user, String password) {
        LOG.info("Verifying if {} match with {}'s password", password, user.getLogin());
        if (argon2.verify(user.getPassword(), password)) {
            LOG.info("Password match!");
            return true;
        } else {
            LOG.info("Password doesn't match!");
            return false;
        }
    }

    public boolean verifyPassword(String hash, String password) {
        LOG.info("Verifying if {} match with {}", password, hash);
        if (argon2.verify(hash, password)) {
            LOG.info("Password match!");
            return true;
        } else {
            LOG.info("Password doesn't match!");
            return false;
        }
    }

    public Users addUser(Users user) throws LoginAlreadyExistingException {
        if (user.getLogin() == null || user.getLogin().equals("")) {
            throw new IllegalArgumentException("A user needs a login.");
        }
        if (userDao.checkSimilarLogin(user)) throw new LoginAlreadyExistingException("This login already exists.");
        if (user.getPassword() == null || user.getPassword().equals("")) {
            throw new IllegalArgumentException("A user needs a password.");
        }
        if (user.getFirstName() == null || user.getFirstName().equals("")) {
            throw new IllegalArgumentException("A user needs a firstName.");
        }
        if (user.getLastName() == null || user.getLastName().equals("")) {
            throw new IllegalArgumentException("A user needs a lastName.");
        }
        if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new IllegalArgumentException("A user needs an email.");
        }
        LOG.info("Adding new user with attributes:");
        LOG.info("  -login: {}", user.getLogin());
        LOG.info("  -password: {}", user.getPassword());
        LOG.info("  -firstName: {}", user.getFirstName());
        LOG.info("  -lastName: {}", user.getLastName());
        LOG.info("  -email: {}", user.getEmail());
        LOG.info("  -privilege: {}", user.getAdminPrivilege());
        return userDao.addUser(user);
    }

    public List<Users> listUsers() {
        LOG.info("Listing all users:");
        return userDao.listUsers();
    }

    public Users getUserById(Integer id) throws UserNotFoundException {
        LOG.info("Getting user with id #{}:", id);
        return userDao.getUserById(id);
    }

    public Users getUserByLogin(String login) throws UserNotFoundException {
        LOG.info("Getting user with login {}:", login);
        return userDao.getUserByLogin(login);
    }

    public Boolean changePassword(Users user, String oldPassword, String newPassword) {
        LOG.info("Trying to changer {}'s password to {}", user.getLogin(), newPassword);
        if (verifyPassword(user, oldPassword)) {
            LOG.info("Password successfully changed");
            newPassword = encode(newPassword);
            user.setPassword(newPassword);
            userDao.changePassword(user, newPassword);
            return true;
        } else {
            LOG.info("Password not changed (old password didn't matched)");
            return false;
        }
    }

    public void deleteUser(Users user) throws UserNotFoundException {
        LOG.info("Deleting review then user {}", user.getLogin());
        List<ReviewWithLogin> listReviewUser = reviewService.listReviewByUserId(user.getId());
        for (ReviewWithLogin review : listReviewUser) {
            reviewService.deleteReview(review);
        }
        userDao.deleteUser(user);
    }

    public void changePrivilege(int userId) throws UserNotFoundException {
        Users user = getUserById(userId);
        LOG.info("change privilege of user {}", user.getLogin());
        userDao.changePrivilege(userId);
    }

    public boolean checkSimilarLogin(Users newUser) throws LoginAlreadyExistingException {
        LOG.info("verify if login doesn't exist for new user {}", newUser);
        return userDao.checkSimilarLogin(newUser);
    }
}