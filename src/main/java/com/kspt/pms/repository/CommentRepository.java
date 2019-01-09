package com.kspt.pms.repository;

import com.kspt.pms.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by kivi on 03.12.17.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
    Collection<Comment> findByUserLogin(String login);
}
