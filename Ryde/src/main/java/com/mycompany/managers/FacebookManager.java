/*
 * Created by Eric Leszcynski on 2016.04.19  * 
 * Copyright © 2016 Eric Leszcynski. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.UserTable;
import com.mycompany.session.UserTableFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Eric
 */
@Named(value = "facebookManager")
@SessionScoped
public class FacebookManager implements Serializable {
    
    private Boolean driverStatus;
    private String lastName;
    private String firstName;
    private String fbTok;
    private String fbId;
    private String phoneNumber;
    private String carMake;
    private String carModel;
    private String carColor;
    private String statusMessage;
        
    @EJB
    private UserTableFacade userFacade;
    
    public FacebookManager() {
    }

    public Boolean getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(Boolean driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFbTok() {
        return fbTok;
    }

    public void setFbTok(String fbTok) {
        this.fbTok = fbTok;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    public String createUser() {
        // Check to see if a user already exists with the username given.
        UserTable aUser = userFacade.findByToken(fbTok);

        if (aUser != null) {
            firstName = "";
            statusMessage = "User already exists!";
            return "";
        }

        if (statusMessage.isEmpty()) {
            try {
                UserTable user = new UserTable();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setDriverStatus(false);
                user.setFbTok(fbTok);
                user.setPhoneNumber(phoneNumber);
                userFacade.create(user);
            } catch (EJBException e) {
                firstName = "";
                statusMessage = "Something went wrong while creating your account!";
                return "";
            }
            initializeSessionMap();
            return "Home";
        }
        return "";
    }
    
    public void initializeSessionMap() {
        UserTable user = userFacade.findByName(getFirstName(), getLastName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("first_name", user.getFirstName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("last_name", user.getLastName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }
}
