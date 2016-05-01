/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.GroupTable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author cloud
 */
@Stateless
public class GroupTableFacade extends AbstractFacade<GroupTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupTableFacade() {
        super(GroupTable.class);
    }
    
    public GroupTable findById(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupTable.findById").setParameter("id", id);
        q.setFirstResult(0);
        //TODO add empty result handling
        return (GroupTable)q.getSingleResult();
    }
    
    public List<GroupTable> searchGroupByTitle(String title) {
        List<GroupTable> groups = new ArrayList<>();
        
        if (title.trim().length() != 0) {
            groups = em.createQuery("SELECT g FROM GroupTable g WHERE g.title LIKE '%" + title + "%'", GroupTable.class)
                .getResultList();
        }

        return groups;
    }
    
    public List<GroupTable> findGroupsByTitle(String title) {
        String addSpacesToTitle = title.replaceAll("\\+", " ");
        Query q = getEntityManager().createNamedQuery("GroupTable.findByTitle").setParameter("title", addSpacesToTitle);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
}
