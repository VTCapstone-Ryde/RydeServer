/*
 * Created by Peter Cho on 2016.04.11  * 
 * Copyright © 2016 Peter Cho. All rights reserved. * 
 */
package com.mycompany.entity;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Cho
 */
@XmlRootElement
public class Response implements Serializable{
    @XmlElement
    private Integer tsId;
    @XmlElement
    private Integer numDrivers;
    @XmlElement
    private Integer queueSize;
    @XmlElement
    private Integer position;
    @XmlElement
    private String groupName;
    @XmlElement
    private boolean inQueue;
    @XmlElement
    private boolean joinTADSuccess;
    
    public Response() {
        
    }
    
    public Response(Integer position) {
        this.position = position;
    }
    
    public Response(Integer tsId, Integer numDrivers, Integer queueSize,
            Integer position, String groupName) {
        this.tsId = tsId;
        this.numDrivers = numDrivers;
        this.queueSize = queueSize;
        this.position = position;
        this.groupName = groupName;
    }

    public Integer getTsId() {
        return tsId;
    }

    public void setTsId(Integer tsId) {
        this.tsId = tsId;
    }

    public Integer getNumDrivers() {
        return numDrivers;
    }

    public void setNumDrivers(Integer numDrivers) {
        this.numDrivers = numDrivers;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }

    public boolean isJoinTADSuccess() {
        return joinTADSuccess;
    }

    public void setJoinTADSuccess(boolean joinTADSuccess) {
        this.joinTADSuccess = joinTADSuccess;
    }
}
