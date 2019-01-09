package com.kspt.pms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

/**
 * Created by kivi on 03.12.17.
 */

public class NotFoundException extends PMSException {
    public NotFoundException(String name) {
        super("could not find object with name: " + name);
    }
}
