package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Task1.User (" +
            "user_id BIGINT NOT NULL AUTO_INCREMENT," +
            "name VARCHAR(30) NOT NULL," +
            "lastName VARCHAR(30) NOT NULL," +
            "age TINYINT NOT NULL," +
            "PRIMARY KEY (user_id))";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS Task1.User";

    private static final String SQL_SAVE_USER = "INSERT INTO Task1.User (name, lastName, age) VALUES(?, ?, ?)";

    private static final String SQL_REMOVE_USER_BY_ID = "DELETE from Task1.User where user_id=?";

    private static final String SQL_GET_ALL_USERS = "SELECT user_id, name, lastName, age FROM Task1.User";

    private static final String SQL_CLEAN_TABLE = "DELETE FROM Task1.User";

    public void createUsersTable() {

        try (Connection connection = Util.getConnection();
             Statement statement = Util.getStatement(connection)) {

                statement.executeUpdate(SQL_CREATE_TABLE);
                System.out.println("Таблица успешно создана!");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы", e);
        }
    }

    public void dropUsersTable() {

        try (Connection connection = Util.getConnection();
             Statement statement = Util.getStatement(connection)) {

                statement.executeUpdate(SQL_DROP_TABLE);
                System.out.println("Таблица успешно удалена!");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection()) {
            try {

                connection.setAutoCommit(false);

                PreparedStatement prepareStatement = connection.prepareStatement(SQL_SAVE_USER);

                prepareStatement.setString(1, name);
                prepareStatement.setString(2, lastName);
                prepareStatement.setByte(3, age);

                prepareStatement.executeUpdate();
                connection.commit();

                System.out.printf("User с именем – %s добавлен в базу дынных%n", name);

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Ошибка при добавлении пользователя", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {

        try (Connection connection = Util.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_USER_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Пользователь успешно удален!");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }

    }

    public List<User> getAllUsers() {

        List<User> usersList = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             Statement statement = Util.getStatement(connection)) {

            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_USERS);

            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                byte age = resultSet.getByte(4);

                User user = new User(name, lastName, age);
                user.setId(id);
                usersList.add(user);

            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке пользователей", e);
        }

        return usersList;
    }

    public void cleanUsersTable() {

        try (Connection connection = Util.getConnection();
             Statement statement = Util.getStatement(connection)) {

            statement.executeUpdate(SQL_CLEAN_TABLE);
            System.out.println("Все строки успешно удалены!");


        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении строк таблицы", e);
        }
    }
}
