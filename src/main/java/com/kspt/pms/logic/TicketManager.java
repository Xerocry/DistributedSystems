package com.kspt.pms.logic;

import com.kspt.pms.entity.*;
import com.kspt.pms.exception.MilestoneAlreadyClosedException;
import com.kspt.pms.exception.NoRightsException;
import com.kspt.pms.exception.WrongStatusException;
import com.kspt.pms.repository.CommentRepository;
import com.kspt.pms.repository.TicketRepository;

/**
 * Created by xerocry on 05.01.19.
 */
public interface TicketManager  extends TicketCommenter {
    TicketRepository getTicketRepository();

    default void checkTicketManagerPermissions(Ticket ticket) throws NoRightsException {
        User user = getUser();
        Project project = ticket.getMilestone().getProject();
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(user));
        if (!permissions.isTicketManager())
            throw new NoRightsException(user, Permissions.getTicketManager(), project);
    }

    default Ticket createTicket(Milestone milestone, String task) throws MilestoneAlreadyClosedException, NoRightsException {
        Project project = milestone.getProject();
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isTicketManager())
            throw new NoRightsException(getUser(), Permissions.getTicketManager(), project);
        if (milestone.isClosed()) throw new MilestoneAlreadyClosedException(milestone);
        Ticket ticket = new Ticket();
        ticket.setMilestone(milestone);
        ticket.setTask(task);
        ticket.setCreator(getUser());
        getTicketRepository().save(ticket);

        project.getAllDevelopers().forEach(it -> {
            UserInterface usr = new UserImpl(it, getMessageRepository());
            usr.addMessage("New ticket " + ticket.getId() + " in project " + project.getName());
        });
        return ticket;
    }

    default void addAssignee(Ticket ticket, User developer) throws NoRightsException {
        checkTicketManagerPermissions(ticket);
        Project project = ticket.getMilestone().getProject();
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(developer));
        if (!permissions.isTicketDeveloper())
            throw new NoRightsException(developer, Permissions.getTicketDeveloper(), project);
        ticket.addAssignee(developer);
        commentTicket(ticket, "Added new assignee: " + developer.getLogin());
    }

    default void reopenTicket(Ticket ticket) throws NoRightsException {
        checkTicketManagerPermissions(ticket);
        if (ticket.isNew())
            throw new WrongStatusException("Reopened", "Reopened");
        ticket.setNew();
        commentTicket(ticket, "Reopened");
    }

    default void closeTicket(Ticket ticket) throws NoRightsException {
        checkTicketManagerPermissions(ticket);
        if (ticket.isClosed())
            throw new WrongStatusException("Closed", "Closed");
        ticket.setClosed();
        commentTicket(ticket, "Closed");
    }

}
