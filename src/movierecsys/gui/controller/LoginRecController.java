/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.stage.Stage;
import movierecsys.be.User;
import movierecsys.bll.exception.MovieRecSysException;
import movierecsys.gui.model.UserModel;

/**
 * FXML Controller class
 *
 * @author nedas
 */
public class LoginRecController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private ListView<User> lstUsers;
    @FXML
    private TextField userName;

    private UserModel userModel;
    @FXML
    private Label errorLabel;

    public LoginRecController() throws MovieRecSysException {
        userModel = new UserModel();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstUsers.setItems(userModel.getUsers());
    }

    @FXML
    private void loginUser(ActionEvent event) throws IOException {
        if (lstUsers.getSelectionModel().getSelectedItem() != null) {
            logInUser(lstUsers.getSelectionModel().getSelectedItem());
        } else {
            errorLabel.setText("Error : please select a user to login");
        }

    }

    @FXML
    private void createUser(ActionEvent event) throws IOException {
        if (userName.getText() != null && !userName.getText().isEmpty()) {
            User newUser = userModel.createUser(userName.getText());
            logInUser(newUser);
        } else {
            errorLabel.setText("Error :  namie field cannot be empty");
        }
    }

    private void logInUser(User selectedUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/movierecsys/gui/view/MovieRecView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        fxmlLoader.<MovieRecController>getController().setInfo(selectedUser);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();
    }

}
