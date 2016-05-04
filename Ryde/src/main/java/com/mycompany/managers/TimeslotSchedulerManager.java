/*
 * Created by Cameron Gibson on 2016.05.02  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.TimeslotTable;
import com.mycompany.session.TimeslotTableFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 *
 * @author Cameron
 */
@Singleton
public class TimeslotSchedulerManager {
    @EJB
    TimeslotTableFacade tsFacade;
   
//    @Schedule(hour="*", minute="*")
    public void runsEveryMinute() {
            List<TimeslotTable> expiredTimeslots = tsFacade.findExpiredTimeslots();
            
        }
}