/*
 * Created by Osman Balci on 2016.02.14  * 
 * Copyright © 2016 Osman Balci. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupUserFacade;
import com.mycompany.session.TimeslotUserFacade;
import com.mycompany.session.UserTableFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "profileViewManager")
@SessionScoped
/**
 *
 * @author Balci
 */
public class ProfileViewManager implements Serializable {

    // Instance Variable (Property)
    private UserTable user;

    private TimeslotTable selectedTimeSlot = new TimeslotTable();

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private UserTableFacade userFacade = new UserTableFacade();
    private GroupUserFacade groupUserFacade = new GroupUserFacade();
    private TimeslotUserFacade timeSlotUserFacade = new TimeslotUserFacade();

    public ProfileViewManager() {

    }

    public String viewProfile() {
        return "Profile";
    }

    public UserTable getLoggedInUser() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }

    public List<GroupTable> getUserGroups() {
        return groupUserFacade.findGroupsForUser(getLoggedInUser().getId());
    }

    public List<TimeslotTable> getUserTimeSlots() {
        return timeSlotUserFacade.findTimeslotsForUser(getLoggedInUser().getId());
    }

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }

    public TimeslotTable getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(TimeslotTable selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }
}
