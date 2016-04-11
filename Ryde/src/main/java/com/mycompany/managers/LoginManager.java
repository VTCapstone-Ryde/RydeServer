/*
 * Created by Osman Balci on 2016.02.14  * 
 * Copyright © 2016 Osman Balci. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.UserTable;
import com.mycompany.session.UserTableFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "loginManager")
@SessionScoped
/**
 *
 * @author Balci
 */
public class LoginManager implements Serializable {

  private String firstName;
  private String lastName;
  private String errorMessage;
  
  /**
   * The instance variable 'userFacade' is annotated with the @EJB annotation.
   * This means that the GlassFish application server, at runtime, will inject in
   * this instance variable a reference to the @Stateless session bean UserFacade.
   */
  @EJB
  private UserTableFacade userFacade;

  /**
   * Creates a new instance of LoginManager
   */
  public LoginManager() {
  }


  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
    
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public void setLastName(String lastName) {
      this.lastName = lastName;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String loginUser() {
    UserTable user = userFacade.findByName(firstName, lastName);
    if (user == null) {
      errorMessage = "Invalid username or password!";
      return "";
    } else {
      if (user.getFirstName().equals(getFirstName()) && user.getLastName().equals(getLastName())) {
        errorMessage = "";
        initializeSessionMap(user);
        return "Home";
      }
      errorMessage = "Invalid username or password!";
      return "";
    }
  }

  public void initializeSessionMap(UserTable user) {
    FacesContext.getCurrentInstance().getExternalContext().
            getSessionMap().put("first_name", user.getFirstName());
    FacesContext.getCurrentInstance().getExternalContext().
            getSessionMap().put("last_name", user.getLastName());
    FacesContext.getCurrentInstance().getExternalContext().
            getSessionMap().put("user_id", user.getId());
  }

}
