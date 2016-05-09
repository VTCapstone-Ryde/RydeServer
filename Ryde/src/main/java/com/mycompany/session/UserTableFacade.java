/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

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
 * The class handles all queries made on the user database table
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
    
    /**
     * Find a user by his/her id
     * @param id the user id
     * @return the user found
     */
    public UserTable findById(Integer id) {
        Query q = getEntityManager().createNamedQuery("UserTable.findById").setParameter("id", id);
        q.setFirstResult(0);
        return (UserTable)q.getSingleResult();
    }
    
    /**
     * Find a specific user by his/her facebook id
     * @param fbId the facebook id
     * @return the user found
     */
    public UserTable findByFbId(String fbId) {
        Query q = getEntityManager().createNamedQuery("UserTable.findByFbId").setParameter("fbId", fbId);
        q.setFirstResult(0);
        if (q.getResultList().isEmpty()) {
            return null;
        }
        return (UserTable)q.getResultList().get(0);
    }
    
    /**
     * Find a specific user by first and last name
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @return the user found
     */
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
    
    /**
     * Find a specific user by his/her facebook token
     * @param token the facebook token
     * @return the user found
     */
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
    
    /**
     * Find all users with names that match the given pattern
     * @param name the name pattern to search
     * @return the list of users found
     */
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
