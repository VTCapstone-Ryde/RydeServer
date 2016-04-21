/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.UserTable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.PathParam;

/**
 *
 * @author cloud
 */
@Stateless
public class UserTableFacade extends AbstractFacade<UserTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserTableFacade() {
        super(UserTable.class);
    }
    
    public UserTable findById(Integer id) {
        Query q = getEntityManager().createNamedQuery("UserTable.findById").setParameter("id", id);
        q.setFirstResult(0);
        //TODO add empty result handling
        return (UserTable)q.getSingleResult();
    }
    
    public UserTable findByFbId(String fbId) {
        Query q = getEntityManager().createNamedQuery("UserTable.findByFbId").setParameter("fbId", fbId);
        q.setFirstResult(0);
        if (q.getResultList().isEmpty()) {
            return null;
        }
        //TODO add empty result handling
        return (UserTable)q.getSingleResult();
    }
    
    public UserTable findByName(String firstName, String lastName) {
        if (em.createQuery("SELECT u FROM UserTable u WHERE u.firstName = :firstName AND u.lastName = :lastName")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return em.createQuery("SELECT u FROM UserTable u WHERE u.firstName = :firstName AND u.lastName = :lastName", UserTable.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList().get(0);
        }
    }
    
    public UserTable findByToken(String token) {
        try {
            if (em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token)
                    .getResultList().isEmpty()) {
                System.out.println("No user found with token: " + token);
                return null;
            }
            else {
                 return em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token).getResultList().get(0);
                            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }
    
     public List<UserTable> findUsersByName(@PathParam("name") String name) {
        String addSpacesToName = name.replaceAll(" ", "%");
        addSpacesToName = addSpacesToName.concat("%");
        addSpacesToName = "%".concat(addSpacesToName);
        Query q = getEntityManager().createNamedQuery("UserTable.findByEntireName").setParameter("name", addSpacesToName);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    } 
}
