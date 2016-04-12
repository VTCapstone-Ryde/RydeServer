/*
 * Created by Patrick Abod on 2016.04.11  * 
 * Copyright © 2016 Patrick Abod. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.Event;
import com.mycompany.entity.Ride;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.EventFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author patrickabod
 */
@Stateless
@Path("ride")
public class RideFacadeREST extends AbstractFacade<Ride> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    private EventFacade eventFacade = new EventFacade();
    
    public RideFacadeREST() {
        super(Ride.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Ride entity) {
        super.create(entity);
    }
    
    @POST
    @Path("request/{fbTok}/{tsId}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(@PathParam("fbTok") String fbTok, @PathParam("tsId") Integer tsId, Ride entity) {
        UserTable user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
            .setParameter("fbTok", fbTok).getSingleResult();
        
        TimeslotTable ts = em.createQuery("SELECT t FROM TimeslotTable t WHERE t.id = :tsId", TimeslotTable.class)
            .setParameter("tsId", tsId).getSingleResult();
        
        entity.setDriverUserId(null);
        entity.setRiderUserId(user);
        entity.setTsId(ts);
        
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Ride entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Ride find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ride> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ride> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("getQueue/{timeslotId}/{driverToken}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Ride> getQueueForTimeslot(@PathParam("timeslotId") Integer timeslotId, @PathParam("driverToken") Integer driverToken) {
        UserTable driver = findByToken(Integer.toString(driverToken));
        List<Ride> allRides = findAll();
        ArrayList<Ride> ridesForTimeslot = new ArrayList<Ride>();
        for (Ride i : allRides) {
            if (timeslotId.equals(i.getTsId().getId())) {
                ridesForTimeslot.add(i);
            }
        }
        if (!ridesForTimeslot.isEmpty()) {
            Ride activeRide = ridesForTimeslot.get(0);
            activeRide.setActive(true);
            activeRide.setDriverUserId(driver);
            em.merge(activeRide);
        }
        System.out.println(ridesForTimeslot);
        return ridesForTimeslot;
    }
    
    @POST
    @Path("endRide/{rideId}")
    public Ride endRide(@PathParam("rideId") Integer rideId) {
        // timestamp
        Date date = new Date();        
        
        // find ride to delete
        Ride rideToDelete;
        rideToDelete = find(rideId);
        
        // if problems creating event, we do not want to delete ride
        Event createdEvent = null;
        try {
            createdEvent = eventFacade.createRideEvent(rideToDelete, date);
            System.out.println(createdEvent.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (createdEvent != null) {
            em.remove(rideToDelete);
            return rideToDelete;
        }
        return null; 
    }
    
    public UserTable findByToken(String token) {
        try {
            if (em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token)
                    .getResultList().isEmpty()) {
                System.out.println("No user found with token: " + token);
                return null;
            }
            else {
                 return em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token).getResultList().get(0);
                            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }
}
