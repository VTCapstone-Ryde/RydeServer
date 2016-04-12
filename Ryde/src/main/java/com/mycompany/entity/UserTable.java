/*
 * Created by Cameron Gibson on 2016.04.04  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cameron
 */
@Entity
@Table(name = "User_Table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserTable.findAll", query = "SELECT u FROM UserTable u"),
    @NamedQuery(name = "UserTable.findById", query = "SELECT u FROM UserTable u WHERE u.id = :id"),
    @NamedQuery(name = "UserTable.findByDriverStatus", query = "SELECT u FROM UserTable u WHERE u.driverStatus = :driverStatus"),
    @NamedQuery(name = "UserTable.findByLastName", query = "SELECT u FROM UserTable u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "UserTable.findByFirstName", query = "SELECT u FROM UserTable u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "UserTable.findByEntireName", query = "SELECT u FROM UserTable u WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE :name"),
    @NamedQuery(name = "UserTable.findByFbTok", query = "SELECT u FROM UserTable u WHERE u.fbTok = :fbTok"),
    @NamedQuery(name = "UserTable.findByFbId", query = "SELECT u FROM UserTable u WHERE u.fbId = :fbId"),
    @NamedQuery(name = "UserTable.findByPhoneNumber", query = "SELECT u FROM UserTable u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "UserTable.findByCarMake", query = "SELECT u FROM UserTable u WHERE u.carMake = :carMake"),
    @NamedQuery(name = "UserTable.findByCarModel", query = "SELECT u FROM UserTable u WHERE u.carModel = :carModel"),
    @NamedQuery(name = "UserTable.findByCarColor", query = "SELECT u FROM UserTable u WHERE u.carColor = :carColor")})
public class UserTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "driver_status")
    private Boolean driverStatus;
    @Size(max = 64)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 64)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 256)
    @Column(name = "fb_tok")
    private String fbTok;
    @Size(max = 256)
    @Column(name = "fb_id")
    private String fbId;
    @Size(max = 15)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Size(max = 32)
    @Column(name = "car_make")
    private String carMake;
    @Size(max = 32)
    @Column(name = "car_model")
    private String carModel;
    @Size(max = 32)
    @Column(name = "car_color")
    private String carColor;

    public UserTable() {
    }

    public UserTable(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(Boolean driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFbTok() {
        return fbTok;
    }

    public void setFbTok(String fbTok) {
        this.fbTok = fbTok;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
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
        if (!(object instanceof UserTable)) {
            return false;
        }
        UserTable other = (UserTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.UserTable[ id=" + id + " ]";
    }
    
}
