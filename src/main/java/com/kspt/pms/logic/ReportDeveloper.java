package com.kspt.pms.logic;

import com.kspt.pms.entity.BugReport;
import com.kspt.pms.entity.Comment;
import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.User;
import com.kspt.pms.exception.AlreadyAcceptedException;
import com.kspt.pms.exception.NoRightsException;
import com.kspt.pms.exception.WrongStatusException;
import com.kspt.pms.repository.CommentRepository;

/**
 * Created by xerocry on 05.01.19.
 */
public interface ReportDeveloper extends ReportCommenter {

    default void checkReportDeveloperPermissions(BugReport report) throws NoRightsException {
        User user = getUser();
        Project project = report.getProject();
        Permissions permissions = Permissions.getPermissionsByRole(project.getRoleForUser(user));
        if (!permissions.isReportDeveloper())
            throw new NoRightsException(user, Permissions.getReportDeveloper(), project);
    }

    default void acceptReport(BugReport report) throws AlreadyAcceptedException, NoRightsException {
        checkReportDeveloperPermissions(report);
        if (report.getDeveloper() != null && !report.getDeveloper().equals(getUser()))
            throw new AlreadyAcceptedException(report, getUser());
        if (report.isAccepted())
            throw new WrongStatusException("Accepted", "Accepted");

        report.setAccepted();
        report.setDeveloper(getUser());
        commentReport(report, "Accepted");
    }

    default void fixReport(BugReport report) throws NoRightsException {
        checkReportDeveloperPermissions(report);
        if (!getUser().equals(report.getDeveloper()));
        if (report.isFixed())
            throw new WrongStatusException("Fixed", "Fixed");
        report.setFixed();
        commentReport(report, "Fixed");
    }

}
