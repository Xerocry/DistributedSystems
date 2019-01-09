package com.kspt.pms.exception;

/**
 * Created by kivi on 20.12.17.
 */
public class IncorrectMilestoneDateException extends PMSException {

    private static final String template = "Can't create milestone with %s";

    public IncorrectMilestoneDateException(String message) {
        super(String.format(template, message));
    }
}
