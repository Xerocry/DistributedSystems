package com.kspt.pms.logic;

import com.kspt.pms.entity.Milestone;
import com.kspt.pms.entity.MilestoneStatus;
import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.Ticket;
import com.kspt.pms.exception.*;

import java.util.Date;

/**
 * Created by kivi on 20.12.17.
 */
public interface MilestoneManager extends UserInterface {

    default Milestone createMilestone(Project project, Date start, Date end) throws NoRightsException {
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isMilestoneManager())
            throw new NoRightsException(getUser(), Permissions.getMilestoneManager(), project);
        if (end.compareTo(start) < 0)
            throw new IncorrectMilestoneDateException("end date before start date");
        if (end.compareTo(new Date()) < 0)
            throw new IncorrectMilestoneDateException("end date in the past");

        Milestone milestone = new Milestone();
        milestone.setProject(project);
        milestone.setStartingDate(start);
        milestone.setEndingDate(end);
        return milestone;
    }

    default void activateMilestone(Milestone milestone) throws TwoActiveMilestonesException, WrongStatusException, NoRightsException {
        Project project = milestone.getProject();
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isMilestoneManager())
            throw new NoRightsException(getUser(), Permissions.getMilestoneManager(), project);
        if (milestone.isActive())
            throw new WrongStatusException("Active", "Active");
        if (!milestone.isOpened())
            throw new WrongStatusException(milestone.getStatus().name(), MilestoneStatus.ACTIVE.name());

        for (Milestone m : project.getMilestones()) {
            if (m.isActive()) throw new TwoActiveMilestonesException(m.getId(), milestone.getId());
        }
        milestone.setActive();
        milestone.setActivatedDate(new Date());
    }

    default void closeMilestone(Milestone milestone) throws MilestoneTicketNotClosedException, WrongStatusException, NoRightsException {
        Project project = milestone.getProject();
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isMilestoneManager())
            throw new NoRightsException(getUser(), Permissions.getMilestoneManager(), project);
        if (milestone.isClosed())
            throw new WrongStatusException("Closed", "Closed");
        if (!milestone.isActive())
            throw new WrongStatusException(milestone.getStatus().name(), MilestoneStatus.CLOSED.name());
        for (Ticket t : milestone.getTickets())
            if (!t.isClosed()) throw new MilestoneTicketNotClosedException(t.getId());

        milestone.setClosed();
        milestone.setClosedDate(new Date());
    }
}
