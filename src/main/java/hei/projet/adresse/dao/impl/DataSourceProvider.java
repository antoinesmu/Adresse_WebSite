package hei.projet.adresse.dao.impl;

import hei.projet.adresse.service.Configuration;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceProvider {

    private Properties properties;
    private static MariaDbDataSource dataSource;

    public static DataSource getDataSource() throws SQLException {
        if (dataSource == null) {
            Properties configuration = Configuration.loadProperties();
            dataSource = new MariaDbDataSource();
            dataSource.setServerName(configuration.getProperty("database.serverHost"));
            dataSource.setPort(Integer.parseInt(configuration.getProperty("database.serverPort")));
            dataSource.setDatabaseName(configuration.getProperty("database.name"));
            dataSource.setUser(configuration.getProperty("database.user"));
            dataSource.setPassword(configuration.getProperty("database.password"));
        }
        return dataSource;
    }
}
