package com.newsletter.axon.web.rest;

import com.newsletter.axon.domain.Authority;
import com.newsletter.axon.domain.User;
import com.newsletter.axon.repository.AuthorityRepository;
import com.newsletter.axon.repository.UserRepository;
import com.newsletter.axon.security.AuthoritiesConstants;
import com.newsletter.axon.security.jwt.JwtUtils;
import com.newsletter.axon.security.service.UserDetailsImpl;
import com.newsletter.axon.service.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthResource {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthResource(final AuthenticationManager authenticationManager, final UserRepository userRepository, final AuthorityRepository authorityRepository,
                        final PasswordEncoder encoder, final JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtils.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDTO(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        if (userRepository.existsByLogin(userDTO.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        final User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        final String password = encoder.encode(userDTO.getPassword());
        user.setPassword(password);

        final Set<AuthorityDTO> strRoles = userDTO.getAuthorities();
        final Set<Authority> authorities = new HashSet<>();

        if (strRoles == null) {
            final Authority authority = authorityRepository.findByName(AuthoritiesConstants.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            authorities.add(authority);
        } else {
            strRoles.forEach(role -> {
                final Authority adminRole = authorityRepository.findByName(AuthoritiesConstants.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                authorities.add(adminRole);
            });
        }

        user.setAuthorities(authorities);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
