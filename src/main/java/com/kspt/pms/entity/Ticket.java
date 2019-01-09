package com.kspt.pms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kivi on 03.12.17.
 */
@Entity
@Table(name = "TICKET")
public class Ticket {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Milestone milestone;

    @ManyToOne
    private User creator;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.NEW;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(
            name = "TICKET_ASSIGNEES",
            joinColumns = { @JoinColumn(name = "ticket") },
            inverseJoinColumns = { @JoinColumn(name = "user") }
    )
    private Set<User> assignees = new HashSet<>();

    @Column(name = "CREATION_TIME")
    @Temporal(TemporalType.DATE)
    private Date creationTime = new Date();

    @Column(name = "TASK")
    private String task;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TICKET_COMMENTS",
            joinColumns = { @JoinColumn(name = "ticket") },
            inverseJoinColumns = { @JoinColumn(name = "commentid") }
    )
    private Set<Comment> comments;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Set<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<User> assignees) {
        this.assignees = assignees;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public boolean isNew()          { return status.equals(TicketStatus.NEW); }
    public boolean isAccepted()     { return status.equals(TicketStatus.ACCEPTED); }
    public boolean isInProgress()   { return status.equals(TicketStatus.IN_PROGRESS); }
    public boolean isFinished()     { return status.equals(TicketStatus.FINISHED); }
    public boolean isClosed()       { return status.equals(TicketStatus.CLOSED); }

    public void addAssignee(User user) {
        assignees.add(user);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void setNew() {
        status = TicketStatus.NEW;
    }

    public void setAccepted() {
        status = TicketStatus.ACCEPTED;
    }

    public void setInProgress() {
        status = TicketStatus.IN_PROGRESS;
    }

    public void setFinished() {
        status = TicketStatus.FINISHED;
    }

    public void setClosed() {
        status = TicketStatus.CLOSED;
    }
}
