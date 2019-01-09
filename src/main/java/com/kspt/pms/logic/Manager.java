package com.kspt.pms.logic;

import com.kspt.pms.entity.Milestone;
import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.Role;
import com.kspt.pms.entity.User;
import com.kspt.pms.exception.*;
import com.kspt.pms.repository.CommentRepository;
import com.kspt.pms.repository.MessageRepository;
import com.kspt.pms.repository.MilestoneRepository;
import com.kspt.pms.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/**
 * Created by xerocry on 05.01.19.
 */
public class Manager implements MilestoneManager, UserManager, TicketManager {

    private User user;
    TicketRepository ticketRepository;
    CommentRepository commentRepository;
    MessageRepository messageRepository;

    public Manager(User user, TicketRepository ticketRepository,
                   CommentRepository commentRepository, MessageRepository messageRepository) {
        this.user = user;
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
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

    @Override
    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    @Override
    public CommentRepository getCommentRepository() {
        return commentRepository;
    }
}
