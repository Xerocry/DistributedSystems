/**
 * Created by Azat on 28.03.2017.
 */

package com.kspt.pms.exception;

import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.User;
import com.kspt.pms.logic.Permissions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NoRightsException extends PMSException {

    private static final String template = "User %s don't have permission %s for project %s";

    public NoRightsException(User user, Permissions permissions, Project project) {
        super(String.format(template, user.getLogin(), permissions.toString(), project.getName()));
    }
}
