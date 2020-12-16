package com.newsletter.axon.service;

import com.newsletter.axon.domain.Authority;
import com.newsletter.axon.domain.User;
import com.newsletter.axon.repository.AuthorityRepository;
import com.newsletter.axon.repository.UserRepository;
import com.newsletter.axon.service.dto.UserDTO;
import com.newsletter.axon.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final UserMapper userMapper;

    public UserService(final UserRepository userRepository,
                       final AuthorityRepository authorityRepository,
                       final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userMapper = userMapper;
    }


    public User createUser(final UserDTO userDTO) {
        final User user = userMapper.toEntity(userDTO);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(final Pageable pageable) {
        return userRepository.findAllBy(pageable)
                .map(userMapper::toDto);
    }

    private Set<Authority> getAuthorities(final UserDTO userDTO) {
        return userDTO.getAuthorities().stream()
                .map(authorityDTO -> authorityRepository.findById(authorityDTO.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}
