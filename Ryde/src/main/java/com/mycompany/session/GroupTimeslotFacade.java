/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.TimeslotTable;
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
    
    public List<TimeslotTable> findTimeslotsForGroup(Integer groupId) {
        Query q = getEntityManager().createNamedQuery("GroupTimeslot.findTimeslotsByGroupId").setParameter("id", groupId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    public GroupTable findGroupForTimeslot(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupTimeslot.findByTimeslotId").setParameter("id", id);
        q.setFirstResult(0);
        List<GroupTimeslot> result = q.getResultList();
        //TODO add empty result handling
        return result.get(0).getGroupId();
    } 
}
