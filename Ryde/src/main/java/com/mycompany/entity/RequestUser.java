/*
 * Created by Jared Deiner on 2016.04.20  * 
 * Copyright © 2016 Jared Deiner. All rights reserved. * 
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
 * @author archer
 */
@Entity
@Table(name = "RequestUser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestUser.findAll", query = "SELECT r FROM RequestUser r"),
    @NamedQuery(name = "RequestUser.findById", query = "SELECT r FROM RequestUser r WHERE r.id = :id"),
    @NamedQuery(name = "RequestUser.findByAdmin", query = "SELECT r FROM RequestUser r WHERE r.admin = :admin")})
public class RequestUser implements Serializable {

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

    public RequestUser() {
    }

    public RequestUser(Integer id) {
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
        if (!(object instanceof RequestUser)) {
            return false;
        }
        RequestUser other = (RequestUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.RequestUser[ id=" + id + " ]";
    }
    
}
