package pl.sda.jdbc.zad4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnimalsTypesDao {
    private static Logger logg = LoggerFactory.getLogger(AnimalsTypesDao.class);
    private ConnectionFactory connectionFactory;

    public AnimalsTypesDao(ConnectionFactory connFact) {
        this.connectionFactory = connFact;
    }

    /**
     * Method for adding new animal
     * @param animalType
     * @throws SQLException
     */
    public void add(AnimalType animalType) throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO animals_types(name) VALUES(?)")) {

            statement.setString(1, animalType.getName());

            statement.executeUpdate();
        }
    }

    public List<AnimalType> list() throws SQLException {
        List<AnimalType> animalTypesList = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("Select id,name from animals_types")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                AnimalType animalType = new AnimalType(id, name);
                animalTypesList.add(animalType);
            }
        }

        return animalTypesList;
    }
    public String showOneAnim(int animalId) throws SQLException {
        String name = "";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("Select id,name from animals_types where id=?")) {
            statement.setInt(1,animalId);
            ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                name = resultSet.getString("name");
        }
        return name;
    }

    public void delAnim(int animalId) throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("Delete from animals_types where id=?")) {

            statement.setInt(1,animalId);

            statement.executeUpdate();
        }
        System.out.println("Deleted animal with id="+animalId);
    }
    public void updAnim(int animalId,String name) throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("Update animals_types set name=? where id=?")) {

            statement.setInt(2,animalId);
            statement.setString(1,name);

            statement.executeUpdate();
        }
        System.out.println("Change name of animal with id="+animalId);
    }
}
