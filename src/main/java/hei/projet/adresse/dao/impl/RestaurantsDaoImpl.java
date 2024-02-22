package hei.projet.adresse.dao.impl;

import hei.projet.adresse.dao.RestaurantsDao;
import hei.projet.adresse.entity.Restaurant;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RestaurantsDaoImpl implements RestaurantsDao {


    private Restaurant createRestaurantFromResult(ResultSet resultSelect) throws SQLException {
        return new Restaurant(
                resultSelect.getInt("id"),
                resultSelect.getString("name"),
                resultSelect.getString("specialty"),
                resultSelect.getString("address"),
                resultSelect.getString("phone"),
                resultSelect.getString("website"),
                resultSelect.getInt("price"),
                resultSelect.getString("moreInfo"),
                resultSelect.getString("image"),
                resultSelect.getString("carte"));
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        String sql = "INSERT INTO liste_restau (name, address, phone, price, specialty, moreInfo, website, image, carte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, restaurant.getName());
                preparedStatement.setString(2, restaurant.getAddress());
                preparedStatement.setString(3, restaurant.getPhone());
                preparedStatement.setInt(4, restaurant.getPrice());
                preparedStatement.setString(5, restaurant.getSpecialty());
                preparedStatement.setString(6, restaurant.getMoreInfo());
                preparedStatement.setString(7, restaurant.getWebsite());
                preparedStatement.setString(10, restaurant.getImage());
                preparedStatement.setString(11, restaurant.getCarte());
                preparedStatement.executeUpdate();
                ResultSet ids = preparedStatement.getGeneratedKeys();
                if (ids.next()) {
                    restaurant.setId(ids.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    @Override
    public void editRestaurant(Restaurant Restaurant, String Name, String Specialty, String Address, Integer Price, String MoreInfo, String Phone, String Website) {
        String sql = "UPDATE data_adresse.liste_restau t SET t.name = ?, t.address = ?, t.phone = ?, t.price = ?, t.specialty = ?, t.moreInfo = ?, t.website = ? WHERE t.id = ?";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, Name);
                preparedStatement.setString(2, Address);
                preparedStatement.setString(3, Phone);
                preparedStatement.setInt(4, Price);
                preparedStatement.setString(5, Specialty);
                preparedStatement.setString(6, MoreInfo);
                preparedStatement.setString(7, Website);
                preparedStatement.setInt(8, Restaurant.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Restaurant> listRestaurants() {
        List<Restaurant> result = new ArrayList<>();
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 Statement statement = cnx.createStatement();
                 ResultSet resultSelect = statement.executeQuery("SELECT * FROM liste_restau")) {
                while (resultSelect.next()) {
                    result.add(createRestaurantFromResult(resultSelect));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Restaurant getRestaurantById(Integer id) {
        Restaurant est = null;
        String sql = "SELECT * FROM liste_restau WHERE id=?";
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        est = createRestaurantFromResult(result);
                    }
                }
            }
        } catch (SQLException e) {
            //TODO changer en log
            e.printStackTrace();
        }
        return est;
    }

    @Override
    public List<String> listParams() {
        List<String> listParams = new ArrayList<>();
        listParams.add("id");
        listParams.add("name");
        listParams.add("specialty");
        listParams.add("address");
        listParams.add("phone");
        listParams.add("website");
        listParams.add("price");
        listParams.add("moreInfo");
        listParams.add("image");
        listParams.add("carte");
        return listParams;
    }

    @Override
    public List<Restaurant> searchResult(String keyword) {
        List<Restaurant> result = new ArrayList<>();
        List<String> listParams = listParams();
        List<String> keywordCases = new ArrayList<>();

        keywordCases.add(keyword.concat("%"));
        keywordCases.add("%".concat(keyword));
        keywordCases.add("%".concat(keyword).concat("%"));

        for (String selector : keywordCases) { //Try each placement
            for (String param : listParams) { //Try each column
                List<Restaurant> resultNotVerified = searchRequest(selector, param);

                for (Restaurant newRestaurant : resultNotVerified) { //Verify restaurant is new in list
                    System.out.println(newRestaurant.getName());
                    boolean alreadyAdd = false;
                    for (Restaurant restau : result) {
                        if (restau.getId() == newRestaurant.getId()) {
                            alreadyAdd = true;
                        }
                    }
                    if (!alreadyAdd) {
                        result.add(newRestaurant);
                    }
                }
            }
        }
        return result;
    }


    @Override
    public List<Restaurant> searchRequest(String keyword, String param) {
        List<Restaurant> result = new ArrayList<>();
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 Statement statement = cnx.createStatement();
                 ResultSet resultSelect = statement.executeQuery("SELECT * FROM liste_restau WHERE " + param + " LIKE '" + keyword + "'")) {
                while (resultSelect.next()) {
                    result.add(createRestaurantFromResult(resultSelect));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public Restaurant getRandomRestaurant() {
        Restaurant result = null;
        //TODO mettre dans un service
        Random id = new Random(System.currentTimeMillis());
        int max = 29;
        int min = 1;
        Integer idRandom = id.nextInt(max - min + 1) + min;
        try {
            DataSource dataSource = DataSourceProvider.getDataSource();
            try (Connection cnx = dataSource.getConnection();
                 Statement statement = cnx.createStatement();

                 ResultSet resultSelect = statement.executeQuery("SELECT * FROM liste_restau WHERE id = '" + idRandom + "'")) {
                while (resultSelect.next()) {
                    result = createRestaurantFromResult(resultSelect);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteRestaurant(Restaurant restaurant) {
        String sql = "DELETE FROM data_adresse.liste_restau WHERE id = " + restaurant.getId();
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

}