/*
 * Created by Ryde on 2016.02.14  * 
 * Copyright © 2016 Ryde. All rights reserved. * 
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
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "profileViewManager")
@SessionScoped
/**
 *
 * @author Ryde
 */
public class ProfileViewManager implements Serializable {

    private TimeslotTable selectedTimeSlot = new TimeslotTable();

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private UserTableFacade userFacade;
    @EJB
    private GroupUserFacade groupUserFacade;
    @EJB
    private TimeslotUserFacade timeSlotUserFacade;

    public ProfileViewManager() {

    }

    /**
     * Returns the profile page string
     * @return profile xhtml page
     */
    public String viewProfile() {
        return "Profile";
    }

    /**
     * gets the logged in user
     * @return the user that is logged in
     */
    public UserTable getLoggedInUser() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }

    /**
     * gets the groups that the logged in user is in
     * @return the list of groups
     */
    public List<GroupTable> getUserGroups() {
        return groupUserFacade.findGroupsForUser(getLoggedInUser().getId());
    }

    /**
     * gets the timeslots the logged in user is in
     * @return a list of the timeslots
     */
    public List<TimeslotTable> getUserTimeSlots() {
        return timeSlotUserFacade.findTimeslotsForUser(getLoggedInUser().getId());
    }

    /**
     * gets the selected timeslot
     * @return the selected timeslot
     */
    public TimeslotTable getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    /**
     * sets the selected timeslot
     * @param selectedTimeSlot the timeslot to select
     */
    public void setSelectedTimeSlot(TimeslotTable selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    /**
     * logs out the current logged in user
     * @return redirect string to be used by the XHTML
     */
    public String logout() {
        return "Login?faces-redirect=true";
    }
    
    /**
     * gets the phone number of the logged in user
     * @return the phone number
     */
    public String getPhoneNumber() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getPhoneNumber();
    }
    
    /**
     * sets the phone number of the logged in user
     * @param phoneNumber string phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        if (phoneNumber.trim().isEmpty()) {
            phoneNumber = "Enter a Phone Number.";
        }
        
        user.setPhoneNumber(phoneNumber);
        
        userFacade.edit(user);
    }
    
    /**
     * gets the car make of the logged in user
     * @return the car make
     */
    public String getCarMake() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getCarMake();
    }
    
    /**
     * sets the car make of the logged in user
     * @param carMake string representing the car make
     */
    public void setCarMake(String carMake) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        if (carMake.trim().isEmpty()) {
            carMake = "Enter a Car Make.";
        }
        user.setCarMake(carMake);
        
        userFacade.edit(user);
    }
    
    /**
     * gets the car model of the logged in user
     * @return the car model
     */
    public String getCarModel() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getCarModel();
    }
    
    /**
     * sets the car model of the logged in user
     * @param carModel string representing the car model
     */
    public void setCarModel(String carModel) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        if (carModel.trim().isEmpty()) {
            carModel = "Enter a Car Model.";
        }
        
        user.setCarModel(carModel);
        
        userFacade.edit(user);
    }
    
    /**
     * gets the car color of the logged in user
     * @return the car color
     */
    public String getCarColor() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getCarColor();
    }
    
    /**
     * sets the car color of the logged in user
     * @param carColor the color of the car
     */
    public void setCarColor(String carColor) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        if (carColor.trim().isEmpty()) {
            carColor = "Enter a Car Color.";
        }
        
        user.setCarColor(carColor);
        
        userFacade.edit(user);
    }
}
