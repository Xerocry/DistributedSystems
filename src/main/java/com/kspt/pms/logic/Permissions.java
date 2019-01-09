package com.kspt.pms.logic;

import com.kspt.pms.entity.Role;
import com.kspt.pms.entity.User;

/**
 * Created by xerocry on 05.01.19.
 */
public class Permissions {

    private static final Long REPORT_DEVELOPER = 1L << 0;
    private static final Long REPORT_MANAGER = 1L << 1;
    private static final Long REPORT_CREATOR = 1L << 2;
    private static final Long TICKET_DEVELOPER = 1L << 3;
    private static final Long TICKET_MANAGER = 1L << 4;
    private static final Long MILESTONE_MANAGER = 1L << 5;
    private static final Long USER_MANAGER = 1L << 6;

    private final Long permissions;

    private Permissions(Long permissions) {
        this.permissions = permissions;
    }

    public static Permissions getUserManager() {
        return new Permissions(USER_MANAGER);
    }

    public static Permissions getMilestoneManager() {
        return new Permissions(MILESTONE_MANAGER);
    }

    public static Permissions getTicketManager() {
        return new Permissions(TICKET_MANAGER);
    }

    public static Permissions getTicketDeveloper() {
        return new Permissions(TICKET_DEVELOPER);
    }

    public static Permissions getRepoortManager() {
        return new Permissions(REPORT_MANAGER);
    }

    public static Permissions getReportDeveloper() {
        return new Permissions(REPORT_DEVELOPER);
    }

    public static Permissions getReportCreator() {
        return new Permissions(REPORT_CREATOR);
    }

    public static Permissions getEmptyPermissions() {
        return new Permissions(0L);
    }

    public static Permissions getManagerPermissions() {
        Long permissions = USER_MANAGER | MILESTONE_MANAGER | TICKET_MANAGER;
        return new Permissions(permissions);
    }

    public static Permissions getTeamLeaderPermissions() {
        Long permissions = TICKET_MANAGER | TICKET_DEVELOPER | REPORT_CREATOR | REPORT_DEVELOPER;
        return new Permissions(permissions);
    }

    public static Permissions getDeveloperPermissions() {
        Long permissions = TICKET_DEVELOPER | REPORT_CREATOR | REPORT_DEVELOPER;
        return new Permissions(permissions);
    }

    public static Permissions getTesterPermissions() {
        Long permissions = REPORT_CREATOR | REPORT_MANAGER;
        return new Permissions(permissions);
    }

    public static Permissions getPermissionsByRole(Role role) {
        switch (role) {
            case MANAGER: return getManagerPermissions();
            case TEAMLEADER: return getTeamLeaderPermissions();
            case DEVELOPER: return getDeveloperPermissions();
            case TESTER: return getTesterPermissions();
            default: return getEmptyPermissions();
        }
    }

    public boolean isUserManager() {
        return (permissions & USER_MANAGER) == USER_MANAGER;
    }

    public boolean isMilestoneManager() {
        return (permissions & MILESTONE_MANAGER) == MILESTONE_MANAGER;
    }

    public boolean isTicketManager() {
        return (permissions & TICKET_MANAGER) == TICKET_MANAGER;
    }

    public boolean isTicketDeveloper() {
        return (permissions & TICKET_DEVELOPER) == TICKET_DEVELOPER;
    }

    public boolean isReportManager() {
        return (permissions & REPORT_MANAGER) == REPORT_MANAGER;
    }

    public boolean isReportDeveloper() {
        return (permissions & REPORT_DEVELOPER) == REPORT_DEVELOPER;
    }

    public boolean isReportCreator() {
        return (permissions & REPORT_CREATOR) == REPORT_CREATOR;
    }

    public boolean isTicketCommenter() {
        return isTicketDeveloper() || isTicketManager();
    }

    public boolean isReportCommenter() {
        return isReportCreator() || isReportDeveloper() || isReportManager();
    }

    @Override
    public String toString() {
        return permissions.toString();
    }
}
