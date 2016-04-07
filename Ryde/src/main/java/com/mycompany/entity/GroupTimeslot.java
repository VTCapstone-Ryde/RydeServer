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
@Table(name = "GroupTimeslot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupTimeslot.findAll", query = "SELECT g FROM GroupTimeslot g"),
    @NamedQuery(name = "GroupTimeslot.findByTimeslotId", query = "SELECT g FROM GroupTimeslot g WHERE g.groupId.id = :id"),
    @NamedQuery(name = "GroupTimeslot.findById", query = "SELECT g FROM GroupTimeslot g WHERE g.id = :id")})
public class GroupTimeslot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GroupTable groupId;
    @JoinColumn(name = "ts_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TimeslotTable tsId;

    public GroupTimeslot() {
    }

    public GroupTimeslot(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GroupTable getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupTable groupId) {
        this.groupId = groupId;
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
        if (!(object instanceof GroupTimeslot)) {
            return false;
        }
        GroupTimeslot other = (GroupTimeslot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.GroupTimeslot[ id=" + id + " ]";
    }
    
}
