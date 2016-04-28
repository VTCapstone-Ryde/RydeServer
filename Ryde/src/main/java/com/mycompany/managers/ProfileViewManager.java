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
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "profileViewManager")
@SessionScoped
/**
 *
 * @author Balci
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

    public TimeslotTable getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(TimeslotTable selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    public String logout() {
        return "Login?faces-redirect=true";
    }
    
    public String getPhoneNumber() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getPhoneNumber();
    }
    
    public void setPhoneNumber(String phoneNumber) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        user.setPhoneNumber(phoneNumber);
        
        userFacade.edit(user);
    }
    
    public String getCarMake() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getCarMake();
    }
    
    public void setCarMake(String carMake) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        user.setCarMake(carMake);
        
        userFacade.edit(user);
    }
    
    public String getCarModel() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getCarModel();
    }
    
    public void setCarModel(String carModel) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        user.setCarModel(carModel);
        
        userFacade.edit(user);
    }
    
    public String getCarColor() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")).getCarColor();
    }
    
    public void setCarColor(String carColor) {
        UserTable user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        
        user.setCarColor(carColor);
        
        userFacade.edit(user);
    }
}
