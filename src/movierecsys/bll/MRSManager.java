/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.bll.exception.MovieRecSysException;
import movierecsys.dal.MovieDAO;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.RatingDAO;
import movierecsys.dal.UserDAO;

/**
 *
 * @author pgn
 */
public class MRSManager implements MRSLogicFacade {

    private final MovieDAO movieDAO;
    private final MovieSearcher movieSearch;
    private final UserDAO userData;
    private final RatingDAO ratingDAO;

    public MRSManager() {
        movieDAO = new MovieDAO();
        movieSearch = new MovieSearcher();
        userData = new UserDAO();
        ratingDAO = new RatingDAO();
    }

    @Override
    public List<Rating> getRecommendedMovies(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Movie> getAllTimeTopRatedMovies() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Movie> getMovieReccomendations(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Movie> searchMovies(ObservableList<Movie> searchBase, String query) {
        return movieSearch.search(searchBase, query);
    }

    @Override
    public Movie createMovie(int year, String title) {
        try {
            return movieDAO.createMovie(year, title);
        } catch (IOException ex) {
            System.out.println("Could not create movie . Reason : " + ex);
            return null;
        }
    }

    @Override
    public void updateMovie(Movie movie) {
        try {
            movieDAO.updateMovie(movie);
        } catch (IOException ex) {
            System.out.println("Could not update movie . Reason : " + ex);
        }
    }

    @Override
    public void deleteMovie(Movie movie) {
        try {
            movieDAO.deleteMovie(movie);
        } catch (IOException ex) {
            System.out.println("Could not delete movie . Reason : " + ex);
        }
    }

    @Override
    public void rateMovie(Movie movie, User user, int rating) {
        try {
            ratingDAO.createRating(new Rating(movie.getId(), user.getId(), rating));
        } catch (IOException ex) {
            System.out.println("Could not get rate movie . Reason : " + ex);
        }
    }

    @Override
    public User createNewUser(String name) {
        try {
            return userData.createUser(name);
        } catch (IOException ex) {
            System.out.println("Could not create user . Reason : " + ex);
            return null;
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            return userData.getUser(id);
        } catch (IOException ex) {
            System.out.println("Could not get user by ID . Reason : " + ex);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userData.getAllUsers();
        } catch (IOException ex) {
            System.out.println("Could not get all users . Reason : " + ex);
            return null;
        }
    }

    /**
     * Gets all movies.
     *
     * @return List of movies.
     * @throws MovieRecSysException
     */
    @Override
    public List<Movie> getAllMovies() throws MovieRecSysException {
        try {
            return movieDAO.getAllMovies();
        } catch (IOException ex) {
//            Logger.getLogger(MRSManager.class.getName()).log(Level.SEVERE, null, ex); You could log an exception
            throw new MovieRecSysException("Could not read all movies. Cause: " + ex.getMessage());
        }
    }

}
