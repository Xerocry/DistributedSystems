package com.kspt.pms.controller;

import com.kspt.pms.entity.BugReport;
import com.kspt.pms.entity.Milestone;
import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.User;
import com.kspt.pms.exception.NoRightsException;
import com.kspt.pms.exception.NotFoundException;
import com.kspt.pms.logic.Developer;
import com.kspt.pms.logic.Manager;
import com.kspt.pms.logic.Permissions;
import com.kspt.pms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by kivi on 17.12.17.
 */
@RestController
public class ProjectController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    MilestoneRepository milestoneRepository;
    @Autowired
    BugReportRepository bugReportRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MessageRepository messageRepository;

    @RequestMapping("rest/project/{name}")
    public Project getProject(@PathVariable String name) {
        return projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
    }

    @RequestMapping("rest/project/{name}/permissions")
    public Permissions getPermissions(@PathVariable String name,
                                      @RequestParam("user") String login) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        return Permissions.getPermissionsByRole(project.getRoleForUser(user));
    }

    @RequestMapping(value = "rest/project/{name}", method = RequestMethod.PUT)
    public void updateProject(@PathVariable String name,
                              @RequestParam("user") String login,
                              @RequestBody String teamLeader) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        User tl = userRepository.findByLogin(teamLeader)
                .orElseThrow(() -> new NotFoundException(teamLeader));
        Manager manager = new Manager(user, ticketRepository, commentRepository, messageRepository);
        manager.setTeamLeader(project, tl);
        projectRepository.save(project);
    }

    @RequestMapping("rest/project/{name}/reports")
    public Collection<BugReport> getReports(@PathVariable String name) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        return project.getReports();
    }

    @RequestMapping(value = "rest/project/{name}/reports", method = RequestMethod.POST)
    public void addReport(@PathVariable String name, @RequestBody BugReport report) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        Developer creator = new Developer(report.getCreator(), bugReportRepository, commentRepository, messageRepository);
        creator.createReport(project, report.getDescription());
    }

    @RequestMapping("rest/project/{name}/milestones")
    public Collection<Milestone> getMilestones(@PathVariable String name) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        return project.getMilestones();
    }

    @RequestMapping(value = "rest/project/{name}/milestones", method = RequestMethod.POST)
    public void addMilestone(@PathVariable String name,
                             @RequestParam("user") String login,
                             @RequestBody Milestone milestone) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        Manager manager = new Manager(user, ticketRepository, commentRepository, messageRepository);
        milestone = manager.createMilestone(project, milestone.getStartingDate(), milestone.getEndingDate());
        milestoneRepository.save(milestone);
    }

    @RequestMapping("rest/project/{name}/developers")
    public Collection<User> getDevelopers(@PathVariable String name) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        return project.getDevelopers();
    }

    @RequestMapping(value = "rest/project/{name}/developers", method = RequestMethod.POST)
    public void addDeveloper(@PathVariable String name,
                             @RequestParam("user") String login,
                             @RequestBody User developer) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        User dev = userRepository.findByLogin(developer.getLogin())
                .orElseThrow(() -> new NotFoundException(developer.getLogin()));
        Manager manager = new Manager(user, ticketRepository, commentRepository, messageRepository);
        manager.addDeveloper(project, dev);
        projectRepository.save(project);
    }

    @RequestMapping("rest/project/{name}/testers")
    public Collection<User> getTesters(@PathVariable String name) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        return project.getTesters();
    }

    @RequestMapping(value = "rest/project/{name}/testers", method = RequestMethod.POST)
    public void addTester(@PathVariable String name,
                          @RequestParam("user") String login,
                          @RequestBody User tester) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        User test = userRepository.findByLogin(tester.getLogin())
                .orElseThrow(() -> new NotFoundException(tester.getLogin()));
        Manager manager = new Manager(user, ticketRepository, commentRepository, messageRepository);
        manager.addTester(project, test);
        projectRepository.save(project);
    }
}
