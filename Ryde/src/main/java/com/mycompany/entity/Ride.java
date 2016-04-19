/*
 * Created by Cameron Gibson on 2016.04.04  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cameron
 */
@Entity
@Table(name = "Ride")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ride.findAll", query = "SELECT r FROM Ride r"),
    @NamedQuery(name = "Ride.findById", query = "SELECT r FROM Ride r WHERE r.id = :id"),
    @NamedQuery(name = "Ride.findByStartLat", query = "SELECT r FROM Ride r WHERE r.startLat = :startLat"),
    @NamedQuery(name = "Ride.findByStartLon", query = "SELECT r FROM Ride r WHERE r.startLon = :startLon"),
    @NamedQuery(name = "Ride.findByEndLat", query = "SELECT r FROM Ride r WHERE r.endLat = :endLat"),
    @NamedQuery(name = "Ride.findByEndLon", query = "SELECT r FROM Ride r WHERE r.endLon = :endLon"),
    @NamedQuery(name = "Ride.findByRider", query = "SELECT r FROM Ride r WHERE r.riderUserId = :riderUserId"),

})
public class Ride implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private boolean active;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_lat")
    private double startLat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_lon")
    private double startLon;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "end_lat")
    private Double endLat;
    @Column(name = "end_lon")
    private Double endLon;
    @JoinColumn(name = "driver_user_id", referencedColumnName = "id")
    @ManyToOne
    private UserTable driverUserId;
    @JoinColumn(name = "rider_user_id", referencedColumnName = "id")
    @ManyToOne
    private UserTable riderUserId;
    @JoinColumn(name = "ts_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TimeslotTable tsId;

    public Ride() {
    }

    public Ride(Integer id) {
        this.id = id;
    }

    public Ride(Integer id, double startLat, double startLon) {
        this.id = id;
        this.startLat = startLat;
        this.startLon = startLon;
    }
    
    public Ride(double startLat, double startLon, double endLat, double endLon, 
            UserTable riderUserID, TimeslotTable tsId) {
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon; 
        this.riderUserId = riderUserID;
        this.tsId = tsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    public Double getEndLat() {
        return endLat;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }

    public Double getEndLon() {
        return endLon;
    }

    public void setEndLon(Double endLon) {
        this.endLon = endLon;
    }

    public UserTable getDriverUserId() {
        return driverUserId;
    }

    public void setDriverUserId(UserTable driverUserId) {
        this.driverUserId = driverUserId;
    }

    public UserTable getRiderUserId() {
        return riderUserId;
    }

    public void setRiderUserId(UserTable riderUserId) {
        this.riderUserId = riderUserId;
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
        if (!(object instanceof Ride)) {
            return false;
        }
        Ride other = (Ride) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.Ride[ id=" + id + " ]";
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
