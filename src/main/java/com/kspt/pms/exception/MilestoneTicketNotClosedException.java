package com.kspt.pms.exception;

/**
 * Created by xerocry on 05.01.19.
 */
public class MilestoneTicketNotClosedException extends PMSException {

    private static final String template = "Ticket %d is not closed";

    public MilestoneTicketNotClosedException(Long id) {
        super(String.format(template, id));
    }
}
