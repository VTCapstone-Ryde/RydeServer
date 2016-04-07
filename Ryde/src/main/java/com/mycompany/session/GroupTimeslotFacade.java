/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.GroupUser;
import java.util.ArrayList;
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
    
    public GroupTable findGroupForTimeslot(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupTimeslot.findByTimeslotId").setParameter("id", id);
        q.setFirstResult(0);
        List<GroupTimeslot> result = q.getResultList();
        return result.get(0).getGroupId();
    }  
    
}
