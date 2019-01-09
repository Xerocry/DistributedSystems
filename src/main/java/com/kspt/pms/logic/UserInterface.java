package com.kspt.pms.logic;

import com.kspt.pms.entity.Message;
import com.kspt.pms.entity.User;
import com.kspt.pms.repository.CommentRepository;
import com.kspt.pms.repository.MessageRepository;
import com.kspt.pms.repository.TicketRepository;

/**
 * Created by xerocry on 05.01.19.
 */
public interface UserInterface {
    User getUser();
    MessageRepository getMessageRepository();

    default void addMessage(String message) {
        Message m = new Message();
        m.setOwner(getUser());
        m.setContent(message);
        getMessageRepository().save(m);
    }
}
