package com.kspt.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kspt.pms.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by kivi on 26.11.17.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Collection<Message> findByOwnerLogin(String login);
}
