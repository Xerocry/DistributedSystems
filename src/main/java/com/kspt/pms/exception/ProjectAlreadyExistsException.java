package com.kspt.pms.exception;

/**
 * Created by kivi on 20.12.17.
 */
public class ProjectAlreadyExistsException extends PMSException {

    private static final String template = "Project with name %s already exists in system";

    public ProjectAlreadyExistsException(String name) {
        super(String.format(template, name));
    }
}
