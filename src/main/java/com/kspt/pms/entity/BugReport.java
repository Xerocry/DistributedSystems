package com.kspt.pms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by kivi on 03.12.17.
 */
@Entity
@Table(name = "BUGREPORT")
public class BugReport {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Project project;

    @ManyToOne
    private User creator;

    @ManyToOne
    private User developer;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.OPENED;

    @Column(name = "CREATION_TIME")
    @Temporal(TemporalType.DATE)
    private Date creationTime = new Date();

    @Column(name = "DESCRIPTION")
    private String description;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "BUGREPORT_COMMENTS",
            joinColumns = { @JoinColumn(name = "bugreport") },
            inverseJoinColumns = { @JoinColumn(name = "commentid") }
    )
    private Set<Comment> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getDeveloper() {
        return developer;
    }

    public void setDeveloper(User developer) {
        this.developer = developer;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public boolean isOpened()   { return status.equals(ReportStatus.OPENED); }
    public boolean isAccepted() { return status.equals(ReportStatus.ACCEPTED); }
    public boolean isFixed()    { return status.equals(ReportStatus.FIXED); }
    public boolean isClosed()   { return status.equals(ReportStatus.CLOSED); }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void setReopened() {
        status = ReportStatus.OPENED;
    }

    public void setAccepted() {
        status = ReportStatus.ACCEPTED;
    }

    public void setFixed() {
        status = ReportStatus.FIXED;
    }

    public void setClosed() {
        status = ReportStatus.CLOSED;
    }
}
