package com.newsletter.axon.repository;

import com.newsletter.axon.domain.Image;
import com.newsletter.axon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
