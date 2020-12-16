package com.newsletter.axon.security.service;

import com.newsletter.axon.domain.User;
import com.newsletter.axon.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        User user = userRepository.findOneByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with login: " + login));

        return UserDetailsImpl.build(user);
    }

}
