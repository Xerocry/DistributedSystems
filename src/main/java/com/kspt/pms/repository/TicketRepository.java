package com.kspt.pms.repository;

import com.kspt.pms.entity.Ticket;
import com.kspt.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by kivi on 03.12.17.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findById(Long id);
    Collection<Ticket> findByMilestoneId(Long id);
    Collection<Ticket> findByAssigneesContaining(User user);
    Collection<Ticket> findByCreatorLogin(String login);
}
