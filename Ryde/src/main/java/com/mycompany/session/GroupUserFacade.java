/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupUser;
import com.mycompany.entity.UserTable;
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
//        getEntityManager().clear();
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

    public GroupUser findByGroupAndUser(GroupTable groupId, UserTable userId) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByGroupAndUser").
                setParameter("groupId", groupId).setParameter("userId", userId);
        
        if (!q.getResultList().isEmpty()) {
            return (GroupUser) q.getSingleResult();
        }
        return null;
    }

    public void clearEM() {
        getEntityManager().clear();
    }

    public GroupUser findByUserAndGroupIds(Integer userId, Integer groupId) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByGroupAndUserIDs").
                setParameter("userId", userId).setParameter("groupId", groupId);
        if (!q.getResultList().isEmpty()) {
            return (GroupUser) q.getSingleResult();
        }
        return null;
    }

    public void removeByUserAndGroupIds(Integer userId, Integer groupId) {
        GroupUser gu = findByUserAndGroupIds(userId, groupId);
        if (gu != null) {
            super.remove(gu);
        }
    }

}
