/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.GroupUserFacade;
import com.mycompany.session.TimeslotUserFacade;
import java.util.List;
import javax.ejb.EJB;
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
@Path("/group")
public class GroupTableFacadeREST extends AbstractFacade<GroupTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;
    @EJB
    private GroupTimeslotFacade gtFacade;
    @EJB
    private TimeslotUserFacade tuFacade;
    @EJB
    private GroupUserFacade guFacade;
    
    public GroupTableFacadeREST() {
        super(GroupTable.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(GroupTable entity) {
        super.create(entity);
    }
    
    @POST
    @Path("createWithReturn")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public GroupTable createWithReturn(GroupTable entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        return entity;
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
    
    @GET
    @Path("user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<GroupTable> findGroupsForUser(@PathParam("id") Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findByUserId").setParameter("id", id);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    @GET
    @Path("timeslot/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public GroupTable findGroupForTimeslot(@PathParam("id") Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupTimeslot.findByTimeslotId").setParameter("id", id);
        q.setFirstResult(0);
        List<GroupTimeslot> result = q.getResultList();
        //TODO add empty result handling
        return result.get(0).getGroupId();
    }
    
    @GET
    @Path("admin/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserTable> findAdminsForGroup(@PathParam("id") Integer id) {
        Query q = getEntityManager().createNamedQuery("GroupUser.findAdminsByGroupId").setParameter("id", id);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }

    @GET
    @Path("title/{title}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<GroupTable> findGroupsByTitle(@PathParam("title") String title) {
        String addSpacesToTitle = title.replaceAll("\\+", " ");
        Query q = getEntityManager().createNamedQuery("GroupTable.findByTitle").setParameter("title", addSpacesToTitle);
        q.setFirstResult(0);
        //TODO add empty result handling
        return q.getResultList();
    }
    
    @GET
    @Path("/findRequestUserForGroup/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserTable> findRequestUserForGroup (@PathParam("groupId") Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findUsersByRequestsForGroupId").setParameter("groupId", groupId);
        q.setFirstResult(0);
        //TODO add empty result handling
        List list = q.getResultList();
        return q.getResultList();
    }
    
    @DELETE
    @Path("/removeUserFromGroup/{userId}/{groupId}")
    public void removeUserFromGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        List<TimeslotTable> groupTimeslots = gtFacade.findTimeslotsForGroup(groupId);
        for (TimeslotTable ts: groupTimeslots) {
            tuFacade.removeByUserAndTimeslot(userId, ts.getId());
        }
        guFacade.removeByUserAndGroupIds(userId, groupId);
    }
}
