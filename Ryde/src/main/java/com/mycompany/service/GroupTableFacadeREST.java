/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.GroupUserFacade;
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
@Path("/group")
public class GroupTableFacadeREST extends AbstractFacade<GroupTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();
    //Our facades for relational information
    private final GroupUserFacade groupUserFacade = new GroupUserFacade();
    private final GroupTimeslotFacade groupTimeslotFacade = new GroupTimeslotFacade();
    
    public GroupTableFacadeREST() {
        super(GroupTable.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(GroupTable entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, GroupTable entity) {
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
    public GroupTable find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<GroupTable> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<GroupTable> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    //The following has been added to the auto-generated code
    public GroupUserFacade getGroupUserFacade() {
        return groupUserFacade;
    }

    public GroupTimeslotFacade getGroupTimeslotFacade() {
        return groupTimeslotFacade;
    }
    
    @GET
    @Path("user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<GroupTable> findGroupsForUser(@PathParam("id") Integer id) {
        return getGroupUserFacade().findGroupsForUser(id);
    }
    
    @GET
    @Path("timeslot/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public GroupTable findGroupForTimeslot(@PathParam("id") Integer id) {
        return getGroupTimeslotFacade().findGroupForTimeslot(id);
    }
    @GET
    @Path("admin/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserTable> findAdminsForGroup(@PathParam("id") Integer id) {
        return getGroupUserFacade().findAdminsForGroup(id);
    }    
}
