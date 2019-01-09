package com.kspt.pms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kspt.pms.entity.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kivi on 26.11.17.
 */
@Entity
@Table(name = "MESSAGE")
public class Message {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @JsonIgnore
    @ManyToOne
    private User owner;

    @Column(name = "CONTENT")
    private String content;

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
