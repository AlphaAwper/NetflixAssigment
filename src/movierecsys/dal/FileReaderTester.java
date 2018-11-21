/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.IOException;
<<<<<<< HEAD
import java.io.RandomAccessFile;
import java.util.List;
import movierecsys.be.Rating;
=======
>>>>>>> SQL implementation

/**
 *
 * @author pgn
 */
public class FileReaderTester {

    /**
     * Example method. This is the code I used to create the users.txt files.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
<<<<<<< HEAD
        RatingDAO ratingDao = new RatingDAO();

        List<Rating> ratings = ratingDao.getAllRatings();
        for (Rating rating : ratings) {
            System.out.println("R: " + rating.getMovie() + "," + rating.getUser() + "," + rating.getRating()); //Take a coffee break now...
        }

    }

    public static void createRafFriendlyRatingsFile() throws IOException {
        String target = "data/user_ratings";
        RatingDAO ratingDao = new RatingDAO();
        List<Rating> all = ratingDao.getAllRatings();

        try (RandomAccessFile raf = new RandomAccessFile(target, "rw")) {
            for (Rating rating : all) {
                raf.writeInt(rating.getMovie());
                raf.writeInt(rating.getUser());
                raf.writeInt(rating.getRating());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
=======
        DatabaseMigration mig = new DatabaseMigration();
        mig.transferMovies();
       // mig.transferRatings();
       // mig.transferUsers();
>>>>>>> SQL implementation
    }

}
