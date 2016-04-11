/*
 * Created by Cameron Gibson on 2016.04.04  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupUser;
import com.mycompany.entity.TimeslotUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupUserFacade;
import com.mycompany.session.TimeslotUserFacade;
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
 * @author cameron
 */
@Stateless
@Path("/user")
public class UserTableFacadeREST extends AbstractFacade<UserTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    private final TimeslotUserFacade timeslotUserFacade = new TimeslotUserFacade();
    private final GroupUserFacade groupUserFacade = new GroupUserFacade();
    
    public UserTableFacadeREST() {
        super(UserTable.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(UserTable entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, UserTable entity) {
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
    public UserTable find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserTable> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserTable> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    The following methods were added after code generation
    */

    public TimeslotUserFacade getTimeslotUserFacade() {
        return timeslotUserFacade;
    }

    public GroupUserFacade getGroupUserFacade() {
        return groupUserFacade;
    }
    
    @GET
    @Path("validateToken/{token}")
    @Produces({MediaType.TEXT_PLAIN})
    public String validateToken(@PathParam("token") String token) {
        if (findByToken(token) == null) {
            return "false";
        }
        else {
            return "true";
        }
    }
    
    @GET
    @Path("inGroup/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserTable> findUsersInGroup(@PathParam("groupId") String groupId) {
        List<GroupUser> userIds = groupUserFacade.findUsersForGroup(Integer.parseInt(groupId));
        List<UserTable> users = Collections.EMPTY_LIST;
        for (GroupUser g : userIds) {
            users.add(find(g.getUserId()));
        }
        System.out.println(users);
        return users;
    }
    
    @GET
    @Path("inTimeslot/{timeslotId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserTable> findUsersInTimeslot(@PathParam("timeslotId") String timeslotId) {
        List<TimeslotUser> userIds = timeslotUserFacade.findUsersForTimeslot(Integer.parseInt(timeslotId));
        List<UserTable> users = Collections.EMPTY_LIST;
        for (TimeslotUser t : userIds) {
            users.add(find(t.getUserId()));
        }
        System.out.println(users);
        return users;
    }
    
    public UserTable findByToken(String token) {
        try {
            if (em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token)
                    .getResultList().isEmpty()) {
                System.out.println("No user found with token: " + token);
                return null;
            }
            else {
                 return em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token).getResultList().get(0);
                            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }
    
}
