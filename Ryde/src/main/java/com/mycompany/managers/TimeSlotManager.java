/*
 * Created by Jared Deiner on 2016.03.15  * 
 * Copyright © 2016 Jared Deiner. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.TimeslotUserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author archer
 */
@Named(value = "timeSlotManager")
@SessionScoped
public class TimeSlotManager  implements Serializable {
    
    private TimeslotTable selectedTimeSlot = new TimeslotTable();
    private TimeslotUserFacade timeSlotUserFacade = new TimeslotUserFacade();
    private GroupTimeslotFacade groupTimeSlotFacade = new GroupTimeslotFacade();

    public GroupTable getTimeSlotGroup(Integer tsId) {
        return groupTimeSlotFacade.findGroupForTimeslot(tsId);
    }

    public TimeslotTable getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(TimeslotTable selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    public TimeslotUserFacade getTimeSlotUserFacade() {
        return timeSlotUserFacade;
    }

    public void setTimeSlotUserFacade(TimeslotUserFacade timeSlotUserFacade) {
        this.timeSlotUserFacade = timeSlotUserFacade;
    }

    public GroupTimeslotFacade getGroupTimeSlotFacade() {
        return groupTimeSlotFacade;
    }

    public void setGroupTimeSlotFacade(GroupTimeslotFacade groupTimeSlotFacade) {
        this.groupTimeSlotFacade = groupTimeSlotFacade;
    }

}
