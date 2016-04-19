/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cloud
 */
@Entity
@Table(name = "GroupUser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupUser.findAll", query = "SELECT g FROM GroupUser g"),
    @NamedQuery(name = "GroupUser.findByGroupAndUser", query = "SELECT g FROM GroupUser g WHERE g.userId = :userId AND g.groupId = :groupId"),
    @NamedQuery(name = "GroupUser.findByGroupAndUserIDs", query = "SELECT g FROM GroupUser g WHERE g.userId.id = :userId AND g.groupId.id = :groupId"),
    @NamedQuery(name = "GroupUser.findById", query = "SELECT g FROM GroupUser g WHERE g.id = :id"),
    @NamedQuery(name = "GroupUser.findByUserId", query = "SELECT g.groupId FROM GroupUser g WHERE g.userId.id = :id"),
    @NamedQuery(name = "GroupUser.findByUsersByGroupId", query = "SELECT g.userId FROM GroupUser g WHERE g.groupId.id = :id"),
    @NamedQuery(name = "GroupUser.findAdminsByGroupId", query = "SELECT g.userId FROM GroupUser g WHERE g.admin = 1 AND g.groupId.id = :id"),
    @NamedQuery(name = "GroupUser.findByAdmin", query = "SELECT g FROM GroupUser g WHERE g.admin = :admin")})
public class GroupUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "admin")
    private Boolean admin;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserTable userId;
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GroupTable groupId;

    public GroupUser() {
    }

    public GroupUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public UserTable getUserId() {
        return userId;
    }

    public void setUserId(UserTable userId) {
        this.userId = userId;
    }

    public GroupTable getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupTable groupId) {
        this.groupId = groupId;
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
        if (!(object instanceof GroupUser)) {
            return false;
        }
        GroupUser other = (GroupUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.GroupUser[ id=" + id + " ]";
    }
    
}
