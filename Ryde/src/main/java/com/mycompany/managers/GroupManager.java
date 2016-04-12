/*
 * Created by Jared Deiner on 2016.03.15  * 
 * Copyright © 2016 Jared Deiner. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.Friend;
import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupUser;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTableFacade;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.GroupUserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author archer
 */
@Named(value = "groupManager")
@SessionScoped
/**
 * This class defines the constant variable (static) to be unchanged and used
 * though out the different components of the project as the web app is running
 *
 * @author Deiner
 */
public class GroupManager implements Serializable {

    private List<Friend> selectedFriends = new ArrayList<Friend>();
    private List<Friend> friends = new ArrayList<Friend>();
    private String name;
    private String description;
    private GroupTable selectedGroup = new GroupTable();

    private GroupTableFacade groupFacade = new GroupTableFacade();
    private GroupUserFacade groupUserFacade = new GroupUserFacade();
    private GroupTimeslotFacade groupTimeslotFacade = new GroupTimeslotFacade();
    private String statusMessage;

    public List<Friend> getFriends() {
        friends.clear();
        selectedFriends.clear();
        Friend friend1 = new Friend(2, "Jim", "Jim Jackson");
        Friend friend2 = new Friend(3, "Jake", "Jake Jundson");
        friends.add(friend1);
        friends.add(friend2);
        return friends;
    }

    public List<Friend> getSelectedFriends() {
        return selectedFriends;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setSelectedFriends(List<Friend> selectedFriends) {
        this.selectedFriends = selectedFriends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSelectedGroup(GroupTable selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public GroupTable getSelectedGroup() {
        return selectedGroup;
    }

    public List<UserTable> getGroupUsers() {
        return groupUserFacade.findUsersForGroup(getSelectedGroup().getId());
    }

    public List<UserTable> getGroupAdmins() {
        return groupUserFacade.findAdminsForGroup(getSelectedGroup().getId());
    }

    public List<TimeslotTable> getGroupTimeslots() {
        return groupTimeslotFacade.findTimeslotsForGroup(getSelectedGroup().getId());
    }

    public void onGroupRowSelect() {
        //System.out.println(selectedGroup);
        ConfigurableNavigationHandler configurableNavigationHandler
                = (ConfigurableNavigationHandler) FacesContext.
                getCurrentInstance().getApplication().getNavigationHandler();

        configurableNavigationHandler.performNavigation("ViewGroup?faces-redirect=true");

    }

    public String createGroup() {

        try {
            GroupTable group = new GroupTable();
            group.setTitle(name);
            group.setDescription(description);
            group.setDirectoryPath("none");
            groupFacade.create(group);
            System.out.println("Created Group");

        } catch (EJBException e) {
            statusMessage = "Something went wrong while creating your account!";
            return "";
        }
        return "";

    }

}
