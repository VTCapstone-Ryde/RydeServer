/*
 * Created by Jared Deiner on 2016.03.15  * 
 * Copyright © 2016 Jared Deiner. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.TimeslotUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.GroupUserFacade;
import com.mycompany.session.TimeslotTableFacade;
import com.mycompany.session.TimeslotUserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.DualListModel;

/**
 *
 * @author archer
 */
@Named(value = "timeSlotManager")
@SessionScoped
public class TimeSlotManager implements Serializable {

    private String passcode;
    private Date startTime;
    private Date endTime;
    private TimeslotTable selectedTimeSlot;
    private GroupTable selectedGroup;
    private String statusMessage;
    
    @EJB 
    private TimeslotUserFacade timeSlotUserFacade;
    @EJB
    private GroupTimeslotFacade groupTimeSlotFacade;
    @EJB
    private TimeslotTableFacade timeSlotFacade;
    @EJB
    private GroupUserFacade groupUserFacade;

    // for assigning drivers
    private List<String> availableDrivers;
    private List<String> selectedDrivers;
    private DualListModel<String> drivers;

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

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public GroupTable getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(GroupTable selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public List<String> getAvailableDrivers() {
        return availableDrivers;
    }

    public void setAvailableDrivers(List<String> availableDrivers) {
        this.availableDrivers = availableDrivers;
    }

    public List<String> getSelectedDrivers() {
        return selectedDrivers;
    }

    public void setSelectedDrivers(List<String> selectedDrivers) {
        this.selectedDrivers = selectedDrivers;
    }

    public DualListModel<String> getDrivers() {
        availableDrivers = new ArrayList();
        selectedDrivers = new ArrayList();

        if (selectedTimeSlot != null) {
            for (UserTable user : timeSlotUserFacade.findDriversForTimeslot(selectedTimeSlot.getId())) {
                selectedDrivers.add(user.getId().toString());
            }
        }

        for (UserTable user : groupUserFacade.findUsersForGroup(selectedGroup.getId())) {
            if (!selectedDrivers.contains(user.getId().toString())) {
                availableDrivers.add(user.getId().toString());
            }
        }

        drivers = new DualListModel<String>(availableDrivers, selectedDrivers);
        return drivers;
    }

    public void setDrivers(DualListModel<String> drivers) {
        this.drivers = drivers;
    }

    public boolean searchDriverById(Integer id) {
        for (String user : drivers.getTarget()) {
            Integer userId = Integer.parseInt(user);
            if (userId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public String getDriversForTimeslot(Integer id) {
        String driverString = "";
        List<UserTable> currentDrivers = timeSlotUserFacade.findDriversForTimeslot(id);
        for (UserTable driver : currentDrivers) {
            driverString += driver.getFirstName() + " " + driver.getLastName() + ", ";
        }

        if (driverString.isEmpty()) {
            return "";
        } else {
            return driverString.substring(0, driverString.length() - 2);
        }
    }

    public String assignDrivers() {
        List<UserTable> users = groupUserFacade.findUsersForGroup(getSelectedGroup().getId());
        for (UserTable user : users) {
            TimeslotUser newTimeslotUserRow = timeSlotUserFacade.findByTimeslotAndUser(selectedTimeSlot, user);

            if (searchDriverById(user.getId())) {
                newTimeslotUserRow.setDriver(Boolean.TRUE);
            } else {
                newTimeslotUserRow.setDriver(Boolean.FALSE);
            }

            timeSlotUserFacade.edit(newTimeslotUserRow);
        }
        return "ViewTimeSlotForGroup?faces-redirect=true";
    }

    public String createTimeslot() {
        try {
            // create the time slot
            TimeslotTable newTimeslot = new TimeslotTable();
            newTimeslot.setPasscode(passcode);
            newTimeslot.setStartTime(startTime);
            newTimeslot.setEndTime(endTime);
            
            // add timeslot to the database
            timeSlotFacade.create(newTimeslot);

            // add row in grouptimeslot table
            GroupTimeslot newGroupTimeslotRow = new GroupTimeslot();
            newGroupTimeslotRow.setGroupId(selectedGroup);
            newGroupTimeslotRow.setTsId(newTimeslot);
            groupTimeSlotFacade.create(newGroupTimeslotRow);

            // add row in timeslotuser table
            List<UserTable> users = groupUserFacade.findUsersForGroup(getSelectedGroup().getId());
            for (UserTable user : users) {
                TimeslotUser newTimeslotUserRow = new TimeslotUser();
                newTimeslotUserRow.setUserId(user);
                newTimeslotUserRow.setTsId(newTimeslot);

                if (searchDriverById(user.getId())) {
                    newTimeslotUserRow.setDriver(Boolean.TRUE);
                } else {
                    System.out.println("nope");
                    newTimeslotUserRow.setDriver(Boolean.FALSE);
                }
                timeSlotUserFacade.create(newTimeslotUserRow);
            }

            selectedTimeSlot = null;
            passcode = "";
            startTime = null;
            endTime = null;
            drivers.getTarget().clear();
            drivers.getSource().clear();
            return "ViewTimeSlotForGroup?faces-redirect=true";

        } catch (EJBException e) {
            statusMessage = "Something went wrong while creating your account!";
            return "";
        }
    }

    public String deleteTimeslot() {
        if (selectedTimeSlot != null) {
            try {
                // get the deleted timeslot and group associated with it
                TimeslotTable deletedTimeslot = selectedTimeSlot;
                GroupTimeslot deleteRow = groupTimeSlotFacade.findByTimeslot(deletedTimeslot.getId());

                // remove from the groupTimeslot table
                groupTimeSlotFacade.remove(deleteRow);

                // remove any timeslotUser rows in the timeslotUser relation table
                for (TimeslotUser row : timeSlotUserFacade.findRowsForTimeslot(deletedTimeslot.getId())) {
                    timeSlotUserFacade.remove(row);
                }

                // remove the timeslot from the timeslot table
                timeSlotFacade.remove(deletedTimeslot);
                selectedTimeSlot = null;
                return "ViewTimeSlotForGroup?faces-redirect=true";

            } catch (EJBException e) {
                statusMessage = "Something went wrong while creating your account!";
                return "";
            }
        }
        return "";
    }

    public String createTimeslotPage() {
        return "CreateTimeslot?faces-redirect=true";
    }

    public String assignDriversPage() {
        return "AssignDrivers?faces-redirect=true";
    }

}
