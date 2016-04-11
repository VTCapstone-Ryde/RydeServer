/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.TimeslotUser;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.TimeslotUserFacade;
import java.util.ArrayList;
import java.util.Collections;
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
@Path("/timeslot")
public class TimeslotTableFacadeREST extends AbstractFacade<TimeslotTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();
    
    private final TimeslotUserFacade timeslotUserFacade = new TimeslotUserFacade();
    private final GroupTimeslotFacade groupTimeslotFacade = new GroupTimeslotFacade();

    public TimeslotTableFacadeREST() {
        super(TimeslotTable.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(TimeslotTable entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, TimeslotTable entity) {
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
    public TimeslotTable find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
    /*
        The following methods are added to the generated code
    */
    
    @GET
    @Path("timeslotsForGroup/{groupId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForGroup(@PathParam("groupId") String groupdId) {
        List<GroupTimeslot> timeslotIds = groupTimeslotFacade.findTimeslotsForGroup(Integer.parseInt(groupdId));
        ArrayList<TimeslotTable> timeslots = new ArrayList<TimeslotTable>();
        for (GroupTimeslot i : timeslotIds) {
            timeslots.add(i.getTsId());
        }
        System.out.println(timeslots);
        return timeslots;
    }
    
    @GET
    @Path("timeslotsForUser/{userId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForUser(@PathParam("userId") String userId) {
        List<TimeslotUser> timeslotIds = timeslotUserFacade.findTimeslotsForUser(Integer.parseInt(userId));
        ArrayList<TimeslotTable> timeslots = new ArrayList<TimeslotTable>();
        for (TimeslotUser i : timeslotIds) {
            timeslots.add(i.getTsId());
        }
        System.out.println(timeslots);
        return timeslots;
    }

    
}
