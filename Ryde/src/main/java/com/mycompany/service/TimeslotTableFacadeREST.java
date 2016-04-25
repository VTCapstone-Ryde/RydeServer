/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.TimeslotUserFacade;
import com.mycompany.session.UserTableFacade;
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
@Path("/timeslot")
public class TimeslotTableFacadeREST extends AbstractFacade<TimeslotTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();
    
    private final TimeslotUserFacade timeslotUserFacade = new TimeslotUserFacade();
    private final GroupTimeslotFacade groupTimeslotFacade = new GroupTimeslotFacade();
    private final UserTableFacade userTableFacade = new UserTableFacade();

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
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForGroupEdit(@PathParam("groupId") String groupId) {
        return groupTimeslotFacade.findTimeslotsForGroup(Integer.parseInt(groupId));

    }
    
    @GET
    @Path("timeslotsForUser/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForUserEdit(@PathParam("userId") String userId) {
        return timeslotUserFacade.findTimeslotsForUser(Integer.parseInt(userId));
    }

    @GET
    @Path("timeslotsForToken/{token}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForToken(@PathParam("token") String token) {
        UserTable ut = userTableFacade.findByToken(token);
        int userId = ut.getId();
        List<TimeslotTable> timeslots = timeslotUserFacade.findTimeslotsForUser(userId);
        return timeslots;
    }
    
    @GET
    @Path("/findDriversForTimeslot/{tsId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserTable> findDriversForTimeslot(@PathParam("tsId") Integer tsId) {
        Query q = getEntityManager().createNamedQuery("TimeslotUser.findDriversByTimeslotId").setParameter("tsId", tsId).setParameter("driver", true);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
}
