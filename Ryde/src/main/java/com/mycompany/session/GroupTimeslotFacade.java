/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.TimeslotUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    public GroupTable findGroupForTimeslot(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupTimeslot.findByTimeslotId").setParameter("id", id);
        q.setFirstResult(0);
        List<GroupTimeslot> result = q.getResultList();
        //TODO add empty result handling
        return result.get(0).getGroupId();
    } 
}
