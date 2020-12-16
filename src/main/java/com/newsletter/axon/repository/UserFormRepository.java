package com.newsletter.axon.repository;

import com.newsletter.axon.domain.User;
import com.newsletter.axon.domain.UserForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserFormRepository extends JpaRepository<UserForm, Long> {

    Optional<UserForm> findOneByEmailIgnoreCase(String email);

    Page<UserForm> findByOrderByCreatedDateDesc(Pageable pageable);

}
