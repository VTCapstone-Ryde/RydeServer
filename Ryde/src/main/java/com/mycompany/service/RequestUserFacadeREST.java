/*
 * Created by Cameron Gibson on 2016.04.21  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.RequestUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTableFacade;
import com.mycompany.session.RequestUserFacade;
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
 * @author Cameron
 * @author Patrick Abod
 */
@Stateless
@Path("requestuser")
public class RequestUserFacadeREST extends AbstractFacade<RequestUser> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;
    
    @EJB
    private RequestUserFacade ruFacade;
    @EJB
    private UserTableFacade userFacade;
    @EJB
    private GroupTableFacade groupFacade;

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
    
    private UserTable findUserById(Integer id) {
        return userFacade.findById(id);
//        List<UserTable> list = getEntityManager().createNamedQuery("UserTable.findById").setParameter("id", id).getResultList();
//        if (list.isEmpty()) {
//            return null;
//        }
//        return list.get(0);
    }
    private GroupTable findGroupById(Integer id) {
        return groupFacade.findById(id);
//        List<GroupTable> list = getEntityManager().createNamedQuery("GroupTable.findById").setParameter("id", id).getResultList();
//        if (list.isEmpty()) {
//            return null;
//        }
//        return list.get(0);
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
        return ruFacade.findByUserAndGroup(userId, groupId);
//        Query q = getEntityManager().createNamedQuery("RequestUser.findByGroupAndUserIDs").
//                setParameter("userId", userId).setParameter("groupId", groupId);
//        if (!q.getResultList().isEmpty()) {
//            return (RequestUser) q.getSingleResult();
//        }
//        return null;
    }
    
    @POST
    @Path("/createByUserAndGroup/{userId}/{groupId}")
    public void createByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        UserTable user = findUserById(userId);
        GroupTable group = findGroupById(groupId);
        RequestUser ru = new RequestUser();
        ru.setGroupId(group);
        ru.setUserId(user);
        create(ru);
    }   
    
    @DELETE
    @Path("/removeByUserAndGroup/{userId}/{groupId}")
    public void removeByUserAndGroup(@PathParam("userId") Integer userId, @PathParam("groupId") Integer groupId) {
        RequestUser ru = findByUserAndGroup(userId, groupId);
        if (ru != null) {
            super.remove(ru);
        }
    }
    
}
