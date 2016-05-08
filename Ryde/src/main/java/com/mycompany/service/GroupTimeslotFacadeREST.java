/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTimeslot;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
 * @author Patrick Abod
 */
@Stateless
@Path("/grouptimeslot")
public class GroupTimeslotFacadeREST extends AbstractFacade<GroupTimeslot> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;

    public GroupTimeslotFacadeREST() {
        super(GroupTimeslot.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(GroupTimeslot entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, GroupTimeslot entity) {
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
    public GroupTimeslot find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<GroupTimeslot> findAll() {
        return super.findAll();
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
    
//    /**
//     * TALK W/ PAT
//     * @param userId
//     * @param groupId 
//     */
//    @POST
//    @Path("/createByTimeslotAndGroup/{timeslotId}/{groupId}")
//    public void createByTimeslotAndGroup(@PathParam("timeslotId") Integer userId, @PathParam("groupId") Integer groupId) {
//        UserTable user = this.findUserById(userId);
//        GroupTable group = this.findGroupById(groupId);
//        RequestUser ru = new RequestUser();
//        ru.setGroupId(group);
//        ru.setUserId(user);
//        create(ru);
//    } 
    
}
