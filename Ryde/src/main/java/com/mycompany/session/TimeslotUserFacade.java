/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.TimeslotUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author cloud
 */
@Stateless
public class TimeslotUserFacade extends AbstractFacade<TimeslotUser> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TimeslotUserFacade() {
        super(TimeslotUser.class);
    }
    
    public List<TimeslotUser> findUsersForTimeslot(Integer tsId) {
        try {
            if (em.createQuery("SELECT t FROM TimeslotUser t WHERE t.tsId = :tsId", TimeslotUser.class)
                    .setParameter("tsId", tsId)
                    .getResultList().isEmpty()) {
                System.out.println("No users in timeslot with id: " + tsId);
                return null;
            } 
            else {
                return em.createQuery("SELECT t FROM TimeslotUser t WHERE t.tsId = :tsId", TimeslotUser.class)
                    .setParameter("tsId", tsId)
                    .getResultList();
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }

    public List<TimeslotUser> findTimeslotsForUser(Integer userId) {
        try {
            if (em.createQuery("SELECT t FROM TimeslotUser t WHERE t.id = :id", TimeslotUser.class)
                    .setParameter("id", userId)
                    .getResultList().isEmpty()) {
                System.out.println("No user found with token: " + userId);
                return null;
            }
            else {
                 return em.createQuery("SELECT t FROM TimeslotUser t WHERE t.id = :id", TimeslotUser.class)
                    .setParameter("id", userId).getResultList();
                            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }
    
}
