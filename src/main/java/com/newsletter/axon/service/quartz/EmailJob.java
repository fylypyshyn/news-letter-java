package com.newsletter.axon.service.quartz;

import com.google.gson.Gson;
import com.newsletter.axon.domain.UserForm;
import com.newsletter.axon.service.MailService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import static com.newsletter.axon.service.quartz.QuartzConstants.EXECUTING_JOB_WITH_KEY;

@Service
public class EmailJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(EmailJob.class);

    private final MailService mailService;

    public EmailJob(final MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    protected void executeInternal(final JobExecutionContext jobExecutionContext) {
        log.info(EXECUTING_JOB_WITH_KEY, jobExecutionContext.getJobDetail().getKey());

        final JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        final String userFormJson = jobDataMap.getString(QuartzConstants.USER_FORM);
        final UserForm userForm = new Gson().fromJson(userFormJson, UserForm.class);
        mailService.sendNewsLetterEmail(userForm);
    }

}
