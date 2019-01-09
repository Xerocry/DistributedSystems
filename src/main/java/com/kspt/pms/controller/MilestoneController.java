package com.kspt.pms.controller;

import com.kspt.pms.entity.*;
import com.kspt.pms.exception.NotFoundException;
import com.kspt.pms.logic.Manager;
import com.kspt.pms.logic.Permissions;
import com.kspt.pms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by kivi on 18.12.17.
 */
@RestController
public class MilestoneController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    MilestoneRepository milestoneRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MessageRepository messageRepository;

    @RequestMapping("/rest/milestone/{id}")
    public Milestone getMilestone(@PathVariable Long id) {
        return milestoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Milestone " + id.toString()));
    }

    @RequestMapping("/rest/milestone/{id}/permissions")
    public Permissions getPermissions(@PathVariable Long id,
                                      @RequestParam("user") String login) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Milestone " + id.toString()));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        return Permissions.getPermissionsByRole(milestone.getProject().getRoleForUser(user));
    }

    @RequestMapping("/rest/milestone/{id}/project")
    public Project getMilestoneProject(@PathVariable Long id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Milestone " + id.toString()));
        return milestone.getProject();
    }

    @RequestMapping(value = "/rest/milestone/{id}", method = RequestMethod.PUT)
    public void updateStatus(@PathVariable Long id,
                             @RequestParam("user") String login,
                             @RequestBody String status) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Milestone " + id.toString()));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        Manager manager = new Manager(user, ticketRepository, commentRepository, messageRepository);
        switch (status) {
            case "ACTIVE": manager.activateMilestone(milestone); break;
            case "CLOSED": manager.closeMilestone(milestone); break;
            default: break;
        }
        milestoneRepository.save(milestone);
    }

    @RequestMapping("/rest/milestone/{id}/tickets")
    public Collection<Ticket> getTickets(@PathVariable Long id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Milestone " + id.toString()));
        return milestone.getTickets();
    }

    @RequestMapping(value = "/rest/milestone/{id}/tickets", method = RequestMethod.POST)
    public void addTicket(@PathVariable Long id,
                                         @RequestParam("user") String login,
                                         @RequestBody Ticket ticket) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Milestone " + id.toString()));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        Manager manager = new Manager(user, ticketRepository, commentRepository, messageRepository);
        manager.createTicket(milestone, ticket.getTask());
    }
}
