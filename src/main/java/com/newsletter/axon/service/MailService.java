package com.newsletter.axon.service;

import com.newsletter.axon.domain.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private static final String TITLE_KEY = "email.newsletter.title";
    private static final String TEMPLATE_NAME = "mail/newsLetterEmail";
    private static final String URL = "http://127.0.0.1:8080";
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER_FORM = "user";

    private static final String BASE_URL = "baseUrl";

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final MailProperties mailProperties;

    public MailService(final JavaMailSender javaMailSender, final MessageSource messageSource,
                       final SpringTemplateEngine templateEngine, final MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.mailProperties = mailProperties;
    }

    @Async
    public void sendEmail(final String to, final String subject,
                          final String content, final boolean isMultipart,
                          final boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(mailProperties.getUsername());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(final UserForm userForm, final String templateName,
                                      final String titleKey) {
        if (userForm.getEmail() == null) {
            log.debug("Email doesn't exist for userForm '{}'", userForm.getId());
            return;
        }
        final Locale locale = Locale.forLanguageTag(userForm.getLangKey());
        final Context context = new Context(locale);
        context.setVariable(USER_FORM, userForm);
        context.setVariable(BASE_URL, URL);
        final String content = templateEngine.process(templateName, context);
        final String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(userForm.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendNewsLetterEmail(final UserForm userForm) {
        log.debug("Sending newsLetter email to '{}'", userForm.getEmail());
        sendEmailFromTemplate(userForm, TEMPLATE_NAME, TITLE_KEY);
    }
}
