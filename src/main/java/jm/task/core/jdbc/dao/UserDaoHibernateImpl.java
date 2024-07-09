package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Task1.User (" +
            "user_id BIGINT NOT NULL AUTO_INCREMENT," +
            "name VARCHAR(30) NOT NULL," +
            "lastName VARCHAR(30) NOT NULL," +
            "age TINYINT NOT NULL," +
            "PRIMARY KEY (user_id))";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS Task1.User";

    private static final String SQL_SAVE_USER = "INSERT INTO Task1.User (name, lastName, age) VALUES(:name, :lastName, :age)";

    private static final String SQL_REMOVE_USER_BY_ID = "DELETE from Task1.User where user_id=:id";

    private static final String SQL_GET_ALL_USERS = "SELECT user_id, name, lastName, age FROM Task1.User";

    private static final String SQL_CLEAN_TABLE = "DELETE FROM Task1.User";

    @Override
    public void createUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.createSQLQuery(SQL_CREATE_TABLE).executeUpdate();
            transaction.commit();
            System.out.println("Таблица создана!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка при создании Таблицы", e);
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.createSQLQuery(SQL_DROP_TABLE).executeUpdate();
            transaction.commit();
            System.out.println("Таблица удалена!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка при удалении Таблицы", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            Query<?> query = session.createSQLQuery(SQL_SAVE_USER);

            query.setParameter("name", name);
            query.setParameter("lastName", lastName);
            query.setParameter("age", age);
            query.executeUpdate();

            transaction.commit();
            System.out.printf("User с именем – %s добавлен в базу дынных%n", name);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка при добавлении пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            Query<?> query = session.createSQLQuery(SQL_REMOVE_USER_BY_ID);

            query.setParameter("id", id);
            query.executeUpdate();

            transaction.commit();
            System.out.println("Пользователь успешно удален!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    @Override
    public List<User> getAllUsers() {

        List<User> usersList = new ArrayList<>();

        try (Session session = Util.getSessionFactory().openSession()) {

            Query query = session.createSQLQuery(SQL_GET_ALL_USERS);
            List<Object[]> users = query.list();

            for (Object[] data : users) {

                BigInteger id = (BigInteger) data[0];
                String name = (String) data[1];
                String lastName = (String) data[2];
                byte age = (byte) data[3];

                User user = new User(name, lastName, age);
                user.setId(id.longValue());

                usersList.add(user);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке пользователей", e);
        }
        return usersList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.createSQLQuery(SQL_CLEAN_TABLE).executeUpdate();
            transaction.commit();
            System.out.println("Все строки успешно удалены!");


        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка при очистке пользователей", e);
        }
    }
}
