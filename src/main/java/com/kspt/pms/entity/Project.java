package com.kspt.pms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kivi on 03.12.17.
 */
@Entity
@Transactional
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    private User manager;

    @ManyToOne
    private User teamLeader;

    //@ManyToMany(mappedBy = "developedProjects", fetch = FetchType.LAZY)
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PROJECT_DEVELOPERS",
            joinColumns = { @JoinColumn(name = "entity") },
            inverseJoinColumns = { @JoinColumn(name = "user") }
    )
    @Fetch(value = FetchMode.SELECT)
    private Set<User> developers = new HashSet<>();

    @JsonIgnore
    //@ManyToMany(mappedBy = "testedProjects", fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PROJECT_TESTERS",
            joinColumns = { @JoinColumn(name = "entity") },
            inverseJoinColumns = { @JoinColumn(name = "user") }
    )
    @Fetch(value = FetchMode.SELECT)
    private Set<User> testers = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    @Fetch(value = FetchMode.SELECT)
    private Set<Milestone> milestones = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    @Fetch(value = FetchMode.SELECT)
    private Set<BugReport> reports = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Set<User> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<User> developers) {
        this.developers = developers;
    }

    public Set<User> getTesters() {
        return testers;
    }

    public void setTesters(Set<User> testers) {
        this.testers = testers;
    }

    public Set<BugReport> getReports() {
        return reports;
    }

    public void setReports(Set<BugReport> reports) {
        this.reports = reports;
    }

    public Role getRoleForUser(User user) {
        if (user.equals(manager)) return Role.MANAGER;
        if (user.equals(teamLeader)) return Role.TEAMLEADER;
        if (developers.contains(user)) return Role.DEVELOPER;
        if (testers.contains(user)) return Role.TESTER;
        return Role.NONE;
    }

    public Set<Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(Set<Milestone> milestones) {
        this.milestones = milestones;
    }

    public void addDeveloper(User developer) {
        developers.add(developer);
    }

    public void removeDeveloper(User developer) {
        developers.remove(developer);
    }

    public void addTester(User tester) {
        testers.add(tester);
    }

    public Set<User> getAllDevelopers() {
        Set<User> dev = new HashSet<>();
        dev.addAll(developers);
        dev.add(teamLeader);
        return dev;
    }
}
