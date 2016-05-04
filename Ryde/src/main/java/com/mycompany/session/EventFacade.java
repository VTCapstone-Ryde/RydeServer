/*
 * Created by Patrick Abod on 2016.04.11  * 
 * Copyright © 2016 Patrick Abod. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.Event;
import com.mycompany.entity.Ride;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.service.UserTableFacadeREST;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author patrickabod
 */
@Stateless
public class EventFacade extends AbstractFacade<Event> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;

    @EJB
    private UserTableFacadeREST userFacadeREST;
    
    @EJB
    private UserTableFacade userFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventFacade() {
        super(Event.class);
    }

    public Event createRideCompletedEvent(Ride ride) {
        Event event = new Event();
        event.setDatetime(new Date());
        event.setDriverUserId(ride.getDriverUserId());
        event.setEventType("rideCompleted");
        event.setTsId(ride.getTsId());

        // create event
        try {
            em.persist(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }

    public Event createRideCancelledEvent(Ride ride) {
        Event event = new Event();
        event.setDatetime(new Date());
        event.setDriverUserId(ride.getDriverUserId());
        event.setEventType("rideCancelled");
        event.setTsId(ride.getTsId());

        // create event
        try {
            em.persist(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }

    public Event createDriverStatusEvent(String fbTok) {
        UserTable driver = userFacade.findByToken(fbTok);
        Event event = new Event();
        event.setDatetime(new Date());
        event.setDriverUserId(driver);
        if (driver.getDriverStatus() == true) {
            event.setEventType("online");
        } else {
            event.setEventType("offline");
        }
        event.setTsId(userFacadeREST.findActiveDriverTimeslot(fbTok));

        // create event
        try {
            em.persist(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }
    
    public List<Event> findEventsForTimeslot(TimeslotTable timeslot) {
        List<Event> events = null;
        try {
            events = em.createNamedQuery("Event.findEventsByTimeslot", Event.class)
                    .setParameter("timeslot", timeslot).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
    
    public File generateReport(TimeslotTable timeslot) {
        List<Event> events = findEventsForTimeslot(timeslot);
        File file = null;
        if (events != null) {
            file = new File("Test/File/Path" + timeslot.getId());
            String content = "";
            for (Event e : events) {
                UserTable driver = e.getDriverUserId();
                String occurrence = e.getDatetime().toString() + "$";
                if (e.getEventType().equals("online")) {                  
                    occurrence += "Driver " + driver.getFirstName() + " "
                            + driver.getLastName() + " went online.";
                } else if (e.getEventType().equals("offline")) {
                    occurrence += "Driver " + driver.getFirstName() + " "
                            + driver.getLastName() + " went offline.";
                } else if (e.getEventType().equals("rideCompleted")) {
                    occurrence += "Driver " + driver.getFirstName() + " "
                            + driver.getLastName() + " completed ride.";
                } else {
                    occurrence += "Driver " + driver.getFirstName() + " "
                            + driver.getLastName() + " cancelled ride.";
                }
                occurrence += "\n";
                content += occurrence;
                remove(e);
            }
            FileWriter fw;
            try {
                fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(EventFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

}
