/*
 * Created by Jared Deiner on 2016.03.15  * 
 * Copyright © 2016 Jared Deiner. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entity.GroupTable;
import com.mycompany.entity.GroupUser;
import com.mycompany.entity.RequestUser;
import com.mycompany.entity.TimeslotTable;
import com.mycompany.entity.TimeslotUser;
import com.mycompany.entity.UserTable;
import com.mycompany.session.GroupTableFacade;
import com.mycompany.session.GroupTimeslotFacade;
import com.mycompany.session.GroupUserFacade;
import com.mycompany.session.RequestUserFacade;
import com.mycompany.session.TimeslotUserFacade;
import com.mycompany.session.UserTableFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author archer
 */
@Named(value = "groupManager")
@SessionScoped
/**
 * This class defines the constant variable (static) to be unchanged and used
 * though out the different components of the project as the web app is running
 *
 * @author Deiner
 */
public class GroupManager implements Serializable {

    private String name;
    private String description;
    private String statusMessage;
    private String searchedGroupName;
    private UserTable selectedMember;
    private GroupTable selectedGroup = new GroupTable();
    private RequestUser request = new RequestUser();
    private List<RequestUser> requests = new ArrayList();
    private List<String> selectedMembers = new ArrayList();
    private List<GroupTable> matchedGroups = new ArrayList();

    @EJB
    private GroupTableFacade groupFacade;
    @EJB
    private GroupUserFacade groupUserFacade;
    @EJB
    private GroupTimeslotFacade groupTimeslotFacade;
    @EJB
    private UserTableFacade userFacade;
    @EJB
    private TimeslotUserFacade timeslotUserFacade;
    @EJB
    private RequestUserFacade requestUserFacade;

    public UserTable getLoggedInUser() {
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }

    public List<UserTable> getUsers() {
        List<UserTable> list = userFacade.findAll();
        list.remove(getLoggedInUser());
        return list;
    }

    public boolean userInGroup() {
        List<UserTable> list = groupUserFacade.findUsersForGroup(selectedGroup.getId());
        return list.contains(getLoggedInUser());
    }

    public boolean adminInGroup() {
        List<UserTable> list = groupUserFacade.findAdminsForGroup(selectedGroup.getId());
        return list.contains(getLoggedInUser());
    }

    public String getSearchedGroupName() {
        return searchedGroupName;
    }

    public void setSearchedGroupName(String searchGroupName) {
        this.searchedGroupName = searchGroupName;
    }

    public void setMatchedGroups(List<GroupTable> matchedGroups) {
        this.matchedGroups = matchedGroups;
    }

    public List<GroupTable> getMatchedGroups() {
        return matchedGroups;
    }

    public List<String> getSelectedMembers() {
        return selectedMembers;
    }

