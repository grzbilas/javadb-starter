package pl.sda.library.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sda.library.core.ConnectionFactory;
import pl.sda.library.users.JdbcUsersDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcBooksDao implements IBooksDao {
    private ConnectionFactory connFact;
    private static Logger logg = LoggerFactory.getLogger(JdbcUsersDao.class);

    public JdbcBooksDao(ConnectionFactory connFact) {
        this.connFact = connFact;
    }

    public JdbcBooksDao() {    }

    @Override
    public List<Book> list() {
        return new ArrayList<>();
    }

    public void addCategory(Category ct) {
        String sql = "Insert Into categories (id,name) values (?,?)";
        try (Connection connection = connFact.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ct.getId());
            statement.setString(2, ct.getName());
            statement.executeUpdate();
            logg.info("Kategoria " + ct.getName() + " added.");
        } catch (SQLException e) {
            logg.error("addCategory, Wystapil blad sql: " + sql);
            logg.error(e.getMessage());
        }
    }

    @Override
    public void add(Book book) {
        String sql = "Insert Into books (category_id,title,author) values (?,?,?)";
        try (Connection connection = connFact.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getCategoryId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.executeUpdate();
            logg.info("Book " + book.getTitle() + " added.");
        } catch (SQLException e) {
            logg.error("add Book, Wystapil blad sql: " + sql);
            logg.error(e.getMessage());
        }
    }

    @Override
    public void delete(int bookId) {
        String sql = "Delete from books where id=?";
        try (Connection connection = connFact.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
            logg.info("Book with id=" + bookId + " deleted.");
        } catch (SQLException e) {
            logg.error("delete Book, Wystapil blad sql: " + sql);
            logg.error(e.getMessage());
        }
    }

    @Override
    public List<Category> listCategories() {
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory("/library-database.properties");
        JdbcBooksDao jbd = new JdbcBooksDao(connectionFactory);
        jbd.addCategory(new Category(1,"Dokument"));
        jbd.addCategory(new Category(2,"Powieść"));
        jbd.add( new Book("Lalka","Bolesław Prus",2));
        jbd.delete(2);
//        System.out.println("Usun wszystkie książki "); for (Book b: jbd.list(null)) jbd.delete(b.getId());
    }
}