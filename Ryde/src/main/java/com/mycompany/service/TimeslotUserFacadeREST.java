/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.Response;
import com.mycompany.entity.Ride;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.TimeslotUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.TimeslotUserFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
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
 * @author cloud
 */
@Stateless
@Path("/timeslotuser")
public class TimeslotUserFacadeREST extends AbstractFacade<TimeslotUser> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    private final GroupTimeslotFacade gtFacade = new GroupTimeslotFacade();
    private final TimeslotUserFacade tuFacade = new TimeslotUserFacade();
    private final RideFacadeREST rideFacade = new RideFacadeREST();

    public TimeslotUserFacadeREST() {
        super(TimeslotUser.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(TimeslotUser entity) {
        super.create(entity);
    }

    @POST
    @Path("jointad/{fbTok}/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response joinTad(@PathParam("fbTok") String fbTok, @PathParam("code") String passCode) {
        Response response = new Response();

        List<TimeslotTable> ts = em.createQuery("SELECT t FROM TimeslotTable t WHERE t.passcode = :passcode", TimeslotTable.class).
                setParameter("passcode", passCode).getResultList();

        if (!ts.isEmpty()) {
            UserTable user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", fbTok).getSingleResult();

            if (tuFacade.userInTAD(user, ts.get(0))) {
                response.setJoinTADSuccess(false);
                return response;
            } else {
                TimeslotUser tu = new TimeslotUser(false, user, ts.get(0));

                super.create(tu);

                response.setJoinTADSuccess(true);
                return response;
            }
        } else {
            response.setJoinTADSuccess(false);
            return response;
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, TimeslotUser entity) {
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
    public TimeslotUser find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotUser> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotUser> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
    public List<TimeslotTable> findTimeslotsForUser(Integer userId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findTimeslotById").setParameter("userId", userId);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    public GroupTable findGroupForTimeslot(Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupTimeslot.findByTimeslotId").setParameter("id", id);
        q.setFirstResult(0);
        List<GroupTimeslot> result = q.getResultList();
        //TODO add empty result handling
        return result.get(0).getGroupId();
    }
    
     @GET
    @Path("testGetTad/{fbTok}")
    @Produces({MediaType.APPLICATION_JSON})
    public TimeslotTable getTadsTest(@PathParam("fbTok") String fbTok) {
        List<Response> responses = new ArrayList<Response>();

        UserTable user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                .setParameter("fbTok", fbTok).getSingleResult();

        List<TimeslotTable> timeslots = this.findTimeslotsForUser(user.getId());
        return timeslots.get(0);
        
//        for (int i = 0; i < timeslots.size(); i++) {
//            Integer tsId = timeslots.get(i).getId();
//
//            GroupTable group = gtFacade.findGroupForTimeslot(tsId);
//            List<UserTable> drivers = tuFacade.findDriversForTimeslot(tsId);
//            List<Ride> rides = rideFacade.findAllRidesForTimeslot(tsId);
//
//            responses.add(new Response(tsId, drivers.size(), rides.size(), null, group.getTitle()));
//        }
//
//        return responses;
    }

    @GET
    @Path("gettads/{fbTok}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Response> getTads(@PathParam("fbTok") String fbTok) {
        List<Response> responses = new ArrayList<Response>();

        UserTable user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                .setParameter("fbTok", fbTok).getSingleResult();

        List<TimeslotTable> timeslots = tuFacade.findTimeslotsForUser(user.getId());
        
        for (int i = 0; i < timeslots.size(); i++) {
            Integer tsId = timeslots.get(i).getId();

            GroupTable group = gtFacade.findGroupForTimeslot(tsId);
            List<UserTable> drivers = tuFacade.findDriversForTimeslot(tsId);
            List<Ride> rides = rideFacade.findAllRidesForTimeslot(tsId);

            responses.add(new Response(tsId, drivers.size(), rides.size(), null, group.getTitle()));
        }

        return responses;
    }

    /**
     * TALK W/ PAT
     *
     * @param token
     * @return
     */
    public UserTable findUserByToken(String token) {
        try {
            if (em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token)
                    .getResultList().isEmpty()) {
                System.out.println("No user found with token: " + token);
                return null;
            } else {
                return em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                        .setParameter("fbTok", token).getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TALK W/ PAT
     *
     * @param userFbTok
     * @param tsId
     * @param isDriver
     */
    @POST
    @Path("createWithParams/{userFbTok}/{tsId}/{isDriver}")
    public void createWithParams(@PathParam("userFbTok") String userFbTok, @PathParam("tsId") Integer tsId, @PathParam("isDriver") Integer isDriver) {
        UserTable user = findUserByToken(userFbTok);
        Query q = getEntityManager().createNamedQuery("TimeslotTable.findById").setParameter("id", tsId);
        if (q.getResultList().isEmpty()) {
            return;
        }
        TimeslotTable ts = (TimeslotTable) q.getResultList().get(0);
        Boolean driver = false;
        if (isDriver == 1) {
            driver = true;
        }
        TimeslotUser tsu = new TimeslotUser(driver, user, ts);
        create(tsu);
    }
}
