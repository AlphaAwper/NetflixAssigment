/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.util.ArrayList;
import java.util.List;
import movierecsys.be.Movie;

/**
 *
 * @author pgn
 */
public class MovieSearcher {

    private List<Movie> temp = new ArrayList<>();

    public List<Movie> search(List<Movie> searchBase, String query) {
        temp.clear();
        for (Movie movie : searchBase) {
            if (movie.getTitle().toLowerCase().startsWith(query.toLowerCase())) {
                temp.add(movie);
            }
        }
        return temp;
    }

}
