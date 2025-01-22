package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    Connection connection = Util.getConnection();

    public void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100)," +
                "lastname VARCHAR(100)," +
                "age INT(3))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTable)) {
            preparedStatement.executeUpdate();
            System.out.println("The table is create successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String dropTable = "DROP TABLE IF EXISTS users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(dropTable)) {
            preparedStatement.executeUpdate();
            System.out.println("The table is drop successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastname, age) VALUES(?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("The saveUser is successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int isRemove = preparedStatement.executeUpdate();
            if (isRemove > 0) {
                System.out.println("The user by id = " + id + " is removed");
            } else
                System.out.println("The user by id = " + id + " is not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT id, name, lastname, age FROM users";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setAge(resultSet.getByte("age"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                userList.add(user);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }


    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int num = preparedStatement.executeUpdate();
            System.out.println("The users table has been cleared. Deleted entries: " + num);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
