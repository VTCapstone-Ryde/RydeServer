/*
 * Created by Patrick Abod on 2016.04.11  * 
 * Copyright © 2016 Patrick Abod. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.Event;
import com.mycompany.entity.GroupTable;
import com.mycompany.entity.Ride;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.managers.Constants;
import com.mycompany.service.UserTableFacadeREST;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * 
 * This class is used to handle queries on the event database table
 */
@Stateless
public class EventFacade extends AbstractFacade<Event> {

    // Entity manager for the class
    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;

    /* Other facades referenced by this class which have their own EM's*/
    @EJB
    private UserTableFacadeREST userFacadeREST;
    
    @EJB
    private UserTableFacade userFacade;
    
    @EJB
    private GroupTimeslotFacade gtFacade;
    
    @EJB
    private TimeslotUserFacade tuFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventFacade() {
        super(Event.class);
    }

    /**
     * Create an event that says a ride has been created
     * @param ride The ride completed
     * @return The newly created event
     */
    public Event createRideCompletedEvent(Ride ride) {
        // create event
        Event event = new Event();
        // set fields
        event.setDatetime(new Date());
        event.setDriverUserId(ride.getDriverUserId());
        event.setEventType("rideCompleted");
        event.setTsId(ride.getTsId());

        // create event and persist to DB
        try {
            em.persist(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }

    /**
     * Create an event that says a ride has been canceled (By a driver)
     * @param ride The ride canceled
     * @return The newly created event
     */
    public Event createRideCancelledEvent(Ride ride) {
        // create event
        Event event = new Event();
        // set fields
        event.setDatetime(new Date());
        event.setDriverUserId(ride.getDriverUserId());
        event.setEventType("rideCancelled");
        event.setTsId(ride.getTsId());

        // create event and persist to DB
        try {
            em.persist(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }

    /**
     * Create an event for a driver status change (online or offline)
     * @param fbTok The Facebook token for the driver
     * @return the newly created event
     */
    public Event createDriverStatusEvent(String fbTok) {
        // find the driver
        UserTable driver = userFacade.findByToken(fbTok);
        // get the timeslot for which the driver is driving
        TimeslotTable timeslot = tuFacade.findUserDriverTimeslots(driver.getId()).get(0);
        // create a new event
        Event event = new Event();
        // set the event fields
        event.setDatetime(new Date());
        event.setDriverUserId(driver);
        event.setTsId(timeslot);
        if (driver.getDriverStatus() == true) {
            event.setEventType("online");
        } else {
            event.setEventType("offline");
        }
        event.setTsId(userFacadeREST.findActiveDriverTimeslot(fbTok));

        // create event and persist to DB
        try {
            em.persist(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }
    
    /**
     * Find all the events for a specific timeslot
     * @param timeslot the timeslot on which to search
     * @return the list of events found
     */
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
    
    /**
     * Internally generate a report of all events when a timeslot ends
     * @param timeslot the timeslot that has ended
     * @return the report text file
     */
    public File generateReport(TimeslotTable timeslot) {
        // find all events for the timeslot
        List<Event> events = findEventsForTimeslot(timeslot);
        // find the group for the timeslot
        GroupTable group = gtFacade.findGroupForTimeslot(timeslot.getId());
        // format the date of the starting time for the timeslot
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy'@'hh:mm");
        String date = sdf.format(timeslot.getStartTime());
        // Create a new file to be added to the server
        File file = null;
        if (events != null) {
            file = new File(Constants.ROOT_DIRECTORY + group.getId());
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(Constants.ROOT_DIRECTORY + group.getId() + "/" + date);
            String content = "";
            // create event descriptions to be placed in the file in the following format
            // date$event
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
                            + driver.getLastName() + " completed a ride.";
                } else {
                    occurrence += "Driver " + driver.getFirstName() + " "
                            + driver.getLastName() + " cancelled a ride.";
                }
                occurrence += "\n";
                content += occurrence;
                remove(e);
            }
            // write the content to the file
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
