/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.model;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.bll.MRSLogicFacade;
import movierecsys.bll.MRSManager;
import movierecsys.bll.exception.MovieRecSysException;

/**
 *
 * @author pgn
 */
public class MovieModel {

    private ObservableList<Movie> movies = FXCollections.observableArrayList();

    private MRSLogicFacade logiclayer;

    public MovieModel() throws MovieRecSysException, IOException {
        movies = FXCollections.observableArrayList();
        logiclayer = new MRSManager();
        movies.addAll(logiclayer.getAllMovies());
    }

    /**
     * Gets a reference to the observable list of Movies.
     *
     * @return List of movies.
     */
    public ObservableList<Movie> getMovies() throws MovieRecSysException {
        movies = FXCollections.observableArrayList();
        movies.addAll(logiclayer.getAllMovies());
        return movies;
    }

    public void createMovie(int year, String title) {
        Movie movie = logiclayer.createMovie(year, title);
        movies.add(movie);
    }

    public void deleteMovie(Movie movie) {
        logiclayer.deleteMovie(movie);
        movies.remove(movie);
    }

    public List<Movie> search(ObservableList<Movie> movies, String text) {
        return logiclayer.searchMovies(movies, text);
    }

    public void submitRating(Movie selectedItem, User newUser, int realRating) {
        logiclayer.rateMovie(selectedItem, newUser, realRating);
    }

    public List<Movie> getHigestRecommended() {
        return logiclayer.getAllTimeTopRatedMovies();
    }

    public List<Movie> getWeighted(User newUser) {
        return logiclayer.getMovieReccomendations(newUser);
    }

    public Rating getRatingForMovie(User newUser, Movie selectedItem) {
        return logiclayer.getMovieRecomendation(newUser, selectedItem);
    }
}
