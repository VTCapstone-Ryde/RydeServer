/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cloud
 */
@Entity
@Table(name = "TimeslotUser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TimeslotUser.findAll", query = "SELECT t FROM TimeslotUser t"),
    @NamedQuery(name = "TimeslotUser.findByUserAndTimeSlot", query = "SElECT t FROM TimeslotUser t WHERE t.userId = :userId AND t.tsId = :tsId"),
    @NamedQuery(name = "TimeslotUser.findDriverTimeslots", query = "SELECT t.tsId FROM TimeslotUser t WHERE t.driver = 1 AND t.userId.id = :userId"),
    @NamedQuery(name = "TimeslotUser.findById", query = "SELECT t FROM TimeslotUser t WHERE t.id = :id"),
    @NamedQuery(name = "TimeslotUser.findByTimeslotId", query = "SELECT t FROM TimeslotUser t WHERE t.tsId.id = :tsId"),
    @NamedQuery(name = "TimeslotUser.findUserById", query = "SELECT t.userId FROM TimeslotUser t WHERE t.tsId.id = :tsId"),
    @NamedQuery(name = "TimeslotUser.findTimeslotById", query = "SELECT t.tsId FROM TimeslotUser t WHERE t.userId.id = :userId"),
    @NamedQuery(name = "TimeslotUser.findDriversByTimeslotId", query = "SELECT t.userId FROM TimeslotUser t WHERE t.tsId.id = :tsId AND t.driver = :driver"),
    @NamedQuery(name = "TimeslotUser.findByDriver", query = "SELECT t FROM TimeslotUser t WHERE t.driver = :driver")})
public class TimeslotUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "driver")
    private Boolean driver;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserTable userId;
    @JoinColumn(name = "ts_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TimeslotTable tsId;

    public TimeslotUser() {
    }

    public TimeslotUser(Integer id) {
        this.id = id;
    }
    
    public TimeslotUser(boolean driver, UserTable userId, TimeslotTable tsId) {
        this.driver = driver;
        this.userId = userId;
        this.tsId = tsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDriver() {
        return driver;
    }

    public void setDriver(Boolean driver) {
        this.driver = driver;
    }

    public UserTable getUserId() {
        return userId;
    }

    public void setUserId(UserTable userId) {
        this.userId = userId;
    }

    public TimeslotTable getTsId() {
        return tsId;
    }

    public void setTsId(TimeslotTable tsId) {
        this.tsId = tsId;
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
        if (!(object instanceof TimeslotUser)) {
            return false;
        }
        TimeslotUser other = (TimeslotUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.TimeslotUser[ id=" + id + " ]";
    }
    
}
