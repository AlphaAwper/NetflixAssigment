/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
=======
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
>>>>>>> SQL implementation
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

<<<<<<< HEAD
    private static final String USER_SOURCE = "data/users.txt";
=======
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
>>>>>>> SQL implementation

    /**
     * Gets a list of all known users.
     *
     * @return List of users.
     */
    public List<User> getAllUsers() throws IOException {
        List<User> allUsers = new ArrayList<>();
<<<<<<< HEAD
        File file = new File(USER_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) //Using a try with resources!
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        User us = stringArrayToUser(line);
                        allUsers.add(us);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        return allUsers;
    }

    private User stringArrayToUser(String line) {
        String[] arrUser = line.split(",");

        int id = Integer.parseInt(arrUser[0]);
        String name = arrUser[1];

        User us = new User(id, name);
        return us;
=======

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
>>>>>>> SQL implementation
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
<<<<<<< HEAD
        File tmp = new File("data/tmp_users.txt");
        List<User> allUsers = getAllUsers();
        allUsers.removeIf((User t) -> t.getId() == user.getId());
        allUsers.add(user);
        Collections.sort(allUsers, (User o1, User o2) -> Integer.compare(o1.getId(), o2.getId()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmp))) {
            for (User us : allUsers) {
                bw.write(us.getId() + "," + us.getName());
                bw.newLine();
            }
        }
        Files.copy(tmp.toPath(), new File(USER_SOURCE).toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(tmp.toPath());
=======
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
>>>>>>> SQL implementation
    }

    //big note to self. Make them check ifString exists by calling get user
    public User createUser(String name) throws IOException {
<<<<<<< HEAD
        Path path = new File(USER_SOURCE).toPath();
        int id = -1;
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.SYNC, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
            id = getNextAvailableUserID();
            bw.newLine();
            bw.write(id + "," + name);
        }
        User sendUser = new User(id, name);
        updateUser(sendUser);
        return sendUser;
    }

    private int getNextAvailableUserID() throws IOException {
        List<User> allUsers = getAllUsers();
        int newhighID = 0;
        for (User allUser : allUsers) {
            if (allUsers.get(newhighID).getId() != newhighID + 1) {
                break;
            }
            newhighID++;
        }
        return newhighID + 1;
=======
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
>>>>>>> SQL implementation
    }

}
