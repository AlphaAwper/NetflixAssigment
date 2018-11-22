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
import movierecsys.be.Movie;

/**
 *
 * @author pgn
 */
public class MovieDAO {

    SQLServerDataSource ds;

    public MovieDAO() throws IOException {

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
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     * @throws java.io.IOException
     */
    public List<Movie> getAllMovies() throws IOException {
        List<Movie> allMovies = new ArrayList<>();

        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Movies";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                int Id = rs.getInt("ID");
                int year = rs.getInt("Year");
                String title = rs.getString("Title");
                Movie mov = new Movie(Id, year, title);
                allMovies.add(mov);
            }
            return allMovies;

        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /**
     * Creates a movie in the persistence storage.
     *
     * @param releaseYear The release year of the movie
     * @param title The title of the movie
     * @return The object representation of the movie added to the persistence
     * storage.
     */
    public Movie createMovie(int releaseYear, String title) throws IOException {
        String sql = "INSERT INTO Movies(ID, Year, Title) VALUES (?, ?, ?)";
        int Id = -1;
        try (Connection con = ds.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            Id = getNextAvailableMovieID();
            ps.setInt(1, Id);
            ps.setInt(2, releaseYear);
            ps.setString(3, title);
            ps.addBatch();

            ps.executeBatch();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
        Movie sendMovie = new Movie(Id, releaseYear, title);
        return sendMovie;
    }

    /**
     * Examines all stored movies and returns the next available highest ID.
     *
     * @return
     * @throws IOException
     */
    private int getNextAvailableMovieID() throws IOException {
        int ID = -1;
        try (Connection con = ds.getConnection()) {

            String sqlStatement = "SELECT max(ID) FROM Movies";
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

    /**
     * Deletes a movie from the persistence storage.
     *
     * @param movie The movie to delete.
     */
    public void deleteMovie(Movie movie) throws IOException {

        try (Connection con = ds.getConnection()) {

            String query = "DELETE from Movies WHERE ID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, movie.getId());

            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }

    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     */
    public void updateMovie(Movie movie) throws IOException {
        try (Connection con = ds.getConnection()) {

            String query = "UPDATE Movies set Year = ?, Title = ?, WHERE ID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, movie.getYear());
            preparedStmt.setString(2, movie.getTitle());
            preparedStmt.setInt(3, movie.getId());
            preparedStmt.executeUpdate();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }

    }

    /**
     * Gets a the movie with the given ID.
     *
     * @param id ID of the movie.
     * @return A Movie object.
     */
    public Movie getMovie(int id) throws IOException {
        List<Movie> all = getAllMovies();
        int index = Collections.binarySearch(all, new Movie(id, 0, ""), new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        if (index >= 0) {
            return all.get(index);
        } else {
            throw new IllegalArgumentException("No movie with ID: " + id + " is found.");
        }
    }

}
