package com.kspt.pms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kspt.pms.exception.MilestoneTicketNotClosedException;
import com.kspt.pms.exception.TwoActiveMilestonesException;
import com.kspt.pms.exception.WrongStatusException;
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
@Table(name = "MILESTONE")
public class Milestone {

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

    public MilestoneStatus getStatus() {
        return status;
    }

    public void setStatus(MilestoneStatus status) {
        this.status = status;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(Date activatedDate) {
        this.activatedDate = activatedDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Project project;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MilestoneStatus status = MilestoneStatus.OPENED;

    @Temporal(TemporalType.DATE)
    @Column(name = "STARTING_DATE", nullable = false)
    private Date startingDate;

    @Column(name = "ACTIVATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date activatedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "ENDING_DATE", nullable = false)
    private Date endingDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "CLOSED_DATE")
    private Date closedDate;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "milestone")
    @Fetch(value = FetchMode.SELECT)
    private Set<Ticket> tickets = new HashSet<>();

    @Override
    public int hashCode() {return project.hashCode() + startingDate.hashCode() + endingDate.hashCode();}
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Milestone other = (Milestone) obj;
        return project.equals(other.getProject()) &&
                startingDate.equals(other.getStartingDate()) &&
                endingDate.equals(other.getEndingDate());
    }

    public boolean isOpened() { return status.equals(MilestoneStatus.OPENED); }
    public boolean isActive() { return status.equals(MilestoneStatus.ACTIVE); }
    public boolean isClosed() { return status.equals(MilestoneStatus.CLOSED); }

    public void setActive() throws TwoActiveMilestonesException, WrongStatusException {
        status = MilestoneStatus.ACTIVE;
    }

    public void setClosed() throws MilestoneTicketNotClosedException, WrongStatusException {
        status = MilestoneStatus.CLOSED;
    }
}
