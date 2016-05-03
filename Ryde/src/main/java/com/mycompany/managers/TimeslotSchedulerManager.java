/*
 * Created by Cameron Gibson on 2016.05.02  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.managers;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Cameron
 */
@Singleton
public class TimeslotSchedulerManager {
   
//    @Schedule(hour="*", minute="*")
    public void runsEveryMinute() {
            System.out.print(" runs EveryMinute ");
        }
}