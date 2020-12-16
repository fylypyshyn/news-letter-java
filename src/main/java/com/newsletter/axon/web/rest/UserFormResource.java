package com.newsletter.axon.web.rest;

import com.newsletter.axon.domain.UserForm;
import com.newsletter.axon.repository.UserFormRepository;
import com.newsletter.axon.security.AuthoritiesConstants;
import com.newsletter.axon.service.UserFormService;
import com.newsletter.axon.service.dto.UserFormDTO;
import com.newsletter.axon.web.rest.errors.BadRequestAlertException;
import com.newsletter.axon.web.rest.errors.EmailAlreadyUsedException;
import com.newsletter.axon.web.util.HeaderUtil;
import com.newsletter.axon.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing user froms.
 * <p>
 * This class accesses the {@link UserForm} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our user froms'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages user froms, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@CrossOrigin()
@RestController
@RequestMapping("/api")
public class UserFormResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("firstName", "lastName", "email", "subscribed", "langKey");

    private final Logger log = LoggerFactory.getLogger(UserFormResource.class);

    private final UserFormService userFormService;

    private final UserFormRepository userFormRepository;

    public UserFormResource(final UserFormService userFormService,
                            final UserFormRepository userFormRepository) {
        this.userFormService = userFormService;
        this.userFormRepository = userFormRepository;
    }

    /**
     * {@code POST /userForms}  : Creates a new user form.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userFormDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/userForms")
    public ResponseEntity<UserForm> createUserForm(@Valid @RequestBody final UserFormDTO userFormDTO) throws URISyntaxException {
        log.debug("REST request to save UserForm : {}", userFormDTO);

        if (userFormDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userFormRepository.findOneByEmailIgnoreCase(userFormDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            final UserForm newUserForm = userFormService.createUserForm(userFormDTO);
            return ResponseEntity.created(new URI("/api/userForms/" + newUserForm.getEmail()))
                    .headers(HeaderUtil.createAlert("userManagement.created", newUserForm.getEmail()))
                    .body(newUserForm);
        }
    }

    /**
     * {@code GET /userForms} : get all user forms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all user froms.
     */
    @GetMapping("/userForms")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<UserFormDTO>> getAllUserForms(final Pageable pageable) {
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest()
                    .build();
        }

        final Page<UserFormDTO> page = userFormService.getAllUsers(pageable);
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }
}
