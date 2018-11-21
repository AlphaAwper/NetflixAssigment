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
import java.util.ArrayList;
import java.util.Collections;
=======
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
>>>>>>> SQL implementation
import java.util.List;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class RatingDAO {

<<<<<<< HEAD
    private static final String RATING_SOURCE = "data/ratings.txt";

    private static final int RECORD_SIZE = Integer.BYTES * 3;
=======
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
>>>>>>> SQL implementation

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
<<<<<<< HEAD
            Path path = new File(RATING_SOURCE).toPath();
            try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.SYNC, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
                bw.newLine();
                bw.write(rating.getMovie() + "," + rating.getUser() + "," + rating.getRating());
                bw.close();
                updateRating(rating);
=======
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

>>>>>>> SQL implementation
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
<<<<<<< HEAD
        File tmp = new File("data/tmp_ratings.txt");
        List<Rating> allRatings = getAllRatings();
        allRatings.removeIf((Rating t) -> t.getMovie() == rating.getMovie());
        allRatings.add(rating);
        Collections.sort(allRatings, (Rating o1, Rating o2) -> Integer.compare(o1.getMovie(), o2.getMovie()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmp))) {
            for (Rating rat : allRatings) {
                bw.write(rat.getMovie() + "," + rat.getUser() + "," + rat.getRating());
                bw.newLine();
            }
        }
        Files.copy(tmp.toPath(), new File(RATING_SOURCE).toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(tmp.toPath());
=======
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
>>>>>>> SQL implementation
    }

    /**
     * Removes the ratings for the movie
     *
     * @param rating
     */
    public void deleteRating(int IdToDelete) throws IOException {
<<<<<<< HEAD
        String currentLine;
        File inputFile = new File(RATING_SOURCE);
        File tempFile = new File("data/tempRatings.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String s1 = Integer.toString(IdToDelete);

        while ((currentLine = reader.readLine()) != null) {
            if (!(currentLine.split(",")[0]).contains(s1)) {
                writer.write(currentLine);
                writer.newLine();
            }
        }

        writer.close();
        reader.close();

        inputFile.delete();
        tempFile.renameTo(inputFile);
=======

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
>>>>>>> SQL implementation
    }

    /**
     * Gets all ratings from all users.
     *
     * @return List of all ratings.
     */
    public List<Rating> getAllRatings() throws IOException {
        List<Rating> allRatings = new ArrayList<>();
<<<<<<< HEAD
        File file = new File(RATING_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) //Using a try with resources!
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        Rating us = stringArrayToRating(line);
                        allRatings.add(us);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        return allRatings;
    }

    private Rating stringArrayToRating(String line) {
        String[] arrRating = line.split(",");

        int movieId = Integer.parseInt(arrRating[0]);
        int userId = Integer.parseInt(arrRating[1]);
        int rating = Integer.parseInt(arrRating[2]);
        Rating rat = new Rating(movieId, userId, rating);
        return rat;
=======
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
>>>>>>> SQL implementation
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

<<<<<<< HEAD
    private Rating getRatingFromLine(String line) throws NumberFormatException {
        String[] cols = line.split(",");
        int movId = Integer.parseInt(cols[0]);
        int userId = Integer.parseInt(cols[1]);
        int rating = Integer.parseInt(cols[2]);
        return new Rating(movId, userId, rating);
    }

=======
>>>>>>> SQL implementation
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
