/*
 * Created by Patrick Abod on 2016.04.11  * 
 * Copyright © 2016 Patrick Abod. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.Event;
import com.mycompany.entity.Ride;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author patrickabod
 */
@Stateless
public class EventFacade extends AbstractFacade<Event> {

    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventFacade() {
        super(Event.class);
    }
    
    public Event createRideEvent(Ride ride, Date date) {
        Event event = new Event();
        event.setDatetime(date);
        event.setDriverUserId(ride.getDriverUserId());
        event.setEventType("rideCompleted");
        event.setTsId(ride.getTsId());
        
        // create event
        try {
            em.persist(new Event());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return event;
    }
    
    public Event createStatusEvent() {
        return null;
    }
}
