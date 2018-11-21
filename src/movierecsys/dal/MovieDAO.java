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
import movierecsys.be.Movie;

/**
 *
 * @author pgn
 */
public class MovieDAO {

<<<<<<< HEAD
    private static final String MOVIE_SOURCE = "data/movie_titles.txt";
=======
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
>>>>>>> SQL implementation

    /**
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     * @throws java.io.IOException
     */
    public List<Movie> getAllMovies() throws IOException {
        List<Movie> allMovies = new ArrayList<>();
<<<<<<< HEAD
        String source = "data/movie_titles.txt";
        File file = new File(source);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) //Using a try with resources!
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        Movie mov = stringArrayToMovie(line);
                        allMovies.add(mov);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        return allMovies;
    }

    /**
     * Reads a movie from the comma separated line.
     *
     * @param line the comma separated line.
     * @return The representing Movie object.
     * @throws NumberFormatException
     */
    private Movie stringArrayToMovie(String line) {
        String[] arrMovie = line.split(",");

        int id = Integer.parseInt(arrMovie[0]);
        int year = Integer.parseInt(arrMovie[1]);
        String title = arrMovie[2];
        // Add if commas in title, includes the rest of the string
        for (int i = 3; i < arrMovie.length; i++) {
            title += "," + arrMovie[i];
        }
        Movie mov = new Movie(id, year, title);
        return mov;
=======

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
>>>>>>> SQL implementation
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
<<<<<<< HEAD
        Path path = new File(MOVIE_SOURCE).toPath();
        int id = -1;
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.SYNC, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
            id = getNextAvailableMovieID();
            bw.newLine();
            bw.write(id + "," + releaseYear + "," + title);
        }
        Movie sendMovie = new Movie(id, releaseYear, title);
        updateMovie(sendMovie);
=======
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
>>>>>>> SQL implementation
        return sendMovie;
    }

    /**
     * Examines all stored movies and returns the next available highest ID.
     *
     * @return
     * @throws IOException
     */
    private int getNextAvailableMovieID() throws IOException {
<<<<<<< HEAD
        List<Movie> allMovies = getAllMovies();
        int newhighID = 0;
        for (Movie allMovie : allMovies) {
            if (allMovie.getId() != newhighID + 1) {
                break;
            }
            newhighID++;
        }
        return newhighID + 1;
=======
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
>>>>>>> SQL implementation
    }

    /**
     * Deletes a movie from the persistence storage.
     *
     * @param movie The movie to delete.
     */
    public void deleteMovie(Movie movie) throws IOException {
<<<<<<< HEAD
        String currentLine;
        File inputFile = new File(MOVIE_SOURCE);
        File tempFile = new File("data/tempMovies.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String s1 = Integer.toString(movie.getId());

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

            String query = "DELETE from Movies WHERE ID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, movie.getId());

            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
>>>>>>> SQL implementation

    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     */
    public void updateMovie(Movie movie) throws IOException {
<<<<<<< HEAD
        File tmp = new File("data/tmp_movies.txt");
        List<Movie> allMovies = getAllMovies();
        allMovies.removeIf((Movie t) -> t.getId() == movie.getId());
        allMovies.add(movie);
        Collections.sort(allMovies, (Movie o1, Movie o2) -> Integer.compare(o1.getId(), o2.getId()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmp))) {
            for (Movie mov : allMovies) {
                bw.write(mov.getId() + "," + mov.getYear() + "," + mov.getTitle());
                bw.newLine();
            }
        }
        Files.copy(tmp.toPath(), new File(MOVIE_SOURCE).toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(tmp.toPath());
=======
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
>>>>>>> SQL implementation
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
