/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.be;

/**
 *
 * @author nedas
 */
public class generalRating {

    private final int movieId;
    private double rating;

    /**
     * Constructs a new rating.
     *
     * @param movie The movie being rated.
     * @param user The rating user.
     * @param rating The value of the rating. Only the constants of the Rating
     * class are allowed values.
     */
    public generalRating(int movieId, double rating) {
        this.movieId = movieId;
        this.rating = rating;
    }

    /**
     * Sets the rating to a new value. Only the constants of the Rating class
     * are allowed values.
     *
     * @param rating The rating to set.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Gets the movie being rated.
     *
     * @return A movie
     */
    public int getMovie() {
        return movieId;
    }

    /**
     * Gets the rating value.
     *
     * @return An integer.
     */
    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Movie id : " + movieId + " Rating  : " + rating;
    }
}
