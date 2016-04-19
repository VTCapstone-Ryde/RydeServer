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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cameron
 */
@Entity
@Table(name = "Group_Table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupTable.findAll", query = "SELECT g FROM GroupTable g"),
    @NamedQuery(name = "GroupTable.findById", query = "SELECT g FROM GroupTable g WHERE g.id = :id"),
    @NamedQuery(name = "GroupTable.findByTitle", query = "SELECT g FROM GroupTable g WHERE g.title = :title"),
    @NamedQuery(name = "GroupTable.findByDescription", query = "SELECT g FROM GroupTable g WHERE g.description = :description"),
    @NamedQuery(name = "GroupTable.findByDirectoryPath", query = "SELECT g FROM GroupTable g WHERE g.directoryPath = :directoryPath")})
public class GroupTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "directory_path")
    private String directoryPath;

    public GroupTable() {
    }

    public GroupTable(Integer id) {
        this.id = id;
    }

    public GroupTable(Integer id, String title, String description, String directoryPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.directoryPath = directoryPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
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
        if (!(object instanceof GroupTable)) {
            return false;
        }
        GroupTable other = (GroupTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entity.GroupTable[ id=" + id + " ]";
    }
       
}
