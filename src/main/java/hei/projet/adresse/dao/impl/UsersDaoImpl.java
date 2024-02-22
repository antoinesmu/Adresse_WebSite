package hei.projet.adresse.dao.impl;

import hei.projet.adresse.dao.UsersDao;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.LoginAlreadyExistingException;
import hei.projet.adresse.exception.users.UserNotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDaoImpl implements UsersDao {

    private Users createUserFromResult(ResultSet resultSelect) throws SQLException {
        return new Users(
                null,
                resultSelect.getInt("id_user"),
                resultSelect.getString("login"),
                resultSelect.getString("password"),
                resultSelect.getString("firstName"),
                resultSelect.getString("lastName"),
                resultSelect.getString("mail"),
                resultSelect.getBoolean("adminPrivilege"));
    }

    @Override
    public List<Users> listUsers() {
        List<Users> result = new ArrayList<>();
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 Statement statement = cnx.createStatement();
                 ResultSet resultSelect = statement.executeQuery("SELECT * FROM utilisateur ORDER BY id_user")) {
                while (resultSelect.next()) {
                    result.add(createUserFromResult(resultSelect));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Users getUserById(Integer id) throws UserNotFoundException {
        Users user = null;
        String sql = "SELECT * FROM utilisateur WHERE id_user=?";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        user = createUserFromResult(result);
                    } else {
                        throw new UserNotFoundException("User not found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Users addUser(Users user) {
        String sql = "INSERT INTO utilisateur (login, password, firstName, lastName, mail, adminPrivilege) VALUES (?, ?, ?, ?, ?, 0)";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getFirstName());
                preparedStatement.setString(4, user.getLastName());
                preparedStatement.setString(5, user.getEmail());
                preparedStatement.executeUpdate();
                ResultSet ids = preparedStatement.getGeneratedKeys();
                if (ids.next()) {
                    user.setId(ids.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Users getUserByLogin(String login) throws UserNotFoundException {
        Users user = null;
        String sql = "SELECT * FROM utilisateur WHERE login = '" + login + "'";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        user = createUserFromResult(result);
                    } else {
                        throw new UserNotFoundException("User not found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String getPassword(String login) {
        String hPass = null;
        String sql = "SELECT password FROM utilisateur WHERE login = '" + login + "'";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        hPass = result.getString("password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hPass;
    }

    public ArrayList<Users> getUserInfo(String login) {
        ArrayList<Users> userInfo = new ArrayList<>();
        String sql = "SELECT login,firstName, lastName, mail FROM utilisateur WHERE login = '" + login + "'";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        userInfo.add(createUserFromResult(result));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    @Override
    public void deleteUser(Users user) {
        String sql = "DELETE FROM data_adresse.utilisateur WHERE id_user = " + user.getId();
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePassword(Users users, String newPassword) {
        String login = users.getLogin();
        String sql = "UPDATE utilisateur SET password = '" + newPassword + "' WHERE login = '" + login + "'";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    //nothing
                } else {
                    throw new UserNotFoundException("User not found");
                }
            }
        } catch (SQLException | UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePrivilege(int userId) {
        String sql = "UPDATE utilisateur SET adminPrivilege = NOT adminPrivilege WHERE id_user = '" + userId + "'";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    //nothing
                } else {
                    throw new UserNotFoundException("User not found");
                }
            }
        } catch (SQLException | UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkSimilarLogin(Users newUser) throws LoginAlreadyExistingException {
        List<Users> allUsers = listUsers();
        Boolean alreadyExists = false;
        for (Users user : allUsers) {
            if (newUser.getLogin().equals(user.getLogin())) {
                alreadyExists = true;
                throw new LoginAlreadyExistingException("login already exist");
            }
        }
        return alreadyExists;
    }

}
