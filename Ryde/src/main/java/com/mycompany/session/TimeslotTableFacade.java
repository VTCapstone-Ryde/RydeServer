/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.TimeslotTable;
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

    public List<TimeslotTable> findById(Integer id) {
        Query q = getEntityManager().createNamedQuery("TimeslotTable.findById").setParameter("id", id);

        if (!q.getResultList().isEmpty()) {
            return q.getResultList();
        }

        return null;
    }

    public List<TimeslotTable> findAllActiveTimeslots() {
        Date time = new Date();
        List<TimeslotTable> activeTimeslots = getEntityManager().createQuery("SELECT t FROM TimeslotTable t WHERE t.startTime <= :time AND t.endTime >= :time")
                .setParameter("time", time)
                .getResultList();
        return activeTimeslots;
    }
}
