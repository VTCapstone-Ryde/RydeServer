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
    
    public List<GroupTable> searchGroupByTitle(String title) {
        List<GroupTable> groups = new ArrayList<>();
        
        if (title.trim().length() != 0) {
            groups = em.createQuery("SELECT g FROM GroupTable g WHERE g.title LIKE '%" + title + "%'", GroupTable.class)
                .getResultList();
        }

        return groups;
    }
}
