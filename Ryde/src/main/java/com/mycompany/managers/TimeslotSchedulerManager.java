/*
 * Created by Cameron Gibson on 2016.05.02  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.Ride;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.service.RideFacadeREST;
import com.mycompany.session.EventFacade;
import com.mycompany.session.TimeslotTableFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Cameron
 */
@Singleton
public class TimeslotSchedulerManager {
    
    /**
     * The instance variable 'tsFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * TimeslotTableFacade.
     */
    @EJB
    private TimeslotTableFacade tsFacade;
    
    /**
     * The instance variable 'rideFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * RideFacadeRest.
     */
    @EJB
    private RideFacadeREST rideFacade;
    
    /**
     * The instance variable 'eventFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * EventFacade.
     */
    @EJB
    private EventFacade eventFacade;
   
    /**
     * Removes expired timeslots
     * runs every minute
     * Only removes if either there are no active drivers or no rides left for the timeslot
     */
    @Schedule(hour="*", minute="*")
    public void removeExpiredTimeslots() {
            List<TimeslotTable> expiredTimeslots = tsFacade.findExpiredTimeslots();
            List<UserTable> activeDriver;
            List<Ride> queue;
            for (TimeslotTable ts: expiredTimeslots) {
                activeDriver = tsFacade.findActiveDriversForTimeslot(ts.getId());
                queue = rideFacade.findAllRidesForTimeslot(ts.getId());
                if (activeDriver.isEmpty() || queue.isEmpty()) {
                    eventFacade.generateReport(ts);
                    tsFacade.remove(ts);
                }
            }
        }
}