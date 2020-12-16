package com.newsletter.axon.service;

import com.newsletter.axon.AxonApplication;
import com.newsletter.axon.domain.Gender;
import com.newsletter.axon.domain.UserForm;
import com.newsletter.axon.repository.UserFormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Integration tests for {@link UserService}.
 */
@SpringBootTest(classes = AxonApplication.class)
@Transactional
class UserFormServiceIT {

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String DEFAULT_LANGKEY = "dummy";

    private static final String DEFAULT_ADDRESS = "address";

    private static final Gender DEFAULT_GENDER = Gender.MALE;

    @Autowired
    private UserFormRepository userFormRepository;

    @Autowired
    private AuditingHandler auditingHandler;

    @Mock
    private DateTimeProvider dateTimeProvider;

    private UserForm userForm;

    @BeforeEach
    public void init() {
        userForm = new UserForm();
        userForm.setEmail(DEFAULT_EMAIL);
        userForm.setFirstName(DEFAULT_FIRSTNAME);
        userForm.setLastName(DEFAULT_LASTNAME);
        userForm.setLangKey(DEFAULT_LANGKEY);
        userForm.setAddress(DEFAULT_ADDRESS);
        userForm.setPhoneNumber(DEFAULT_ADDRESS);
        userForm.setGender(DEFAULT_GENDER);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }

    @Test
    @Transactional
    public void assertThatUserFormWillBeCreated() {
        final UserForm savedUserForm = userFormRepository.saveAndFlush(this.userForm);

        assertThat(savedUserForm.getEmail()).isEqualTo(userForm.getEmail());
        assertThat(savedUserForm.getFirstName()).isEqualTo(userForm.getFirstName());
        assertThat(savedUserForm.getLastName()).isEqualTo(userForm.getLastName());
        assertThat(savedUserForm.getAddress()).isEqualTo(userForm.getAddress());
        assertThat(savedUserForm.getPhoneNumber()).isEqualTo(userForm.getPhoneNumber());
        assertThat(savedUserForm.getGender()).isEqualTo(userForm.getGender());
    }

}
