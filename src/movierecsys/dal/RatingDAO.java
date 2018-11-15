/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class RatingDAO {

    private static final String RATING_SOURCE = "data/ratings.txt";

    private static final int RECORD_SIZE = Integer.BYTES * 3;

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
            Path path = new File(RATING_SOURCE).toPath();
            try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.SYNC, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
                bw.newLine();
                bw.write(rating.getMovie() + "," + rating.getUser() + "," + rating.getRating());
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
        try (RandomAccessFile raf = new RandomAccessFile(RATING_SOURCE, "rw")) {
            long totalRatings = raf.length();
            long low = 0;
            long high = ((totalRatings - 1) / RECORD_SIZE) * RECORD_SIZE;
            while (high >= low) //Binary search of movie ID
            {
                long pos = (((high + low) / 2) / RECORD_SIZE) * RECORD_SIZE;
                raf.seek(pos);
                int movId = raf.readInt();
                int userId = raf.readInt();

                if (rating.getMovie() < movId) //We did not find the movie.
                {
                    high = pos - RECORD_SIZE; //We half our problem size to the upper half.
                } else if (rating.getMovie() > movId) //We did not find the movie.
                {
                    low = pos + RECORD_SIZE; //We half our problem size (Just the lower half)
                } else //We found a movie match, not to search for the user:
                {
                    if (rating.getUser() < userId) //Again we half our problem size
                    {
                        high = pos - RECORD_SIZE;
                    } else if (rating.getUser() > userId) //Another half sized problem
                    {
                        low = pos + RECORD_SIZE;
                    } else //Last option, we found the right row:
                    {
                        raf.write(rating.getRating()); //Remember the to reads at line 60,61. They positioned the filepointer just at the ratings part of the current record.
                        return; //We return from the method. We are done here. The try with resources will close the connection to the file.
                    }
                }
            }
        }
        throw new IllegalArgumentException("Rating not found in file, can't update!"); //If we reach this point we have been searching for a non-present rating.
    }

    /**
     * Removes the given rating.
     *
     * @param rating
     */
    public void deleteRating(Rating rating) {
        //TODO Delete rating
    }

    /**
     * Gets all ratings from all users.
     *
     * @return List of all ratings.
     */
    public List<Rating> getAllRatings() throws IOException {
        List<Rating> allRatings = new ArrayList<>();
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
    }

    /**
     * Get all ratings from a specific user.
     *
     * @param user The user
     * @return The list of ratings.
     */
    public List<Rating> getRatings(User user) {
        //TODO Get user ratings.
        return null;
    }

    private Rating getRatingFromLine(String line) throws NumberFormatException {
        String[] cols = line.split(",");
        int movId = Integer.parseInt(cols[0]);
        int userId = Integer.parseInt(cols[1]);
        int rating = Integer.parseInt(cols[2]);
        return new Rating(movId, userId, rating);
    }

}
