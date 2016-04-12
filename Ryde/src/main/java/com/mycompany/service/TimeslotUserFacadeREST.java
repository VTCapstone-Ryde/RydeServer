/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTable;
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
    
    private GroupTimeslotFacade gtFacade = new GroupTimeslotFacade();
    private TimeslotUserFacade tuFacade = new TimeslotUserFacade();
    private RideFacadeREST rideFacade = new RideFacadeREST();
    
    public TimeslotUserFacadeREST() {
        super(TimeslotUser.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(TimeslotUser entity) {
        super.create(entity);
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
            List<TimeslotTable> drivers = tuFacade.findDriversForTimeslot(tsId);
            List<Ride> rides = rideFacade.findAllRidesForTimeslot(tsId);
            
            responses.add(new Response(tsId, drivers.size(), rides.size(), null, group.getTitle()));
        }
        
        return responses;
    }
}
