package com.kspt.pms.logic;

import com.kspt.pms.entity.BugReport;
import com.kspt.pms.entity.User;
import com.kspt.pms.repository.BugReportRepository;
import com.kspt.pms.repository.CommentRepository;
import com.kspt.pms.repository.MessageRepository;
import com.kspt.pms.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xerocry on 05.01.19.
 */
public class TeamLeader implements ReportCreator, ReportDeveloper, TicketManager, TicketDeveloper {

    private User user;
    TicketRepository ticketRepository;
    BugReportRepository bugReportRepository;
    CommentRepository commentRepository;
    MessageRepository messageRepository;

    public TeamLeader(User user,
                      TicketRepository ticketRepository,
                      BugReportRepository bugReportRepository,
                      CommentRepository commentRepository,
                      MessageRepository messageRepository) {
        this.user = user;
        this.ticketRepository = ticketRepository;
        this.bugReportRepository = bugReportRepository;
        this.commentRepository = commentRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public CommentRepository getCommentRepository() {
        return commentRepository;
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
    public BugReportRepository getBugReportRepository() {
        return bugReportRepository;
    }
}
