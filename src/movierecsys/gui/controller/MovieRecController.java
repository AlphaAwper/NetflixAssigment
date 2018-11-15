/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import movierecsys.be.Movie;
import movierecsys.be.User;
import movierecsys.bll.exception.MovieRecSysException;
import movierecsys.gui.model.MovieModel;

/**
 *
 * @author pgn
 */
public class MovieRecController implements Initializable {

    /**
     * The TextField containing the URL of the targeted website.
     */
    @FXML
    private TextField txtMovieSearcjh;

    /**
     * The TextField containing the query word.
     */
    @FXML
    private ListView<Movie> lstMovies;
    @FXML
    private Label userID;
    @FXML
    private Button loginButton;
    @FXML
    private TextField movieTitle;
    @FXML
    private TextField movieDate;
    @FXML
    private Label rratingTitle;
    @FXML
    private ToggleGroup rating;
    @FXML
    private Button addButton;
    @FXML
    private Button sumitButton;
    @FXML
    private Button deleteButton;

    private MovieModel movieModel;
    private ObservableList<Movie> newMovies;
    private List<Movie> foundMovieList;
    private User newUser;
    @FXML
    private Label errorLabel;

    public MovieRecController() {
        try {
            movieModel = new MovieModel();
        } catch (MovieRecSysException ex) {
            displayError(ex);
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            lstMovies.setItems(movieModel.getMovies());
        } catch (MovieRecSysException ex) {
        }
    }

    /**
     * Displays errormessages to the user.
     *
     * @param ex The Exception
     */
    private void displayError(Exception ex) {
        //TODO Display error properly
        System.out.println(ex.getMessage());
        ex.printStackTrace();
    }

    @FXML
    private void searchMovie(KeyEvent event) throws MovieRecSysException {
        lstMovies.getItems().clear();
        if (txtMovieSearcjh.getText() == null || txtMovieSearcjh.getText().isEmpty()) {
            lstMovies.setItems(movieModel.getMovies());
        } else {
            newMovies = FXCollections.observableArrayList();
            foundMovieList = new ArrayList<>();
            foundMovieList = movieModel.search(movieModel.getMovies(), txtMovieSearcjh.getText());
            if (foundMovieList != null) {
                newMovies.addAll(foundMovieList);
                lstMovies.setItems(newMovies);
            }

        }
    }

    @FXML
    private void loginUser(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/movierecsys/gui/view/loginView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    void setInfo(User selectedUser) {
        addButton.setDisable(false);
        sumitButton.setDisable(false);
        deleteButton.setDisable(false);
        newUser = selectedUser;
        userID.setVisible(true);
        userID.setText("Your username : " + selectedUser.getName());
    }

    @FXML
    private void addMovieToList(ActionEvent event) {
        if (movieDate.getText() != null && !movieDate.getText().isEmpty() && movieTitle.getText() != null && !movieTitle.getText().isEmpty()) {
            int newInt = Integer.parseInt(movieDate.getText());
            movieModel.createMovie(newInt, movieTitle.getText());
        } else {
            errorLabel.setText("Error : please check if you entered data to both fields");
        }
    }

    @FXML
    private void submitUserRating(ActionEvent event) {
        ObservableList<Toggle> temp = rating.getToggles();
        int realRating = -1;
        int x = 0;
        for (Toggle toggle : temp) {
            if (toggle.isSelected()) break;
            x++;
        }
        switch (x) {
            case 0:
                realRating = -5;
                break;
            case 1:
                realRating = -3;
                break;
            case 2:
                realRating = 0;
                break;
            case 3:
                realRating = 3;
                break;
            case 4:
                realRating = 5;
                break;
        }
        movieModel.submitRating(lstMovies.getSelectionModel().getSelectedItem(), newUser, realRating);
    }

    @FXML
    private void deleteSelectedMovie(ActionEvent event) {
        if (lstMovies.getSelectionModel().getSelectedItem() != null) {
            movieModel.deleteMovie(lstMovies.getSelectionModel().getSelectedItem());
        } else {
            errorLabel.setText("Error : please select a movie to remove");
        }
    }

}