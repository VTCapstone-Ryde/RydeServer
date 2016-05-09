/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.TimeslotUser;
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
 * This class handles all the queries made on the timeslot-user
 * relational database table
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

    /**
     * Find all users that belong to a specific timeslot
     * @param tsId the id of the timeslot
     * @return the list of users found
     */
    public List<UserTable> findUsersForTimeslot(Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findUserById").setParameter("usId", tsId);
        q.setFirstResult(0);
        return q.getResultList();
    }

    /**
     * Find all timeslots to which a user belongs
     * @param userId the id of the user to search
     * @return the list of timeslots found
     */
    public List<TimeslotTable> findTimeslotsForUser(Integer userId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findTimeslotById").setParameter("userId", userId);
        q.setFirstResult(0);
        return q.getResultList();
    }
    
    /**
     * Find all drivers for a specific timeslot
     * @param tsId the id of the timeslot to search
     * @return the list of drivers
     */
    public List<UserTable> findDriversForTimeslot(Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findDriversByTimeslotId").
                setParameter("tsId", tsId).setParameter("driver", true);
        q.setFirstResult(0);
        return q.getResultList();
    }
    
    /**
     * Find a list of Timeslot User objects based a timeslot object
     * @param tsId the id of the timeslot to search
     * @return the list of timeslot user relational objects
     */
    public List<TimeslotUser> findRowsForTimeslot(Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findByTimeslotId").
                setParameter("tsId", tsId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    /**
     * Find a timeslot-User object based on a timeslot id and user id
     * @param tsId the timeslot id
     * @param userId the user id
     * @return the timeslot-user relational object found
     */
    public TimeslotUser findByTimeslotAndUser(TimeslotTable tsId, UserTable userId){
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findByUserAndTimeSlot").
                setParameter("tsId", tsId).setParameter("userId", userId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return (TimeslotUser) q.getSingleResult();
    }
    
    /**
     * Find the drivers for a particular timeslot based on a user id
     * @param userId the user id to search
     * @return the list of timeslots the user is a driver for
     */
    public List<TimeslotTable> findUserDriverTimeslots(Integer userId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findDriverTimeslots").
                setParameter("userId", userId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    /**
     * Joins a tad
     * @param fbTok the facebook token of the user joining
     * @param passCode the passcode used to join the TAD
     * @return true if successful join, false otherwise
     */
    public boolean joinTad(String fbTok, String passCode) {
        List<TimeslotTable> ts = em.createQuery("SELECT t FROM TimeslotTable t WHERE t.passcode = :passcode", TimeslotTable.class).
                setParameter("passcode", passCode).getResultList();
        
        if (!ts.isEmpty()){
            UserTable user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                .setParameter("fbTok", fbTok).getSingleResult();
            
            TimeslotUser tu = new TimeslotUser(false, user, ts.get(0));
            
            super.create(tu);
            
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Check if a user is in a TAD (temporary access to drivers)
     * @param user the user to check
     * @param ts the timeslot to check
     * @return true if the user is in the timeslot, false otherwise
     */
    public boolean userInTAD(UserTable user, TimeslotTable ts) {
        List<TimeslotUser> tu = getEntityManager().createNamedQuery("TimeslotUser.findByUserAndTimeSlot").
                setParameter("tsId", ts).setParameter("userId", user).getResultList();
        
        return !tu.isEmpty();
    }
    
    /**
     * Find a timeslot-user relational object based on a user object and a timeslot object
     * @param userId the user id
     * @param tsId the timeslot id
     * @return the timeslot-user relational object found
     */
    public TimeslotUser findByUserAndTimeslot(Integer userId, Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findByUserIdAndTimeslotId").
                setParameter("userId", userId).setParameter("tsId", tsId);
        if (!q.getResultList().isEmpty()) {
            return (TimeslotUser) q.getSingleResult();
        }
        return null;
    }
    
    /**
     * Remove a timeslot-user relational object based on a user object and a timeslot object
     * @param userId the user id
     * @param tsId the timeslot id
     */
    public void removeByUserAndTimeslot(Integer userId, Integer tsId) {
        TimeslotUser tu = findByUserAndTimeslot(userId, tsId);
        if (tu != null) {
            super.remove(tu);
        }
    }
}
