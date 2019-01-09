package com.kspt.pms.logic;

import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.Role;
import com.kspt.pms.entity.User;
import com.kspt.pms.exception.MultipleRoleException;
import com.kspt.pms.exception.NoRightsException;

/**
 * Created by kivi on 20.12.17.
 */
public interface UserManager extends UserInterface {

    default void setTeamLeader(Project project, User teamLeader) throws MultipleRoleException, NoRightsException {
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isUserManager())
            throw new NoRightsException(getUser(), Permissions.getUserManager(), project);

        Role role = project.getRoleForUser(teamLeader);
        User oldTL = project.getTeamLeader();
        if (role.equals(Role.NONE)) {
            project.setTeamLeader(teamLeader);
            UserImpl tlUser = new UserImpl(teamLeader, getMessageRepository());
            tlUser.addMessage("Assigned as team leader to project " + project.getName());
            if (oldTL != null) {
                project.addDeveloper(oldTL);
                UserImpl oldTLUser = new UserImpl(teamLeader, getMessageRepository());
                oldTLUser.addMessage("Changed from team leader to developer in project " + project.getName());
            }
        } else if (role.equals(Role.DEVELOPER)) {
            project.addDeveloper(project.getTeamLeader());
            project.setTeamLeader(teamLeader);
            project.removeDeveloper(teamLeader);
            UserImpl tlUser = new UserImpl(teamLeader, getMessageRepository());
            tlUser.addMessage("Changed from developer to team leader in project " + project.getName());
            if (oldTL != null) {
                project.addDeveloper(oldTL);
                UserImpl oldTLUser = new UserImpl(teamLeader, getMessageRepository());
                oldTLUser.addMessage("Changed from team leader to developer in project " + project.getName());
            }
        } else {
            throw new MultipleRoleException(teamLeader.getLogin(), role.toString(), project.getName());
        }
    }

    default void addDeveloper(Project project, User developer) throws MultipleRoleException, NoRightsException {
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isUserManager())
            throw new NoRightsException(getUser(), Permissions.getUserManager(), project);

        Role role = project.getRoleForUser(developer);
        if (role.equals(Role.NONE)) {
            project.addDeveloper(developer);
            UserImpl devUser = new UserImpl(developer, getMessageRepository());
            devUser.addMessage("Assigned as developer to project " + project.getName());
        } else {
            throw new MultipleRoleException(developer.getLogin(), role.toString(), project.getName());
        }
    }

    default void addTester(Project project, User tester) throws MultipleRoleException, NoRightsException {
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isUserManager())
            throw new NoRightsException(getUser(), Permissions.getUserManager(), project);

        Role role = project.getRoleForUser(tester);
        if (role.equals(Role.NONE)) {
            project.addTester(tester);
            UserImpl testerUser = new UserImpl(tester, getMessageRepository());
            testerUser.addMessage("Assigned as tester to project " + project.getName());
        } else {
            throw new MultipleRoleException(tester.getLogin(), role.toString(), project.getName());
        }
    }

}
