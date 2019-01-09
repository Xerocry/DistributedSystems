package com.kspt.pms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by kivi on 15.12.17.
 */
public class UnknownRequestParamValueException extends PMSException {

    private static final String temlate = "Param %s have unknown value %s";

    public UnknownRequestParamValueException(String param, String value) {
        super(String.format(temlate, param, value));
    }
}
