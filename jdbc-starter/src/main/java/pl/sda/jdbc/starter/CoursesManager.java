package pl.sda.jdbc.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;

public class CoursesManager {
    private static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
    private ConnectionFactory connectionFactory;

    public CoursesManager(String filename) {
        this.connectionFactory = new ConnectionFactory(filename);
    }
//    private static DataSource dataSource;

    public void createCoursesTable() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists courses (" +
                    "id int not null auto_increment, name VARCHAR(50),place VARCHAR(50)" +
                    ",start_date DATE,end_date DATE,PRIMARY KEY(id))");
            logger.info("Table courses created");
            statement.executeUpdate("Insert into courses (name,place,start_date,end_date) values ('JavaGli2','Gliwice','2019-11-15','2020-09-15')");
            statement.executeUpdate("Insert into courses (name,place,start_date,end_date) values ('JavaGli3','Gliwice','2020-11-15','2021-09-15')");
            statement.executeUpdate("Insert into courses (name,place,start_date,end_date) values ('JavaGli4','Gliwice','2021-11-15','2022-09-15')");
            statement.executeUpdate("Insert into courses (name,place,start_date,end_date) values ('JavaGda6','Gdansk','2018-05-15','2019-06-22')");
            logger.info("Courses inserted");
        }
    }

    public void createStudentsTable() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists students (" +
                    "id int not null auto_increment, name VARCHAR(50),course_id INT" +
                    ",description VARCHAR(200),seat VARCHAR(10)" +
                    ",PRIMARY KEY(id)" +
                    ",KEY `course_id_fk` (`course_id`)" +
                    ",CONSTRAINT `course_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`ID`))");
            logger.info("Table students created");
            statement.executeUpdate("Insert into Students (course_id,name,seat,description) values (1,'Grzegorz','A.3.2','Chce poznac nowe jezyki programowania')");
            statement.executeUpdate("Insert into Students (course_id,name,seat,description) values (1,'Marek','A.3.1','Zmiana pracy')");
            statement.executeUpdate("Insert into Students (course_id,name,seat,description) values (1,'Sebastian','A.3.3','Rozwój zainteresowań')");
            statement.executeUpdate("Insert into Students (course_id,name,seat,description) values (1,'Marcin','C.2.1','Ciekawość')");
            statement.executeUpdate("Insert into Students (course_id,name,seat,description) values (1,'Ania','C.1.4','Zmiana pracy')");
            statement.executeUpdate("Insert into Students (course_id,name,seat,description) values (1,'Ewelina','B.2.1','Rozwój zainteresowań')");
            logger.info("Students inserted");
        }

    }

    public void createAttendanceListTable() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists attendance_list (" +
                    "id int not null auto_increment" +
                    ",course_id INT" +
                    ",student_id INT" +
                    ",date DATETIME,PRIMARY KEY(id)" +
                    ",KEY `att_course_id_fk` (`course_id`)" +
                    ",KEY `att_student_id_fk` (`student_id`)" +
                    ",CONSTRAINT `att_course_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`ID`)" +
                    ",CONSTRAINT `att_student_id_fk` FOREIGN KEY (`student_id`) REFERENCES `students` (`ID`))");
            logger.info("Table attendance_list created");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,1,'2019-11-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,2,'2019-11-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,3,'2019-11-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,4,'2019-11-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,5,'2019-11-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,6,'2019-11-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (2,1,'2019-12-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,1,'2020-01-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,2,'2020-01-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,3,'2020-01-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,4,'2020-01-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,5,'2020-01-15')");
            statement.executeUpdate("Insert into attendance_list (course_id,student_id,date) values (1,6,'2020-01-15')");
            logger.info("Attendance inserted");
        }
    }

    public void dropAllTables() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("Drop table if exists sda_courses.attendance_list");
            statement.executeUpdate("Drop table if exists sda_courses.students");
            statement.executeUpdate("Drop table if exists courses");
            logger.info("All tables dropped");
        }
    }

    public void printAllCourses() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("Select id,name,place,start_date, end_date from courses");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String place = resultSet.getString("place");
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");
                logger.info("{},{},{},{} - {}}", id, name, place, startDate, endDate);
            }
        }
    }

    public void printAllStudents() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("Select id,name,description,seat from students");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String seat = resultSet.getString("seat");
                logger.info("{},{},{},{}}", id, name, description,seat);
            }
        }
    }

    public void printAllStudentsPrepared(int courseId) throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "Select s.id,s.name, c.name course_name,description,seat from students s " +
                             " LEFT JOIN courses c on s.course_id=c.id" +
                             " where c.id=?") )
        {

            statement.setInt(1,courseId);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("printAllStudentsPrepared");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String seat = resultSet.getString("seat");
                String courseName = resultSet.getString("course_name");
                logger.info("Prepared: {},{},{},{},{}}", id, name, description,seat,courseName);
            }
        }
    }
    public static void main(String[] args) throws SQLException {
        CoursesManager cm = new CoursesManager("/sda-courses-database.properties");
        cm.createCoursesTable();
        cm.createStudentsTable();
        cm.createAttendanceListTable();
        cm.printAllCourses();
        cm.printAllStudents();
        cm.printAllStudentsPrepared(1);

        cm.dropAllTables();
    }

}
