/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupTimeslot;
import com.mycompany.entity.TimeslotDateResponse;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.TimeslotUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTableFacade;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.TimeslotUserFacade;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
@Path("/timeslot")
public class TimeslotTableFacadeREST extends AbstractFacade<TimeslotTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();
    @EJB
    private GroupTimeslotFacade gtFacade;
    @EJB
    private GroupTableFacade groupFacade;
    @EJB
    private UserTableFacadeREST userFacade;
    @EJB
    private TimeslotUserFacade tuFacade;

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
    @POST
    @Path("createTimeslotForGroup/{groupId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public TimeslotTable createTimeslotForGroup(TimeslotTable entity, @PathParam("groupId") Integer groupId) {
        GroupTable group = groupFacade.find(groupId);
        String passcode = timeslotPasscodeGenerator(6);
        //While loop to ensure unique TAD passcode
        while (!getEntityManager().createNamedQuery("TimeslotTable.findByPasscode").setParameter("passcode", passcode).getResultList().isEmpty()) {
            passcode = timeslotPasscodeGenerator(6);
        }
        entity.setPasscode(passcode);
        super.create(entity);
        getEntityManager().flush();
        //GroupTimeslot relation entry
        GroupTimeslot gt = new GroupTimeslot();
        gt.setGroupId(group);
        gt.setTsId(entity);
        gtFacade.create(gt);
        //UserTimeslot relational entries
        List<UserTable> usersInGroup = userFacade.findUsersInGroup(groupId);
        for (UserTable user: usersInGroup) {
            TimeslotUser tu = new TimeslotUser();
            tu.setUserId(user);
            tu.setTsId(entity);
            tu.setDriver(false);
            tuFacade.create(tu);
        }
        return entity;
    }
    
    @PUT
    @Path("assignDriverForTimeslot/{userId}/{tsId}")
    public void assignDriverForTimeslot(@PathParam("userId") Integer userId, @PathParam("tsId") Integer tsId) {
        List<TimeslotUser> queryResult = getEntityManager().createNamedQuery("TimeslotUser.findByUserIdAndTimeSlotId").setParameter("userId", userId).
                setParameter("tsId", tsId).getResultList();
        if (!queryResult.isEmpty()) {
            TimeslotUser tu = queryResult.get(0);
            tu.setDriver(true);
        }
    }
    
    @PUT
    @Path("removeDriverForTimeslot/{userId}/{tsId}")
    public void removeDriverForTimeslot(@PathParam("userId") Integer userId, @PathParam("tsId") Integer tsId) {
        List<TimeslotUser> queryResult = getEntityManager().createNamedQuery("TimeslotUser.findByUserIdAndTimeSlotId").setParameter("userId", userId).
                setParameter("tsId", tsId).getResultList();
        if (!queryResult.isEmpty()) {
            TimeslotUser tu = queryResult.get(0);
            tu.setDriver(false);
        }
    }

    @GET
    @Path("timeslotsForGroup/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForGroup(@PathParam("groupId") String groupId) {
        List<GroupTimeslot> groupTimeslots = em.createQuery("SELECT gt FROM GroupTimeslot gt WHERE gt.groupId.id = :gId", GroupTimeslot.class)
                .setParameter("gId", groupId).getResultList();
        ArrayList<TimeslotTable> timeslots = new ArrayList<>();
        for (GroupTimeslot gt : groupTimeslots) {
            timeslots.add(gt.getTsId());
        }
        return timeslots;
    }

    /**
     * TALK W/ PAT
     *
     * @param groupId
     * @return
     */
    @GET
    @Path("timeslotsForGroupSorted/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotDateResponse> findTimeslotsForGroupSorted(@PathParam("groupId") Integer groupId) {
        Query q = getEntityManager().createNamedQuery("GroupTimeslot.findTimeslotsByGroupId").setParameter("id", groupId);
        q.setFirstResult(0);
        List<TimeslotTable> list = q.getResultList();
        if (list.isEmpty()) {
            return null;
        }

        Collections.sort(list, new Comparator<TimeslotTable>() {
            @Override
            public int compare(TimeslotTable o1, TimeslotTable o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });

        ArrayList<TimeslotDateResponse> responseList = new ArrayList();
        TimeslotTable first = list.get(0);
        Date currentDate = new Date(first.getStartTime().getYear(), first.getStartTime().getMonth(), first.getStartTime().getDate());
        TimeslotDateResponse currentResponse = new TimeslotDateResponse();
        currentResponse.setDate(currentDate);
        currentResponse.setTimeslots(new ArrayList<TimeslotTable>());
        currentResponse.getTimeslots().add(first);

        for (TimeslotTable timeslot : list) {
            if (timeslot == first) {

            } else if (timeslot.getStartTime().getYear() == currentDate.getYear()
                    && timeslot.getStartTime().getMonth() == currentDate.getMonth()
                    && timeslot.getStartTime().getDate() == currentDate.getDate()) {
                currentResponse.getTimeslots().add(timeslot);
            } else {
                responseList.add(currentResponse);
                currentDate = new Date(timeslot.getStartTime().getYear(), timeslot.getStartTime().getMonth(), timeslot.getStartTime().getDate());
                currentResponse = new TimeslotDateResponse();
                currentResponse.setDate(currentDate);
                currentResponse.setTimeslots(new ArrayList<TimeslotTable>());
                currentResponse.getTimeslots().add(timeslot);
            }
        }
        responseList.add(currentResponse);

        return responseList;
    }

    @GET
    @Path("timeslotsForUser/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForUserEdit(@PathParam("userId") Integer userId) {
        List<TimeslotUser> timeslotUsers = em.createQuery("SELECT ut FROM TimeslotUser ut WHERE ut.userId = :uId", TimeslotUser.class)
                .setParameter("uId", userId).getResultList();
        ArrayList<TimeslotTable> timeslots = new ArrayList<>();
        for (TimeslotUser ut : timeslotUsers) {
            timeslots.add(ut.getTsId());
        }
        return timeslots;
    }

    @GET
    @Path("timeslotsForToken/{token}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findTimeslotsForToken(@PathParam("token") String token) {
        UserTable user = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                .setParameter("fbTok", token).getResultList().get(0);
        List<TimeslotUser> timeslotUsers = em.createQuery("SELECT ut FROM TimeslotUser ut WHERE ut.userId = :uId", TimeslotUser.class)
                .setParameter("uId", user.getId()).getResultList();
        ArrayList<TimeslotTable> timeslots = new ArrayList<>();
        for (TimeslotUser ut : timeslotUsers) {
            timeslots.add(ut.getTsId());
        }
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

    @GET
    @Path("driver/{fbTok}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TimeslotTable> findDriverTimeslots(@PathParam("fbTok") String fbTok) {
        UserTable driver = em.createQuery("SELECT u FROM UserTable u WHERE u.fbTok = :fbTok", UserTable.class)
                .setParameter("fbTok", fbTok).getResultList().get(0);
        List<TimeslotUser> timeslotUsers = em.createQuery("SELECT tu FROM TimeslotUser tu WHERE tu.userId.id = :tId", TimeslotUser.class)
                .setParameter("tId", driver.getId()).getResultList();
        ArrayList<TimeslotTable> timeslots = new ArrayList<>();
        for (TimeslotUser tu : timeslotUsers) {
            timeslots.add(tu.getTsId());
        }
        return timeslots;
    }

    /**
     * Simple method to generate a random length long character string
     */
    public String timeslotPasscodeGenerator(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
