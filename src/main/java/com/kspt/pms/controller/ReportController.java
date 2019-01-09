package com.kspt.pms.controller;

import com.kspt.pms.entity.BugReport;
import com.kspt.pms.entity.Comment;
import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.User;
import com.kspt.pms.exception.NotFoundException;
import com.kspt.pms.logic.Developer;
import com.kspt.pms.logic.Permissions;
import com.kspt.pms.logic.Tester;
import com.kspt.pms.repository.BugReportRepository;
import com.kspt.pms.repository.CommentRepository;
import com.kspt.pms.repository.MessageRepository;
import com.kspt.pms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by kivi on 18.12.17.
 */
@RestController
public class ReportController {
    @Autowired
    BugReportRepository bugReportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MessageRepository messageRepository;

    @RequestMapping("/rest/report/{id}")
    public BugReport getReport(@PathVariable Long id) {
        return bugReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id.toString()));
    }

    @RequestMapping("/rest/report/{id}/permissions")
    public Permissions getPermissions(@PathVariable Long id,
                                      @RequestParam("user") String login) {
        BugReport report = bugReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id.toString()));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        return Permissions.getPermissionsByRole(report.getProject().getRoleForUser(user));
    }

    @RequestMapping("/rest/report/{id}/project")
    public Project getProject(@PathVariable Long id) {
        BugReport report = bugReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id.toString()));
        return report.getProject();
    }

    @RequestMapping(value = "/rest/report/{id}", method = RequestMethod.PUT)
    public void updateStatus(@PathVariable Long id,
                             @RequestParam("user") String login,
                             @RequestBody String status) {
        BugReport report = bugReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id.toString()));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        Tester tester = new Tester(user, bugReportRepository, commentRepository, messageRepository);
        Developer developer = new Developer(user, bugReportRepository, commentRepository, messageRepository);
        switch (status) {
            case "OPENED":
                tester.reopenReport(report);
                break;
            case "ACCEPTED":
                developer.acceptReport(report);
                break;
            case "FIXED":
                developer.fixReport(report);
                break;
            case "CLOSED":
                tester.closeReport(report);
                break;
            default: break;
        }
        bugReportRepository.save(report);
    }

    @RequestMapping("/rest/report/{id}/comments")
    public Collection<Comment> getComments(@PathVariable Long id) {
        BugReport report = bugReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id.toString()));
        return report.getComments();
    }

    @RequestMapping(value = "/rest/report/{id}/comments", method = RequestMethod.POST)
    public void getComments(@PathVariable Long id,
                            @RequestParam("user") String login,
                            @RequestBody Comment comment) {
        BugReport report = bugReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id.toString()));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(login));
        Developer developer = new Developer(user, bugReportRepository, commentRepository, messageRepository);
        developer.commentReport(report, comment.getDescription());
        bugReportRepository.save(report);
    }
}
