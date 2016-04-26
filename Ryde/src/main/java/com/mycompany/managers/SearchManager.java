/*
 * Created by Peter Cho on 2016.04.22  * 
 * Copyright © 2016 Peter Cho. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTableFacade;
import com.mycompany.session.GroupUserFacade;
import com.mycompany.session.UserTableFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

/**
 *
 * @author Peter Cho
 */
@ManagedBean
@Named(value = "searchManager")
@SessionScoped
public class SearchManager implements Serializable{
    private String searchedGroupName;
    private List<GroupTable> matchedGroups = new ArrayList();
    
    /**
     * The instance variable 'groupUserFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private GroupUserFacade groupUserFacade;
    @EJB
    private UserTableFacade userFacade;
    @EJB
    private GroupTableFacade groupFacade;
    
    public SearchManager() {
        
    }
    
    public String getSearchedGroupName() {
        return searchedGroupName;
    }

    public void setSearchedGroupName(String searchGroupName) {
        this.searchedGroupName = searchGroupName;
    }

    public void setMatchedGroups(List<GroupTable> matchedGroups) {
        this.matchedGroups = matchedGroups;
    }

    public List<GroupTable> getMatchedGroups() {
        return matchedGroups;
    }
    
    public UserTable getLoggedInUser() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }
    
    public void searchedGroups() {
        matchedGroups = groupFacade.searchGroupByTitle(searchedGroupName);
        List<GroupTable> userGroups = groupUserFacade.findGroupsForUser( getLoggedInUser().getId());
        
        for (GroupTable group : userGroups) {
            if (matchedGroups.contains(group)) {
                matchedGroups.remove(group);
            }
        }
    }
}



