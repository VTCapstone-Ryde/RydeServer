/*
 * Created by Cameron Gibson on 2016.04.25  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Cameron
 */
public class TimeslotDateResponse implements Serializable{
    @XmlElement
    private Date date;
    @XmlElement
    private List<TimeslotTable> timeslots;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TimeslotTable> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeslotTable> timeslots) {
        this.timeslots = timeslots;
    }
    
}
