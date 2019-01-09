package com.kspt.pms.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kivi on 03.12.17.
 */
@Entity
@Table(name = "COMMENT")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @ManyToOne
    private User user;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
