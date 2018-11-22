/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.IOException;

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
        DatabaseMigration mig = new DatabaseMigration();
        mig.transferMovies();
        mig.transferRatings();
        mig.transferUsers();
    }

}
