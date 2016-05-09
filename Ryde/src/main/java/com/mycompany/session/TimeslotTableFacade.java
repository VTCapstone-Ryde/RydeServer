/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import java.util.Date;
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
 * This class handles all queries made on the timeslot database table
 */
@Stateless
public class TimeslotTableFacade extends AbstractFacade<TimeslotTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TimeslotTableFacade() {
        super(TimeslotTable.class);
    }

    /**
     * Find a specific timeslot by its id
     * @param id the id of the timeslot to search
     * @return the timeslot found in a list
     */
    public List<TimeslotTable> findById(Integer id) {
        Query q = getEntityManager().createNamedQuery("TimeslotTable.findById").setParameter("id", id);

        if (!q.getResultList().isEmpty()) {
            return q.getResultList();
        }

        return null;
    }

    /**
     * Find all timeslots that are currently active
     * @return the list of active timeslots
     */
    public List<TimeslotTable> findAllActiveTimeslots() {
        Date time = new Date();
        List<TimeslotTable> activeTimeslots = getEntityManager().createQuery("SELECT t FROM TimeslotTable t WHERE t.startTime <= :time AND t.endTime >= :time")
                .setParameter("time", time)
                .getResultList();
        return activeTimeslots;
    }

    /**
     * Find all times that have expired
     * @return The list of expired timeslots
     */
    public List<TimeslotTable> findExpiredTimeslots() {
        Date time = new Date();
        List<TimeslotTable> expiredTimeslots = getEntityManager().createQuery("SELECT t FROM TimeslotTable t WHERE t.startTime <= :time AND t.endTime <= :time")
                .setParameter("time", time)
                .getResultList();
        return expiredTimeslots;    
    }
    
    /**
     * Find the active (online) drivers for a specific timeslot
     * @param tsId the timeslot id
     * @return the list of users who are currently driving and online
     */
    public List<UserTable> findActiveDriversForTimeslot(Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findDriversByTimeslotId").setParameter("tsId", tsId).setParameter("driver", true);
        q.setFirstResult(0);
        return q.getResultList();
    }
}
