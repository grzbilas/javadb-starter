package pl.sda.jdbc.zad4;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
    private static DataSource dataSource;

    public ConnectionFactory(String filename) {
        try {
            Properties properties = getDataBaseProperties(filename);
            dataSource = getDataSource(properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectionFactory() {
        this("/zoo-database.properties");
    }

    private Properties getDataBaseProperties(String filename) {
        Properties properties = new Properties();
        try {
            /**
             * Pobieramy zawartość pliku za pomocą classloadera, plik musi znajdować się w katalogu ustawionym w CLASSPATH
             */
            InputStream propertiesStream = ConnectionFactory.class.getResourceAsStream(filename);
            if (propertiesStream == null) {
                throw new IllegalArgumentException("Can't find file: " + filename);
            }
            /**
             * Pobieramy dane z pliku i umieszczamy w obiekcie klasy Properties
             */
            properties.load(propertiesStream);
        } catch (IOException e) {
            logger.error("Error during fetching properties for database", e);
            return null;
        }

        return properties;
    }

    private DataSource getDataSource(Properties properties) throws SQLException {
        String serverName = properties.getProperty("pl.sda.jdbc.db.server");
        String databaseName = properties.getProperty("pl.sda.jdbc.db.name");
        String user = properties.getProperty("pl.sda.jdbc.db.user");
        String password = properties.getProperty("pl.sda.jdbc.db.password");
        int port = Integer.valueOf(properties.getProperty("pl.sda.jdbc.db.port"));

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(serverName);
        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setPort(port);
        dataSource.setCharacterEncoding("UTF-8");
        dataSource.setServerTimezone("Europe/Warsaw");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
        //ustawienie tego parametru ułatwia przeprowadzenie SQLInjection typu MultiQueries
        //dataSource.setAllowMultiQueries(true);

        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not created!");
        }
        return dataSource.getConnection();
    }

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory("/zoo-database.properties");
        try (Connection connection = connectionFactory.getConnection()) {
            logger.info("Connection = " + connection);
            logger.info("Database name = " + connection.getCatalog());
        } catch (SQLException e) {
            logger.error("Error during using connection", e);
        }
    }
}