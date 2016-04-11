/*
 * Created by Cameron Gibson on 2016.04.04  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request {
    @XmlElement 
    private String fbTok;
    @XmlElement
    private int gID;
    @XmlElement
    private int tID;
    @XmlElement
    private double startLat;
    @XmlElement
    private double startLon;
    @XmlElement
    private double endLat;
    @XmlElement
    private double endLon;

    public String getFbTok() {
        return fbTok;
    }

    public void setFbTok(String fbTok) {
        this.fbTok = fbTok;
    }

    public int getgID() {
        return gID;
    }

    public void setgID(int gID) {
        this.gID = gID;
    }

    public int gettID() {
        return tID;
    }

    public void settID(int tID) {
        this.tID = tID;
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

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }
}
