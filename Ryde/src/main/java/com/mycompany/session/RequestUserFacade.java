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

    public List<RequestUser> findRequestsForGroup(Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findRequestUsersByGroupId").setParameter("groupId", groupId);
        q.setFirstResult(0);
        //TODO add empty result handling
        
        return q.getResultList();
    }
    
    public List<UserTable> findRequestUserForGroup (Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findUsersByRequestsForGroupId").setParameter("groupId", groupId);
        q.setFirstResult(0);
        //TODO add empty result handling
        
        return q.getResultList();
    }
    
    public RequestUser findByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findByGroupAndUserIDs").
                setParameter("userId", userId).setParameter("groupId", groupId);
        if (!q.getResultList().isEmpty()) {
            return (RequestUser) q.getSingleResult();
        }
        return null;
    }
}
