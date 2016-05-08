/*
 * Created by Cameron Gibson on 2016.04.04  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupUser;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.TimeslotUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.EventFacade;
import com.mycompany.session.UserTableFacade;
import java.util.ArrayList;
import java.util.Date;
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
 * @author cameron
 * @author Patrick Abod
 */
@Stateless
@Path("/user")
public class UserTableFacadeREST extends AbstractFacade<UserTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private EntityManager em;

    @EJB
    UserTableFacade userFacade;
    
    @EJB
    EventFacade eventFacade;

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
    @GET
    @Path("validateToken/{token}")
    @Produces({MediaType.TEXT_PLAIN})
    public String validateToken(@PathParam("token") String token) {
        if (findByToken(token) == null) {
            return "false";
        } else {
            return "true";
        }
    }

    @GET
    @Path("inGroup/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserTable> findUsersInGroup(@PathParam("groupId") Integer groupId) {
        List<GroupUser> groupUsers = em.createQuery("SELECT gu FROM GroupUser gu WHERE gu.groupId.id = :gId", GroupUser.class)
                .setParameter("gId", groupId).getResultList();
        ArrayList<UserTable> users = new ArrayList<>();
        for (GroupUser gu : groupUsers) {
            users.add(gu.getUserId());
        }
        return users;
    }

    @GET
    @Path("inTimeslot/{timeslotId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserTable> findUsersInTimeslot(@PathParam("timeslotId") Integer timeslotId) {
        List<TimeslotUser> timeslotUsers = em.createQuery("SELECT tu FROM TimeslotUser tu WHERE tu.tsId.id = :tId", TimeslotUser.class)
                .setParameter("tId", timeslotId).getResultList();
        ArrayList<UserTable> users = new ArrayList<>();
        for (TimeslotUser tu : timeslotUsers) {
            users.add(tu.getUserId());
        }
        return users;
    }

    @GET
    @Path("findByToken/{fbTok}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserTable findUserByToken(@PathParam("fbTok") String fbTok) {
        return userFacade.findByToken(fbTok);
//        return findByToken(fbTok);
    }

    public UserTable findByToken(String token) {
        try {
            if (em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                    .setParameter("fbTok", token)
                    .getResultList().isEmpty()) {
                return null;
            } else {
                return em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                        .setParameter("fbTok", token).getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/name/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserTable> findUsersByName(@PathParam("name") String name) {
        return userFacade.findUsersByName(name);
//        //Format name as %first%last% (or %last%first% depending on input)
//        String addSpacesToName = name.replaceAll("\\+", "%");
//        addSpacesToName = addSpacesToName.concat("%");
//        addSpacesToName = "%".concat(addSpacesToName);
//        Query q = getEntityManager().createNamedQuery("UserTable.findByEntireName").setParameter("name", addSpacesToName);
//        q.setFirstResult(0);
//        //TODO add empty result handling
//        return q.getResultList();
    }

    /**
     * Returns list of timeslots that a user is a driver for
     */
    @GET
    @Path("driver/{fbTok}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findDriverTimeslots(@PathParam("fbTok") String fbTok) {
        UserTable driver = findByToken(fbTok);
        List<TimeslotTable> timeslots = em.createQuery("SELECT tu.tsId FROM TimeslotUser tu WHERE tu.userId.id = :tId", TimeslotTable.class)
                .setParameter("tId", driver.getId()).getResultList();
        return timeslots;
    }

    /**
     * Returns list of only active timeslots that a user is a driver for
     * @param fbTok
     * @return 
     */
    @GET
    @Path("findActiveDriverTimeslot/{fbTok}")
    @Produces({MediaType.APPLICATION_JSON})
    public TimeslotTable findActiveDriverTimeslot(@PathParam("fbTok") String fbTok) {
        List<TimeslotTable> timeslots = findDriverTimeslots(fbTok);

        Date rightNow = new Date();
        for (TimeslotTable ts : timeslots) {
            if (ts.getStartTime().before(rightNow)
                    && ts.getEndTime().after(rightNow)) {
                return ts;
            }
        }
        return null;
    }

    /**
     * Sets the driver status for a user
     *
     * @param fbTok the FB token of the user
     * @param driverStatus The driver status as an int. (0 for false, 1 for
     * true)
     */
    @PUT
    @Path("setDriverStatus/{fbTok}/{driverStatus}")
    public void setDriverStatus(@PathParam("fbTok") String fbTok, @PathParam("driverStatus") Integer driverStatus) {
        UserTable user = findByToken(fbTok);
        Boolean driver = false;
        if (driverStatus == 1) {
            driver = true;
        }
        user.setDriverStatus(driver);
        this.edit(user);
        eventFacade.createDriverStatusEvent(fbTok);
    }
}
