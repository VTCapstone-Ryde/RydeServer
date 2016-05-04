/*
 * Created by Cameron Gibson on 2016.05.02  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.Ride;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.service.RideFacadeREST;
import com.mycompany.session.TimeslotTableFacade;
import com.mycompany.session.UserTableFacade;
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
    @EJB
    TimeslotTableFacade tsFacade;
    @EJB
    UserTableFacade userFacade;
    @EJB
    RideFacadeREST rideFacade;
   
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
                if (activeDriver.size() == 0 || queue.size() == 0) {
                    tsFacade.remove(ts);
                }
            }
        }
}