package pl.sda.jdbc.zad4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ZooKeeper {
    private static Logger logg = LoggerFactory.getLogger(ZooKeeper.class);

    public static void main(String[] args) throws SQLException {
        ConnectionFactory connFactory = new ConnectionFactory("/zoo-database.properties");
        AnimalsTypesDao animalTypDao = new AnimalsTypesDao(connFactory);
        animalTypDao.add(new AnimalType(1, "Dog"));
        animalTypDao.add(new AnimalType(2, "Frog"));
        animalTypDao.add(new AnimalType(3, "Cat"));
        animalTypDao.add(new AnimalType(4, "Horse"));

        animalTypDao.delAnim(3);
        animalTypDao.updAnim(4,"Bird");
        List<AnimalType> listOfAnimal = animalTypDao.list();
        logg.info("List of animals types:");
        listOfAnimal.forEach(t -> logg.info(t.toString()));

        System.out.println("Animal, with id. 3d: "+animalTypDao.showOneAnim(2));

    }
}
