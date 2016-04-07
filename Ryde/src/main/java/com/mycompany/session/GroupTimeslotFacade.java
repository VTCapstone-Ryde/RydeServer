/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.GroupTimeslot;
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
public class GroupTimeslotFacade extends AbstractFacade<GroupTimeslot> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupTimeslotFacade() {
        super(GroupTimeslot.class);
    }
    
    /*
        The following methods were added to the generated code
    */

    /**
     *
     * @param groupId
     * @return
     */

    
    public List<GroupTimeslot> findTimeslotsForGroup(Integer groupId) {
        try {
            if (em.createQuery("SELECT g FROM GroupTimeslot g WHERE g.id = :id", GroupTimeslot.class)
                    .setParameter("id", groupId)
                    .getResultList().isEmpty()) {
                System.out.println("No user found with token: " + groupId);
                return null;
            }
            else {
                 return em.createQuery("SELECT g FROM GroupTimeslot g WHERE g.id = :id", GroupTimeslot.class)
                    .setParameter("id", groupId).getResultList();
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
