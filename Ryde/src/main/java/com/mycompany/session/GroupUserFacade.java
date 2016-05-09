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
 * @author Patrick Abod
 * 
 * This class handles all queries on the Group-User relational database table
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

    /**
     * Find all the groups to which a user belongs
     * @param id the id of the user to search
     * @return the list of groups found
     */
    public List<GroupTable> findGroupsForUser(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByUserId").setParameter("id", id);
        q.setFirstResult(0);
        return q.getResultList();
    }

    /**
     * Find the admins for a specific group
     * @param id The id of the group to search
     * @return The list of admin users for a group
     */
    public List<UserTable> findAdminsForGroup(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findAdminsByGroupId").setParameter("id", id);
        q.setFirstResult(0);
        return q.getResultList();
    }

    /**
     * Find all users that belong to a specific group
     * @param id the id of the group to search
     * @return The list of users in a group
     */
    public List<UserTable> findUsersForGroup(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByUsersByGroupId").setParameter("id", id);
        q.setFirstResult(0);
        return q.getResultList();
    }

    /**
     * Find the group user relational object based on a group and user object
     * @param groupId The group to search
     * @param userId The user to search
     * @return The group user relational object
     */
    public GroupUser findByGroupAndUser(GroupTable groupId, UserTable userId) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByGroupAndUser").
                setParameter("groupId", groupId).setParameter("userId", userId);
        
        if (!q.getResultList().isEmpty()) {
            return (GroupUser) q.getSingleResult();
        }
        return null;
    }

    /**
     * used to clear the entity manager
     */
    public void clearEM() {
        getEntityManager().clear();
    }

    /**
     * Find the group user relational object based on a group id and user id
     * @param groupId The group id to search
     * @param userId The user id to search
     * @return The group user relational object
     */
    public GroupUser findByUserAndGroupIds(Integer userId, Integer groupId) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByGroupAndUserIDs").
                setParameter("userId", userId).setParameter("groupId", groupId);
        if (!q.getResultList().isEmpty()) {
            return (GroupUser) q.getSingleResult();
        }
        return null;
    }

    /**
     * Delete the group user relational object based on a group id and user id
     * @param groupId The group id to search
     * @param userId The user id to search
     */
    public void removeByUserAndGroupIds(Integer userId, Integer groupId) {
        GroupUser gu = findByUserAndGroupIds(userId, groupId);
        if (gu != null) {
            super.remove(gu);
        }
    }

}
