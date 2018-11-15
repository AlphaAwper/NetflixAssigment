/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.User;
import movierecsys.bll.MRSLogicFacade;
import movierecsys.bll.MRSManager;
import movierecsys.bll.exception.MovieRecSysException;

/**
 *
 * @author nedas
 */
public class UserModel {

    private ObservableList<User> users = FXCollections.observableArrayList();

    private MRSLogicFacade logiclayer;

    public UserModel() throws MovieRecSysException {
        users = FXCollections.observableArrayList();
        logiclayer = new MRSManager();
        users.addAll(logiclayer.getAllUsers());
    }

    /**
     * Gets a reference to the observable list of Movies.
     *
     * @return List of movies.
     */
    public ObservableList<User> getUsers() {
        users = FXCollections.observableArrayList();
        users.addAll(logiclayer.getAllUsers());
        return users;
    }

    public User createUser(String name) {
        User user = logiclayer.createNewUser(name);
        users.add(user);
        return user;
    }

    public User getUserById(int ID) {
        return logiclayer.getUserById(ID);
    }

}
