package com.kspt.pms.logic;

import com.kspt.pms.entity.Comment;
import com.kspt.pms.entity.Ticket;
import com.kspt.pms.repository.CommentRepository;

/**
 * Created by xerocry on 05.01.19.
 */
public interface TicketCommenter extends UserInterface {
    CommentRepository getCommentRepository();

    default void commentTicket(Ticket ticket, String description) {
        Comment comment = new Comment();
        comment.setDescription(description);
        comment.setUser(getUser());
        getCommentRepository().save(comment);
        ticket.addComment(comment);
    }
}
