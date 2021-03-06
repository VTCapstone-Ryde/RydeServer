/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTableFacade;
import com.mycompany.session.GroupUserFacade;
import com.mycompany.session.UserTableFacade;
import java.util.List;
import javax.ejb.EJB;
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
@Path("/groupuser")
public class GroupUserFacadeREST extends AbstractFacade<GroupUser> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;

    @EJB
    private GroupUserFacade guFacade;
    @EJB
    private UserTableFacade userFacade;
    @EJB
    private GroupTableFacade groupFacade;
    
    public GroupUserFacadeREST() {
        super(GroupUser.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(GroupUser entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, GroupUser entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }
    
    @DELETE
    @Path("/{userId}/{groupId}")
    public void removeByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        GroupUser gu = findByGroupAndUser(userId, groupId);
        if (gu != null) {
            super.remove(gu);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public GroupUser find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<GroupUser> findAll() {
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
    
    @GET
    @Path("/{userId}/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public GroupUser findByGroupAndUser(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        UserTable user = userFacade.findById(userId);
        GroupTable group = groupFacade.findById(groupId);
        
        return guFacade.findByGroupAndUser(group, user);
//        Query q = getEntityManager().createNamedQuery("GroupUser.findByGroupAndUserIDs").
//                setParameter("userId", userId).setParameter("groupId", groupId);
//        if (!q.getResultList().isEmpty()) {
//            return (GroupUser) q.getSingleResult();
//        }
//        return null;
    }
    
    @PUT
    @Path("/admin/{userId}/{groupId}/{admin}")
    public void setUserAsAdmin(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId, @PathParam("admin") Integer adminParam) {
        GroupUser gu = findByGroupAndUser(userId, groupId);
        Boolean admin = false;
        if (adminParam == 1) {
            admin = true;
        }
        gu.setAdmin(admin);
        edit(gu);
    }
    
}
