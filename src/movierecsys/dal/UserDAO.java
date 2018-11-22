/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class UserDAO {

    SQLServerDataSource ds;

    public UserDAO() throws IOException {
        this.ds = new SQLServerDataSource();
        ConnectionDAO connectionInfo = new ConnectionDAO();
        List<String> infoList = connectionInfo.getDatabaseInfo();
        ds.setDatabaseName(infoList.get(0));
        ds.setUser(infoList.get(1));
        ds.setPassword(infoList.get(2));
        ds.setPortNumber(Integer.parseInt(infoList.get(3)));
        ds.setServerName(infoList.get(4));
    }

    /**
     * Gets a list of all known users.
     *
     * @return List of users.
     */
    public List<User> getAllUsers() throws IOException {
        List<User> allUsers = new ArrayList<>();

        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Users";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                int Id = rs.getInt("ID");
                String name = rs.getString("Name");
                User us = new User(Id, name);
                allUsers.add(us);
            }
            return allUsers;

        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /**
     * Gets a single User by its ID.
     *
     * @param id The ID of the user.
     * @return The User with the ID.
     */
    public User getUser(int id) throws IOException {
        List<User> all = getAllUsers();
        int index = Collections.binarySearch(all, new User(id, ""), new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        if (index >= 0) {
            return all.get(index);
        } else {
            throw new IllegalArgumentException("No user with ID: " + id + " is found.");
        }
    }

    /**
     * Updates a user so the persistence storage reflects the given User object.
     *
     * @param user The updated user.
     */
    public void updateUser(User user) throws IOException {

        try (Connection con = ds.getConnection()) {

            String query = "UPDATE Users set Name = ?, WHERE ID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, user.getName());
            preparedStmt.setInt(2, user.getId());
            preparedStmt.executeUpdate();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }

    }

    //big note to self. Make them check ifString exists by calling get user
    public User createUser(String name) throws IOException {

        String sql = "INSERT INTO Users(ID, Name) VALUES (?, ?)";
        int Id = -1;
        try (Connection con = ds.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            Id = getNextAvailableUserID();
            ps.setInt(1, Id);
            ps.setString(2, name);
            ps.addBatch();

            ps.executeBatch();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
        return new User(Id, name);
    }

    private int getNextAvailableUserID() throws IOException {
        int ID = -1;
        try (Connection con = ds.getConnection()) {

            String sqlStatement = "SELECT max(ID) FROM Users";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                ID = rs.getInt(1);
            }
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }

        return ID + 1;

    }

}
