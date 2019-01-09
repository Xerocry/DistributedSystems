package com.kspt.pms.exception;

/**
 * Created by xerocry on 05.01.19.
 */
public class MultipleRoleException extends PMSException {

    private static final String template = "User %s already have a role %s in project %s";

    public MultipleRoleException(String login, String role, String project) {
        super(String.format(template, login, role, project));
    }
}
