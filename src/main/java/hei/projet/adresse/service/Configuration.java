package hei.projet.adresse.service;

import hei.projet.adresse.dao.impl.DataSourceProvider;
import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {


    private static final Logger LOG = LogManager.getLogger();

    public static Properties loadProperties() {
        BasicConfigurator.configure();
        LOG.info("Loading properties file:");
        try (InputStream input = DataSourceProvider.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                LOG.info("Properties file not found!");
                throw new IllegalStateException("Properties file not found!");
            }

            Properties configuration = new Properties();
            configuration.load(input);
            LOG.info("Properties file successfully loaded!");
            return configuration;
        } catch (IOException e) {
            LOG.info("Problem when reading the properties file!");
            throw new RuntimeException("Problem when reading the properties file!", e);
        }
    }
}
