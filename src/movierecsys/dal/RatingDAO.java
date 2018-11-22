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
import java.util.List;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class RatingDAO {

    SQLServerDataSource ds;

    public RatingDAO() throws IOException {
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
     * Persists the given rating.
     *
     * @param rating the rating to persist.
     */
    //I should check if the rating exists really . If it does , just update it.
    public void createRating(Rating rating) throws IOException {
        List<Rating> temp = getAllRatings();
        boolean isFound = false;
        for (Rating rating1 : temp) {
            if (rating1.getMovie() == rating.getMovie() && rating1.getUser() == rating.getUser()) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            updateRating(rating);
        } else {
            String sql = "INSERT INTO Ratings(MovieID, UserID, Rating) VALUES (?, ?, ?)";
            try (Connection con = ds.getConnection()) {

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, rating.getMovie());
                ps.setInt(2, rating.getUser());
                ps.setInt(3, rating.getRating());
                ps.addBatch();

                ps.executeBatch();
            } catch (SQLServerException ex) {
                System.out.println(ex);

            } catch (SQLException ex) {
                System.out.println(ex);

            }
        }
    }

    /**
     * Updates the rating to reflect the given object. Assumes that the source
     * file is in order by movie ID, then User ID..
     *
     * @param rating The updated rating to persist.
     * @throws java.io.IOException
     */
    public void updateRating(Rating rating) throws IOException {

        try (Connection con = ds.getConnection()) {
            String query = "UPDATE Ratings set Rating = ? WHERE MovieID = ? AND UserID = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, rating.getRating());
            ps.setInt(2, rating.getMovie());
            ps.setInt(3, rating.getUser());
            ps.executeUpdate();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }

    }

    /**
     * Removes the ratings for the movie
     *
     * @param rating
     */
    public void deleteRating(int IdToDelete) throws IOException {
        try (Connection con = ds.getConnection()) {

            String query = "DELETE from Ratings WHERE MovieID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, IdToDelete);

            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }

    /**
     * Gets all ratings from all users.
     *
     * @return List of all ratings.
     */
    public List<Rating> getAllRatings() throws IOException {
        List<Rating> allRatings = new ArrayList<>();

        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Ratings";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                int MovieID = rs.getInt("MovieID");
                int UserID = rs.getInt("UserID");
                int Ratings = rs.getInt("Rating");
                Rating ra = new Rating(MovieID, UserID, Ratings);
                allRatings.add(ra);
            }
            return allRatings;

        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /**
     * Get all ratings from a specific user.
     *
     * @param user The user
     * @return The list of ratings.
     */
    public List<Rating> getRatings(User user) throws IOException {
        List<Rating> allUserRatings = new ArrayList<>();
        List<Rating> allRatings = getAllRatings();
        for (Rating allRating : allRatings) {
            if (allRating.getUser() == user.getId()) {
                allUserRatings.add(allRating);
            }
        }
        return allUserRatings;
    }

    public Rating getSingleRating(User newUser, Movie selectedItem) throws IOException {
        List<Rating> allRatings = getAllRatings();
        Rating userRating = null;
        for (Rating allRating : allRatings) {
            if (allRating.getMovie() == selectedItem.getId() && allRating.getUser() == newUser.getId()) {
                userRating = allRating;
                break;
            }
        }
        return userRating;
    }

}
