package com.kspt.pms.exception;

/**
 * Created by xerocry on 05.01.19.
 */
public class TwoActiveMilestonesException extends PMSException {

    private static final String template = "Attempting to activate milestone %d, when milestone %d is already active";

    public TwoActiveMilestonesException(Long active, Long attempting) {
        super(String.format(template, attempting, active));
    }
}
