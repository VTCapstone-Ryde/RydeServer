/*
 * Created by Osman Balci on 2016.02.14  * 
 * Copyright © 2016 Osman Balci. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.UserTable;
import com.mycompany.entity.GroupTable;
import com.mycompany.session.UserTableFacade;
import com.mycompany.session.GroupUserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

@Named(value = "accountManager")
@SessionScoped
/**
 *
 * @author Balci
 */
public class AccountManager implements Serializable {

    // Instance Variables (Properties)
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Boolean driverStatus;
    private String lastName;
    private String firstName;
    private String fbTok;
    private String fbId;
    private String phoneNumber;
    private String carMake;
    private String carModel;
    private String carColor;
    private int security_question;
    private String security_answer;
    private String statusMessage;

    private Map<String, Object> security_questions;

    private UserTable selected;
    private GroupTable selectedGroup;

    private List<String> events = new ArrayList();

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private UserTableFacade userFacade;
    private GroupUserFacade groupUserFacade;

    /**
     * Creates a new instance of AccountManager
     */
    public AccountManager() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(Boolean driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getLastName() {
        lastName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("last_name");
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        firstName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("first_name");
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

    public int getSecurity_question() {
        return security_question;
    }

    public void setSecurity_question(int security_question) {
        this.security_question = security_question;
    }

    public String getSecurity_answer() {
        return security_answer;
    }

    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }

    public UserTableFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserTableFacade userFacade) {
        this.userFacade = userFacade;
    }

    public Map<String, Object> getSecurity_questions() {
        if (security_questions == null) {
            security_questions = new LinkedHashMap<>();
            for (int i = 0; i < Constants.QUESTIONS.length; i++) {
                security_questions.put(Constants.QUESTIONS[i], i);
            }
        }
        return security_questions;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public UserTable getSelected() {
        if (selected == null) {
            selected = userFacade.find(FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("user_id"));
        }
        return selected;
    }

    public void setSelected(UserTable selected) {
        this.selected = selected;
    }

    public GroupTable getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedCar(GroupTable selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void validateInformation(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();
        // Get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String pwd = uiInputPassword.getLocalValue() == null ? ""
                : uiInputPassword.getLocalValue().toString();

        // Get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();

        if (pwd.isEmpty() || confirmPassword.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!pwd.equals(confirmPassword)) {
            statusMessage = "Passwords must match!";
        } else {
            statusMessage = "";
        }
    }

    public List<String> getEvents() {
        events.clear();
        events.add("Scheduled to drive for Capstone Sigma -- 4/1/2016  ");
        events.add("Rode with Capstone Sigma -- 3/30/2016 ");
        events.add("Rode with Beta Balci Beta -- 3/29/2016  ");
        events.add("Drove for Beta Balci Beta -- 3/28/2016  ");
        events.add("Rode with Capstone Sigma -- 3/27/2016");
        events.add("Joined Ryde! -- 3/26/2016  ");
        return events;
    }

    public String updateAccount() {
        if (selected != null) {
            try {
                System.out.println(selected.getPhoneNumber());
                selected.setPhoneNumber(selected.getPhoneNumber());
                userFacade.edit(selected);
            } catch (EJBException e) {
                return "";
            }
            return "Profile";
        }
        return "";
    }

    public String createAccount() {
        // Check to see if a user already exists with the username given.
        try {
            UserTable user = new UserTable();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setDriverStatus(false);
            fbTok = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("access_token");
            user.setFbTok(fbTok);
            fbId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fb_id");
            user.setFbId(fbId);
            user.setPhoneNumber(phoneNumber);
            userFacade.create(user);
            
            System.out.println("HERE");
            System.out.println(user.getFirstName() + " " + user.getLastName());
            
            // put user_id in session map
            FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
            
            return "Home?faces-redirect=true";
        } catch (EJBException e) {
            statusMessage = "Something went wrong while creating your account!";
            System.out.println(statusMessage);
            e.printStackTrace();
            return "";
        }
    }
}
