package com.kspt.pms.exception;

/**
 * Created by xerocry on 05.01.19.
 */
public class WrongStatusException extends PMSException {
    public WrongStatusException(String prev, String next) {
        super("Cannot change status from " + prev + " to " + next);
    }
}
