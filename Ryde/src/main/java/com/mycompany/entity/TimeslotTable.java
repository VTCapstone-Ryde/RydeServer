/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cloud
 */
@Entity
@Table(name = "Timeslot_Table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TimeslotTable.findAll", query = "SELECT t FROM TimeslotTable t"),
    @NamedQuery(name = "TimeslotTable.findById", query = "SELECT t FROM TimeslotTable t WHERE t.id = :id"),
    @NamedQuery(name = "TimeslotTable.findByPasscode", query = "SELECT t FROM TimeslotTable t WHERE t.passcode = :passcode"),
    @NamedQuery(name = "TimeslotTable.findByStartTime", query = "SELECT t FROM TimeslotTable t WHERE t.startTime = :startTime"),
    @NamedQuery(name = "TimeslotTable.findByEndTime", query = "SELECT t FROM TimeslotTable t WHERE t.endTime = :endTime")})
public class TimeslotTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "passcode")
    private String passcode;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tsId")
    private Collection<GroupTimeslot> groupTimeslotCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tsId")
    private Collection<TimeslotUser> timeslotUserCollection;

    public TimeslotTable() {
    }

    public TimeslotTable(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @XmlTransient
    public Collection<GroupTimeslot> getGroupTimeslotCollection() {
        return groupTimeslotCollection;
    }

    public void setGroupTimeslotCollection(Collection<GroupTimeslot> groupTimeslotCollection) {
        this.groupTimeslotCollection = groupTimeslotCollection;
    }

    @XmlTransient
    public Collection<TimeslotUser> getTimeslotUserCollection() {
        return timeslotUserCollection;
    }

    public void setTimeslotUserCollection(Collection<TimeslotUser> timeslotUserCollection) {
        this.timeslotUserCollection = timeslotUserCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeslotTable)) {
            return false;
        }
        TimeslotTable other = (TimeslotTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.TimeslotTable[ id=" + id + " ]";
    }
    
}
