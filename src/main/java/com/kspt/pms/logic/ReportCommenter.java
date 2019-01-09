package com.kspt.pms.logic;

import com.kspt.pms.entity.BugReport;
import com.kspt.pms.entity.Comment;
import com.kspt.pms.repository.CommentRepository;

/**
 * Created by xerocry on 05.01.19.
 */
public interface ReportCommenter extends UserInterface {
    CommentRepository getCommentRepository();

    default void commentReport(BugReport report, String description) {
        Comment comment = new Comment();
        comment.setDescription(description);
        comment.setUser(getUser());
        getCommentRepository().save(comment);
        report.addComment(comment);
    }
}