    public void setSelectedMembers(List<String> selectedMembers) {
        this.selectedMembers = selectedMembers;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSelectedGroup(GroupTable selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public GroupTable getSelectedGroup() {
        return selectedGroup;
    }

    public List<UserTable> getGroupUsers() {
        return groupUserFacade.findUsersForGroup(getSelectedGroup().getId());
    }

    public List<UserTable> getGroupAdmins() {
        return groupUserFacade.findAdminsForGroup(getSelectedGroup().getId());
    }

    public List<TimeslotTable> getGroupTimeslots() {
        return groupTimeslotFacade.findTimeslotsForGroup(getSelectedGroup().getId());
    }

    public UserTable getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(UserTable selectedMember) {
        this.selectedMember = selectedMember;
    }

    public void setRequestedGroup(GroupTable selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public GroupTable getRequestedGroup() {
        return selectedGroup;
    }

    public RequestUser getRequest() {
        return request;
    }

    public void setRequest(RequestUser request) {
        this.request = request;
    }

    public String promoteToAdmin() {
        if (selectedMember != null) {
            GroupUser editRow = groupUserFacade.findByGroupAndUser(selectedGroup, selectedMember);
            editRow.setAdmin(Boolean.TRUE);
            groupUserFacade.edit(editRow);
        }

        selectedMember = null;
        return "ViewGroup?faces-redirect=true";
    }

    public String lastAdminToLeave() {
        return "";
    }

    public void onGroupRowSelect() {
        //System.out.println(selectedGroup);
        ConfigurableNavigationHandler configurableNavigationHandler
                = (ConfigurableNavigationHandler) FacesContext.
                getCurrentInstance().getApplication().getNavigationHandler();

        configurableNavigationHandler.performNavigation("ViewGroup?faces-redirect=true");

        searchedGroupName = "";
        matchedGroups.clear();
    }

    public String createGroup() {
        try {
            // create the group
            GroupTable group = new GroupTable();
            group.setTitle(name);
            group.setDescription(description);
            group.setDirectoryPath("none");
            groupFacade.create(group);

            // make creator of group an admin
            GroupUser relationTable = new GroupUser();
            relationTable.setGroupId(group);
            relationTable.setUserId(getLoggedInUser());
            relationTable.setAdmin(Boolean.TRUE);

            // create the groupUser relation
            groupUserFacade.create(relationTable);

            // add all members that were checked off
            for (String user_id : selectedMembers) {
                UserTable member = userFacade.findById(Integer.parseInt(user_id));
                relationTable = new GroupUser();
                relationTable.setGroupId(group);
                relationTable.setUserId(member);
                relationTable.setAdmin(Boolean.FALSE);

                // create the groupUser relation
                groupUserFacade.create(relationTable);
            }

            name = "";
            description = "";
            selectedMembers.clear();
            return "Home?faces-redirect=true";

        } catch (EJBException e) {
            statusMessage = "Something went wrong while creating group!";
            return "";
        }
    }

    public String leaveGroup() {
        try {
            // get the selected group to leave
            UserTable leaving = getLoggedInUser();

            // delete user from groupUser table
            GroupUser deleteGroupUserRow = groupUserFacade.findByGroupAndUser(selectedGroup, leaving);
            groupUserFacade.remove(deleteGroupUserRow);

            // delete all user timeslot relations that have to do with the group
            List<TimeslotTable> timeslots = groupTimeslotFacade.findTimeslotsForGroup(selectedGroup.getId());
            for (TimeslotTable timeslot : timeslots) {
                TimeslotUser deleteTimeslotUserRow = timeslotUserFacade.findByTimeslotAndUser(timeslot, leaving);
                if (deleteTimeslotUserRow != null) {
                    timeslotUserFacade.remove(deleteTimeslotUserRow);
                }
            }

            return "Home?faces-redirect=true";
        } catch (EJBException e) {
            statusMessage = "Something went wrong leaving group";
            return "";
        }
    }

    public String addToGroup() {
        try {

            // add user from groupUser table
            for (String userId : selectedMembers) {
                GroupUser addGroupUserRow = new GroupUser();
                UserTable addedUser = userFacade.findById(Integer.parseInt(userId));

                // check if user already exists in group
                if (userInGroup()) {
                    continue;
                }

                // add to realtional table
                addGroupUserRow.setGroupId(selectedGroup);
                addGroupUserRow.setUserId(addedUser);
                groupUserFacade.create(addGroupUserRow);

                // add all user timeslot relations that have to do with the group
                List<TimeslotTable> timeslots = groupTimeslotFacade.findTimeslotsForGroup(selectedGroup.getId());
                for (TimeslotTable timeslot : timeslots) {
                    TimeslotUser addTimeslotUserRow = new TimeslotUser();
                    addTimeslotUserRow.setTsId(timeslot);
                    addTimeslotUserRow.setUserId(addedUser);
                    timeslotUserFacade.create(addTimeslotUserRow);
                }
            }

            selectedMembers.clear();
            return "ViewGroup?faces-redirect=true";
        } catch (EJBException e) {
            statusMessage = "Something went trying to add to group";
            return "";
        }
    }

    public String kickFromGroup() {
        if (selectedMember != null) {
            try {
                // delete user from groupUser table
                GroupUser deleteGroupUserRow = groupUserFacade.findByGroupAndUser(selectedGroup, selectedMember);
                groupUserFacade.remove(deleteGroupUserRow);

                // delete all user timeslot relations that have to do with the group
                List<TimeslotTable> timeslots = groupTimeslotFacade.findTimeslotsForGroup(selectedGroup.getId());
                for (TimeslotTable timeslot : timeslots) {
                    TimeslotUser deleteTimeslotUserRow = timeslotUserFacade.findByTimeslotAndUser(timeslot, selectedMember);
                    if (deleteTimeslotUserRow != null) {
                        timeslotUserFacade.remove(deleteTimeslotUserRow);
                    }
                }

                selectedMember = null;
                return "ViewGroup?faces-redirect=true";
            } catch (EJBException e) {
                statusMessage = "Something went wrong leaving group";
                return "";
            }
        }
        return "";
    }

    public String acceptRequest() {
        if (request != null) {
            try {
                selectedMembers.add(request.getUserId().getId().toString());
                requestUserFacade.remove(request);
                addToGroup();
                selectedMembers.clear();
                return "ViewGroup?faces-redirect=true";
            } catch (EJBException e) {
                statusMessage = "Something went trying to add to group";
                return "";
            }
        }
        return "";
    }

    public String denyRequest() {
        if (request != null) {
            try {
                requestUserFacade.remove(request);
                return "ViewGroup?faces-redirect=true";
            } catch (EJBException e) {
                statusMessage = "Something went trying to add to group";
                return "";
            }
        }
        return "";
    }

    public String requestGroup() {
        UserTable user = getLoggedInUser();

        if (selectedGroup != null) {
            try {
                // add to request user table
                RequestUser req = new RequestUser();

                // if already requested
                if (requestUserFacade.findAll().contains(req)) {
                    return "";
                }

                // make the request in the requestUser table
                req.setUserId(user);
                req.setGroupId(selectedGroup);
                requestUserFacade.create(req);

                return "Profile?faces-redirect=true";
            } catch (EJBException e) {
                statusMessage = "Something went wrong when requesting to join group";
                return "";
            }
        }
        return "";
    }

    public String getNameById(int user_id) {
        UserTable user = userFacade.findById(user_id);
        return user.getFirstName() + " " + user.getLastName();
    }

    public List<RequestUser> getRequests() {
        requests = requestUserFacade.findRequestsForGroup(selectedGroup.getId());

        for (RequestUser req : requests) {
            System.out.println(req.getUserId().getFirstName() + " " + req.getGroupId().getTitle());
        }

        return requests;
    }

    public void searchedGroups() {
        matchedGroups = groupFacade.searchGroupByTitle(searchedGroupName);
        List<GroupTable> userGroups = groupUserFacade.findGroupsForUser(getLoggedInUser().getId());

        for (GroupTable group : userGroups) {
            if (matchedGroups.contains(group)) {
                matchedGroups.remove(group);
            }
        }
    }
}
