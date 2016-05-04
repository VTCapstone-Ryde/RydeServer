/*
 * Created by Patrick Abod on 2016.04.11  * 
 * Copyright © 2016 Patrick Abod. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.Event;
import com.mycompany.entity.Response;
import com.mycompany.entity.Ride;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.EventFacade;
import com.mycompany.session.TimeslotTableFacade;
import com.mycompany.session.UserTableFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    private EntityManager em;

    @EJB
    private UserTableFacade userFacade;
    @EJB
    private TimeslotTableFacade timeslotFacade;
    
    public RideFacadeREST() {
        super(Ride.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Ride entity) {
        super.create(entity);
    }

    @POST
    @Path("request/{fbTok}/{tsId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(@PathParam("fbTok") String fbTok, @PathParam("tsId") Integer tsId, Ride entity) {
        UserTable user = userFacade.findByToken(fbTok);

        List<TimeslotTable> ts = timeslotFacade.findById(tsId);
//        List<TimeslotTable> ts = em.createQuery("SELECT t FROM TimeslotTable t WHERE t.id = :tsId", TimeslotTable.class)
//                .setParameter("tsId", tsId).getResultList();

        entity.setDriverUserId(null);
        entity.setRiderUserId(user);
        entity.setTsId(ts.get(0));

        super.create(entity);

        Response response = new Response(getPosition(user.getId(), ts.get(0).getId()));

        return response;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_JSON})
    public Ride find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Ride> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Ride> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("getposition/{fbTok}/{tsId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getpos(@PathParam("fbTok") String fbTok, @PathParam("tsId") Integer tsId) {
        UserTable user = userFacade.findByToken(fbTok);

        List<TimeslotTable> ts = timeslotFacade.findById(tsId);
//        List<TimeslotTable> ts = em.createQuery("SELECT t FROM TimeslotTable t WHERE t.id = :tsId", TimeslotTable.class)
//                .setParameter("tsId", tsId).getResultList();
        Response response;
        Ride ride;
        if (user != null && !ts.isEmpty()) {
            response = new Response(getPosition(user.getId(), ts.get(0).getId()));
            ride = em.createNamedQuery("Ride.findByRider", Ride.class)
                .setParameter("riderUserId", user.getId())
                .getResultList().get(0);
            if (ride.getActive()) {
                response.setQueueStatus("active");
            }
            else {
                response.setQueueStatus("notInQueue");
            }
            response.setRide(ride);
            return response;
        }
        return new Response();
         
            //Talk with pat

//        Ride ride = querylist.get(0);
//        if (ride.getActive()) {
//            return new Response("active", ride);
//        }
//        return new Response("notInQueue", ride);
                
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @GET
    @Path("getQueue/{timeslotId}/{driverToken}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Ride> getQueueForTimeslot(@PathParam("timeslotId") Integer timeslotId, @PathParam("driverToken") String driverToken) {
        UserTable driver = userFacade.findByToken(driverToken);
        List<Ride> allRides = findAll();
        ArrayList<Ride> ridesForTimeslot = new ArrayList<>();
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

    //TALK W/ PAT
    //Creating event stuff needs to be in this class, or fails.
    @POST
    @Path("endRide/{rideId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Ride endRide(@PathParam("rideId") Integer rideId) {
        // timestamp
        Date date = new Date();

        // find ride to delete
        Ride rideToDelete;
        rideToDelete = find(rideId);

        // if problems creating event, we do not want to delete ride
        Event createdEvent = null;
        try {
//            createdEvent = eventFacade.createRideEvent(rideToDelete, date);
            createdEvent = new Event(date, rideToDelete.getDriverUserId(), rideToDelete.getTsId(), "rideCompleted");
            getEntityManager().persist(createdEvent);
            System.out.println(createdEvent.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (createdEvent != null) {
            getEntityManager().remove(rideToDelete);
            return rideToDelete;
        }
        return null;
    }
    
    public UserTable findByToken(String token) {
        List<UserTable> user = new ArrayList();

        try {
            user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user.isEmpty()) {
            System.out.println("No user found with token: " + token);
            return null;
        } else {
            return user.get(0);
        }
    }
    
    @GET
    @Path("findAllRidesForTimeslot/{tsId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Ride> findAllRidesForTimeslot(@PathParam("tsId") Integer tsId) {
        List<Ride> rides = em.createQuery("SELECT r FROM Ride r WHERE r.tsId.id = :tsId",
                Ride.class).setParameter("tsId", tsId).getResultList();

        return rides;
    }

    public List<Ride> findAllRidesForUser(Integer uId) {
        List<Ride> rides = em.createQuery("SELECT r FROM Ride r WHERE r.riderUserId.id = :uId",
                Ride.class).setParameter("uId", uId).getResultList();

        return rides;
    }

    public Integer getPosition(Integer userId, Integer tsId) {
        List<Ride> rides = findAllRidesForTimeslot(tsId);

        int position = 0;

        for (int i = 0; i < rides.size(); i++) {
            if (rides.get(i).getRiderUserId().getId().equals(userId)) {
                position = i + 1;
            }
        }

        return position;
    }

    @GET
    @Path("rideStatusForUser/{fbToken}")
    @Produces({MediaType.TEXT_PLAIN})
    public String getRideStatusForUser(@PathParam("fbToken") Integer fbToken) {
        UserTable user = userFacade.findByToken(fbToken.toString());
        Ride ride;
        try {
            if (em.createNamedQuery("findByRider", Ride.class)
                    .setParameter("riderUserId", user.getId())
                    .getResultList().isEmpty()) {
                return "notInQueue";
            } else {
                ride = em.createNamedQuery("findByRider", Ride.class)
                        .setParameter("riderUserId", user.getId())
                        .getResultList().get(0);

                if (ride.getActive() == true) {
                    return "active";
                } else {
                    return "pending";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TALK W/ PAT
     *
     * @param fbToken
     * @return
     */
    @GET
    @Path("driverInfo/{fbToken}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDriverInfoForRider(@PathParam("fbToken") String fbToken) {
        UserTable user = userFacade.findByToken(fbToken);
        Ride ride;
        Query q = getEntityManager().createNamedQuery("Ride.findByRider").setParameter("riderUserId", user.getId());
        List<Ride> list = q.getResultList();
        if (list.isEmpty()) {
            return new Response("notInQueue", null);
        } else {
            ride = list.get(0);
            if (!ride.getActive()) {
                return new Response("nonActive", ride);
            }
            return new Response("active", ride);
        }
    }

    @DELETE
    @Path("cancel/{fbTok}")
    public void cancelRide(@PathParam("fbTok") String fbTok) {
        UserTable user = userFacade.findByToken(fbTok);

        List<Ride> rides = findAllRidesForUser(user.getId());

        for (Ride ride : rides) {
            em.remove(ride);
        }
    }

    /**
     * TALK W/ PAT
     *
     * @param timeslotId
     * @return
     */
    @GET
    @Path("getNonActiveQueue/{tsId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Ride> getNonActiveQueueForTimeslot(@PathParam("tsId") Integer tsId) {
        Query q = getEntityManager().createNamedQuery("Ride.getNonActiveQueueForTimeslot").setParameter("tsId", tsId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }

    /**
     * TALK W/ PAT Should be a put?
     *
     * @param timeslotId
     * @return
     */
    @GET
    @Path("startNextRideForTimeslot/{tsId}/{driverTok}")
    @Produces({MediaType.APPLICATION_JSON})
    public Ride startNextRideForTimeslot(@PathParam("tsId") Integer tsId, @PathParam("driverTok") String driverTok) {
        List<Ride> currentNonActiveQueue = getNonActiveQueueForTimeslot(tsId);
        if (!currentNonActiveQueue.isEmpty()) {
            //If there are rides in queue:
            //Set first ride in queue to active
            //Assign specified driver to driver of the ride

            //Need to be sure this gets lowest id (first position in queue)
            Ride ride = currentNonActiveQueue.get(0);
            ride.setActive(true);
            UserTable driver = userFacade.findByToken(driverTok);
            if (driver == null) {
                return null;
            }
            ride.setDriverUserId(driver);
            edit(ride);
            return ride;
        }
        return null;
    }

    /**
     * TALK W/ PAT Finds ride that a driver is assigned to
     *
     * @param driverTok
     * @return
     */
    @GET
    @Path("findRideByDriverTok/{driverTok}")
    @Produces({MediaType.APPLICATION_JSON})
    public Ride findRideByDriverTok(@PathParam("driverTok") String driverTok) {
        Query q = getEntityManager().createNamedQuery("Ride.findByDriverTok").setParameter("driverFbtok", driverTok);
        //Maybe should only do getResultList once for efficiency...
        if (q.getResultList().isEmpty()) {
            return null;
        }
        return (Ride) q.getResultList().get(0);
    }

    /**
     * TALK W/ PAT Cancels the ride a driver is assigned to Keeps ride in queue
     *
     * @param tsId
     * @param driverTok
     */
    @PUT
    @Path("driverCancel/{driverTok}")
    public void driverCancel(@PathParam("driverTok") String driverTok) {
        Ride ride = findRideByDriverTok(driverTok);
        ride.setActive(false);
        ride.setDriverUserId(null);
        edit(ride);
    }
}
