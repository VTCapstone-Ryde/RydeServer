/*
 * Created by Jared Deiner on 2016.04.02  * 
 * Copyright © 2016 Jared Deiner. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.RequestUser;
import com.mycompany.entity.UserTable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.PathParam;

/**
 *
 * @author cloud
 * @author Patrick Abod
 * 
 * This class handles all queries made on the request user database table
 */
@Stateless
public class RequestUserFacade extends AbstractFacade<RequestUser> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RequestUserFacade() {
        super(RequestUser.class);
    }

    /**
     * Find all requests for a specific group
     * @param groupId the id of the group to search
     * @return The list of request users who request to join the group
     */
    public List<RequestUser> findRequestsForGroup(Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findRequestUsersByGroupId").setParameter("groupId", groupId);
        q.setFirstResult(0);
        
        return q.getResultList();
    }
    
    /**
     * Find the users who request to join a specific group
     * @param groupId the id of the group to search
     * @return the list of users who request to join the group
     */
    public List<UserTable> findRequestUserForGroup (Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findUsersByRequestsForGroupId").setParameter("groupId", groupId);
        q.setFirstResult(0);
        
        return q.getResultList();
    }
    
    /**
     * Find a request user object based on a user object and group object
     * @param userId the id of the user
     * @param groupId the id of the group
     * @return the request user object found
     */
    public RequestUser findByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findByGroupAndUserIDs").
                setParameter("userId", userId).setParameter("groupId", groupId);
        if (!q.getResultList().isEmpty()) {
            return (RequestUser) q.getSingleResult();
        }
        return null;
    }
}
