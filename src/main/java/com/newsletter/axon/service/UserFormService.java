package com.newsletter.axon.service;

import com.newsletter.axon.domain.UserForm;
import com.newsletter.axon.repository.UserFormRepository;
import com.newsletter.axon.service.dto.UserFormDTO;
import com.newsletter.axon.service.mapper.UserFormMapper;
import com.newsletter.axon.service.quartz.EmailQuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserFormService {

    private final Logger log = LoggerFactory.getLogger(UserFormService.class);

    private final UserFormRepository userFormRepository;

    private final UserFormMapper userFormMapper;

    private final EmailQuartzService quartzService;

    public UserFormService(final UserFormRepository userFormRepository, final UserFormMapper userFormMapper,
                           final EmailQuartzService quartzService) {
        this.userFormRepository = userFormRepository;
        this.userFormMapper = userFormMapper;
        this.quartzService = quartzService;
    }


    public UserForm createUserForm(final UserFormDTO userFormDTO) {
        final UserForm userFormEntity = userFormMapper.toEntity(userFormDTO);
        userFormEntity.setSubscribed(true);

        final UserForm savedUserForm = userFormRepository.save(userFormEntity);

        quartzService.scheduleEmail(savedUserForm);

        log.debug("Created Information for User: {}", savedUserForm);
        return savedUserForm;
    }

    @Transactional(readOnly = true)
    public Page<UserFormDTO> getAllUsers(final Pageable pageable) {
        return userFormRepository.findByOrderByCreatedDateDesc(pageable)
                .map(userFormMapper::toDto);
    }
}
