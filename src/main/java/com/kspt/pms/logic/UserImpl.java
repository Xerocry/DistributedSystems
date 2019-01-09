package com.kspt.pms.logic;

import com.kspt.pms.entity.User;
import com.kspt.pms.repository.MessageRepository;

/**
 * Created by kivi on 20.12.17.
 */
public class UserImpl implements UserInterface {

    private User user;
    private MessageRepository messageRepository;

    public UserImpl(User user, MessageRepository messageRepository) {
        this.user = user;
        this.messageRepository = messageRepository;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public MessageRepository getMessageRepository() {
        return messageRepository;
    }
}
