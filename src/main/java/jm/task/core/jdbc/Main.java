package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        UserService sqlAction = new UserServiceImpl();

        sqlAction.createUsersTable();

        sqlAction.saveUser("Oleg", "Olegov", (byte) 30);
        sqlAction.saveUser("Ivan", "Ivanov", (byte) 23);
        sqlAction.saveUser("Levan", "Gorozia", (byte) 30);
        sqlAction.saveUser("Shamil", "Ahmedov", (byte) 30);

        System.out.println(sqlAction.getAllUsers());

        sqlAction.cleanUsersTable();
        sqlAction.dropUsersTable();

    }
}
