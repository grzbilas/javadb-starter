package pl.sda.library.core;

import pl.sda.library.books.IBooksDao;
import pl.sda.library.books.JdbcBooksDao;
import pl.sda.library.orders.IOrdersDao;
import pl.sda.library.orders.JdbcOrdersDao;
import pl.sda.library.users.IUsersDao;
import pl.sda.library.users.JdbcUsersDao;

/**
 * Wykorzystanie wzorca Factory do umieszczenia kodu tworzącego obiekty DAO w jednym miejscu
 */
public class DaoFactory {
    public static IUsersDao getUsersDao() {
        ConnectionFactory connectionFactory = new ConnectionFactory("/library-database.properties");
        return new JdbcUsersDao(connectionFactory);
    }

    public static IBooksDao getBooksDao() {
        return new JdbcBooksDao();
    }

    public static IOrdersDao getOrdersDao() {
        return new JdbcOrdersDao();
    }
}