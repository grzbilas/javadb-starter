package pl.sda.jdbc.starter;

import java.sql.*;

public class SqlInjectionSample {

    public void createTable() throws SQLException {
        try (Connection connection = new ConnectionFactory().getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE admins (" +
                    "  id INT NOT NULL AUTO_INCREMENT, " +
                    "  login VARCHAR(50) NOT NULL, " +
                    "  password VARCHAR(45) NOT NULL, " +
                    "PRIMARY KEY (id))"
            );
        }
    }

    public String findAdmin(String login, String password) throws SQLException {
        String query = "SELECT id, login, password FROM admins WHERE login=? AND password=?;";
        try (Connection connection = new ConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,login);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                login = resultSet.getString("login");
                password = resultSet.getString("password");
                return id + ", " + login + ", " + password;
            }
            return null;
        }
    }

    public void addAdmin(String login, String password) throws SQLException {
        String query = "INSERT INTO admins(login, password) VALUES(?, ?);";
        try (Connection connection = new ConnectionFactory().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeUpdate();
        }
    }

    public static void main (String[] args) throws SQLException {
        SqlInjectionSample sample = new SqlInjectionSample();

        //sample.createTable();

//        sample.addAdmin("albert", "sekurak");
//        sample.addAdmin("top", "secret");
        System.out.println("admin = " + sample.findAdmin("top", "secret"));
        System.out.println("not-admin = " + sample.findAdmin("top", "123"));

        //dodanie OR'a
        String admin = sample.findAdmin("", "' OR 1='1");
        System.out.println("admin1 = " + admin);

        //dodanie znaku początku komentarza
        String admin2 = sample.findAdmin("top'#", "123");
        System.out.println("admin2 = " + admin2);

        //połączenie OR'a i komentarzy
        String admin3 = sample.findAdmin("' OR 1=1;#", "123");
        System.out.println("admin3 = " + admin3);

        //MultiQueries
        sample.addAdmin("123", "');DROP TABLE admins;#");
    }
}