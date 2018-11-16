/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.generalRating;

/**
 *
 * @author pgn
 */
public class MovieRecommender {

    /**
     * Returns a list of movie recommendations based on the highest total
     * recommendations.
     *
     * @param allRatings List of all users ratings.
     * @return Sorted list of movies recommended to the caller. Sorted in
     * descending order.
     */
    public List<Movie> highAverageRecommendations(List<Rating> allRatings, List<Movie> allMovies) {
        List<generalRating> endResult = new ArrayList<>();
        List<Movie> sentBackMovies = new ArrayList<>();
        List<Rating> allUserRatings = allRatings;

        int currentMovie = 0;
        double newNumber = 0;
        int divisionNumb = 0;

        for (Rating allUserRating : allUserRatings) {
            if (allUserRating.getMovie() == currentMovie) {
                newNumber = newNumber + allUserRating.getRating();
                divisionNumb++;
            } else {
                if (currentMovie != 0) {
                    newNumber = Math.round((newNumber / divisionNumb) * 100.0) / 100.0;
                    endResult.add(new generalRating(allUserRating.getMovie(), newNumber));
                    newNumber = 0;
                    divisionNumb = 0;
                    currentMovie = allUserRating.getMovie();
                } else {
                    currentMovie = allUserRating.getMovie();
                    newNumber = newNumber + allUserRating.getRating();
                    divisionNumb++;
                }
            }
        }
        Collections.sort(endResult, (generalRating o1, generalRating o2) -> Double.compare(o2.getRating(), o1.getRating()));
        for (generalRating rating : endResult) {
            for (Movie movie : allMovies) {
                if (rating.getMovie() == movie.getId()) {
                    sentBackMovies.add(movie);
                    break;
                }
            }
        }
        return sentBackMovies;
    }

    /**
     * Returns a list of movie recommendations based on weighted
     * recommendations. Excluding already rated movies from the list of results.
     *
     * @param allRatings List of all users ratings.
     * @param excludeRatings List of Ratings (aka. movies) to exclude.
     * @return Sorted list of movies recommended to the caller. Sorted in
     * descending order.
     */
    public List<Movie> weightedRecommendations(List<Rating> allRatings, List<Rating> allRatings0, List<Movie> allMovies) {
        List<generalRating> endResult = new ArrayList<>();
        List<Movie> sentBackMovies = new ArrayList<>();
        List<Rating> allUserRatings = allRatings;

        int currentMovie = 0;
        double newNumber = 0;
        int divisionNumb = 0;

        for (Rating allUserRating : allUserRatings) {
            if (allUserRating.getMovie() == currentMovie) {
                newNumber = newNumber + allUserRating.getRating();
                divisionNumb++;
            } else {
                if (currentMovie != 0) {
                    newNumber = Math.round((newNumber / divisionNumb) * 100.0) / 100.0;
                    endResult.add(new generalRating(allUserRating.getMovie(), newNumber));
                    newNumber = 0;
                    divisionNumb = 0;
                    currentMovie = allUserRating.getMovie();
                } else {
                    currentMovie = allUserRating.getMovie();
                    newNumber = newNumber + allUserRating.getRating();
                    divisionNumb++;
                }
            }
        }
        boolean isFound = false;
        Collections.sort(endResult, (generalRating o1, generalRating o2) -> Double.compare(o2.getRating(), o1.getRating()));
        for (generalRating rating : endResult) {
            for (Movie movie : allMovies) {
                if (rating.getMovie() == movie.getId()) {
                    for (Rating ratingas : allRatings0) {
                        if (ratingas.getMovie() == rating.getMovie()) {
                            isFound = true;
                        }
                    }
                    if (!isFound) {
                        sentBackMovies.add(movie);
                    }
                    isFound = false;
                    break;
                }
            }
        }

        return sentBackMovies;
    }
}
