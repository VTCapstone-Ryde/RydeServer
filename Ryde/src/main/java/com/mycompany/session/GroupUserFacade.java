/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupUser;
import com.mycompany.entity.UserTable;
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
public class GroupUserFacade extends AbstractFacade<GroupUser> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupUserFacade() {
        super(GroupUser.class);
    }
    
    public List<GroupTable> findGroupsForUser(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByUserId").setParameter("id", id);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    public List<UserTable> findAdminsForGroup(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findAdminsByGroupId").setParameter("id", id);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    public List<UserTable> findUsersForGroup(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByUsersByGroupId").setParameter("id", id);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
}
