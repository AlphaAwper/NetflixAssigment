/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author nedas
 */
public class DatabaseMigration {

    SQLServerDataSource ds;

    public DatabaseMigration() throws IOException {
        this.ds = new SQLServerDataSource();
        ConnectionDAO connectionInfo = new ConnectionDAO();
        List<String> infoList = connectionInfo.getDatabaseInfo();
        ds.setDatabaseName(infoList.get(0));
        ds.setUser(infoList.get(1));
        ds.setPassword(infoList.get(2));
        ds.setPortNumber(Integer.parseInt(infoList.get(3)));
        ds.setServerName(infoList.get(4));
    }

    public void transferMovies() throws IOException {
        String source = "data/movie_titles.txt";

        File file = new File(source);
        List<Movie> MovieList = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) //Using a try with resources!
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        //read the data 
                        String[] arrMovie = line.split(",");

                        int id = Integer.parseInt(arrMovie[0]);
                        int year = Integer.parseInt(arrMovie[1]);
                        String title = arrMovie[2];
                        // Add if commas in title, includes the rest of the string
                        for (int i = 3; i < arrMovie.length; i++) {
                            title += "," + arrMovie[i];
                        }
                        MovieList.add(new Movie(id, year, title));

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }

            String sql = "INSERT INTO Movies(ID, Year, Title) VALUES (?, ?, ?)";
            try (Connection con = ds.getConnection()) {
                PreparedStatement ps = con.prepareStatement(sql);

                for (Movie movie : MovieList) {
                    ps.setInt(1, movie.getId());
                    ps.setInt(2, movie.getYear());
                    ps.setString(3, movie.getTitle());
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLServerException ex) {
                System.out.println(ex);

            } catch (SQLException ex) {
                System.out.println(ex);

            }
        }

    }

    public void transferUsers() throws IOException {
        String source = "data/users.txt";

        File file = new File(source);
        List<User> UserList = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) //Using a try with resources!
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        //read the data 
                        String[] arrUser = line.split(",");

                        int id = Integer.parseInt(arrUser[0]);
                        String name = arrUser[1];

                        UserList.add(new User(id, name));

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }

            String sql = "INSERT INTO Users(ID, Name) VALUES (?, ?)";
            try (Connection con = ds.getConnection()) {
                PreparedStatement ps = con.prepareStatement(sql);

                for (User usar : UserList) {
                    ps.setInt(1, usar.getId());
                    ps.setString(2, usar.getName());
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLServerException ex) {
                System.out.println(ex);

            } catch (SQLException ex) {
                System.out.println(ex);

            }
        }
    }

    public void transferRatings() throws IOException {
        String source = "data/ratings.txt";

        File file = new File(source);
        List<Rating> rat = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) //Using a try with resources!
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        //read the data 
                        String[] arrRating = line.split(",");

                        int movieId = Integer.parseInt(arrRating[0]);
                        int userId = Integer.parseInt(arrRating[1]);
                        int rating = Integer.parseInt(arrRating[2]);
                        rat.add(new Rating(movieId, userId, rating));

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }

            String sql = "INSERT INTO Ratings(MovieID, UserID, Rating) VALUES (?, ?, ?)";
            try (Connection con = ds.getConnection()) {
                PreparedStatement ps = con.prepareStatement(sql);

                for (Rating ra : rat) {
                    ps.setInt(1, ra.getMovie());
                    ps.setInt(2, ra.getUser());
                    ps.setInt(3, ra.getRating());
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLServerException ex) {
                System.out.println(ex);

            } catch (SQLException ex) {
                System.out.println(ex);

            }
        }
    }
}
