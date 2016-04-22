/*
 * Created by Cameron Gibson on 2016.04.21  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.RequestUser;
import com.mycompany.entity.RequestUser;
import com.mycompany.entity.UserTable;
import java.util.List;
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
 * @author Cameron
 */
@Stateless
@Path("com.mycompany.entity.requestuser")
public class RequestUserFacadeREST extends AbstractFacade<RequestUser> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;

    public RequestUserFacadeREST() {
        super(RequestUser.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(RequestUser entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, RequestUser entity) {
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
    public RequestUser find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<RequestUser> findAll() {
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
    
    //The following was added to the generated code
    @GET
    @Path("/findByUserAndGroup/{userId}/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public RequestUser findByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        Query q = getEntityManager().createNamedQuery("RequestUser.findByGroupAndUserIDs").
                setParameter("userId", userId).setParameter("groupId", groupId);
        if (!q.getResultList().isEmpty()) {
            return (RequestUser) q.getSingleResult();
        }
        return null;
    }
    
    @POST
    @Path("/createByUserAndGroup/{userId}/{groupId}")
    public void createByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        RequestUser ru = findByUserAndGroup(userId, groupId);
        super.create(ru);
    }   
    
    @DELETE
    @Path("/removeByUserAndGroup/{userId}/{groupId}")
    public void removeByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        RequestUser ru = findByUserAndGroup(userId, groupId);
        super.remove(ru);
    }
    
}
