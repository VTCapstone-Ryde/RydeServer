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

    public List<UserTable> findUsersForTimeslot(Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findUserById").setParameter("usId", tsId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }

    public List<TimeslotTable> findTimeslotsForUser(Integer userId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findTimeslotById").setParameter("userId", userId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    public List<UserTable> findDriversForTimeslot(Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findDriversByTimeslotId").
                setParameter("tsId", tsId).setParameter("driver", true);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    public List<TimeslotUser> findRowsForTimeslot(Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findByTimeslotId").
                setParameter("tsId", tsId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    public TimeslotUser findByTimeslotAndUser(TimeslotTable tsId, UserTable userId){
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findByUserAndTimeSlot").
                setParameter("tsId", tsId).setParameter("userId", userId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return (TimeslotUser) q.getSingleResult();
    }
    
    public List<TimeslotTable> findUserDriverTimeslots(Integer userId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findDriverTimeslots").
                setParameter("userId", userId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    
    public boolean joinTad(String fbTok, String passCode) {
        TimeslotTable ts = em.createQuery("SELECT t FROM TimeslotTable t WHERE t.passcode = :passcode", TimeslotTable.class).
                setParameter("passcode", passCode).getSingleResult();
        
        if (ts != null){
            UserTable user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                .setParameter("fbTok", fbTok).getSingleResult();
            
            TimeslotUser tu = new TimeslotUser(false, user, ts);
            
            create(tu);
            
            return true;
        }
        else {
            return false;
        }
    }
}
