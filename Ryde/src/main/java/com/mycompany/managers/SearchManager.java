/*
 * Created by Peter Cho on 2016.04.22  * 
 * Copyright © 2016 Peter Cho. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.session.GroupUserFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

/**
 *
 * @author Peter Cho
 */
@ManagedBean(name = "searchManager")
@SessionScoped
public class SearchManager implements Serializable{
    private String text;
    
    /**
     * The instance variable 'groupUserFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private GroupUserFacade groupUserFacade = new GroupUserFacade();
    
    public SearchManager() {
        
    }
 
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
     
    public void handleKeyEvent() {
        text = text.toUpperCase();
    }
}



