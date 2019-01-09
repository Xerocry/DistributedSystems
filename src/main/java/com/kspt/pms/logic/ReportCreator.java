package com.kspt.pms.logic;

import com.kspt.pms.entity.BugReport;
import com.kspt.pms.entity.Comment;
import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.User;
import com.kspt.pms.exception.NoRightsException;
import com.kspt.pms.repository.BugReportRepository;
import com.kspt.pms.repository.CommentRepository;

/**
 * Created by xerocry on 05.01.19.
 */
public interface ReportCreator  extends ReportCommenter {
    BugReportRepository getBugReportRepository();

    default void checkReportCreatorPermissions(BugReport report) throws NoRightsException {
        User user = getUser();
        Project project = report.getProject();
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(user));
        if (!permissions.isReportCreator())
            throw new NoRightsException(user, Permissions.getReportCreator(), project);
    }

    default BugReport createReport(Project project, String description) throws NoRightsException {
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(getUser()));
        if (!permissions.isReportCreator())
            throw new NoRightsException(getUser(), Permissions.getReportCreator(), project);

        BugReport report = new BugReport();
        report.setCreator(getUser());
        report.setProject(project);
        report.setDescription(description);
        getBugReportRepository().save(report);

        project.getAllDevelopers().forEach(it -> {
            UserInterface usr = new UserImpl(it, getMessageRepository());
            usr.addMessage("New bug report " + report.getId() + " in project " + project.getName());
        });
        return report;
    }
}
