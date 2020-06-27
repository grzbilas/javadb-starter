package pl.sda.library.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sda.library.core.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUsersDao implements IUsersDao {
    private ConnectionFactory connFact;
    private static Logger logg = LoggerFactory.getLogger(JdbcUsersDao.class);

    public JdbcUsersDao(ConnectionFactory connFact) {
        this.connFact = connFact;
    }

    public JdbcUsersDao() {
    }

    @Override
    public User findUser(String login, String password) {
        String sql = "Select id,login,password name, admin from users where login=? and password=?";
        try (Connection connection = connFact.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                login = resultSet.getString("login");
                password = resultSet.getString("password");
                String name = resultSet.getString("name");
                boolean isAdmin = resultSet.getBoolean("admin");
                return new User(id, login, password, name, isAdmin);
            }
        } catch (SQLException e) {
            logg.error("findUser, Wystapil blad sql: " + sql);
            logg.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> list(UserParameters userParameters) {
        List<User> users = new ArrayList<>();
        String sql = "Select id,login,password name, admin from users";
        try (Connection connection = connFact.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                boolean isAdmin = resultSet.getBoolean("admin");
                users.add(new User(id, login, password, name, isAdmin));
            }
        } catch (SQLException e) {
            logg.error("list, Wystapil blad sql: " + sql);
            logg.error(e.getMessage());
        }
        return users;
    }

    @Override
    public void addUser(User user) {
        String sql = "Insert Into users (login,password, name, admin) values (?,?,?,?)";
        try (Connection connection = connFact.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setBoolean(4, user.isAdmin());
            statement.executeUpdate();
            logg.info("User " + user.getLogin() + " added.");
        } catch (SQLException e) {
            logg.error("addUser, Wystapil blad sql: " + sql);
            logg.error(e.getMessage());
        }
    }

    @Override
    public void deleteUser(int userId) {
        String sql = "Delete from users where id=?";
        try (Connection connection = connFact.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            logg.info("User with id=" + userId + " deleted.");
        } catch (SQLException e) {
            logg.error("deleteUser, Wystapil blad sql: " + sql);
            logg.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory("/library-database.properties");
        JdbcUsersDao dao = new JdbcUsersDao(connectionFactory);
        dao.addUser(new User(1, "admin", "admin", "Jan", true));
        dao.addUser(new User(1, "kosz", "smieci", "Jurek", false));
        dao.addUser(new User(1, "komoda", "biolo", "Romek", false));
        dao.addUser(new User(1, "atomek", "idol", "Tomek", true));
    }
}