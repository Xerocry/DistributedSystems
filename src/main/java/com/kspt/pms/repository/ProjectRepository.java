package com.kspt.pms.repository;

import com.kspt.pms.entity.Project;
import com.kspt.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by kivi on 03.12.17.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
    Collection<Project> findByManagerLogin(String login);
    Collection<Project> findByTeamLeaderLogin(String login);
    Collection<Project> findByDevelopersContaining(User user);
    Collection<Project> findByTestersContaining(User user);
}