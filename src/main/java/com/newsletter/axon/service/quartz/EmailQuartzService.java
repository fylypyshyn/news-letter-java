package com.newsletter.axon.service.quartz;

import com.google.gson.Gson;
import com.newsletter.axon.domain.UserForm;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.newsletter.axon.service.quartz.QuartzConstants.*;

@Service
@Transactional
public class EmailQuartzService {

    private final Logger log = LoggerFactory.getLogger(EmailQuartzService.class);

    private final Scheduler scheduler;

    public EmailQuartzService(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleEmail(final UserForm userForm) {
        try {
            final JobDetail jobDetail = buildJobDetail(userForm);
            final Trigger trigger = buildJobTrigger(jobDetail, userForm.getCreatedDate());
            scheduler.scheduleJob(jobDetail, trigger);

            log.info(EMAIL_SCHEDULED_SUCCESSFULLY_NAME + jobDetail.getKey().getName() + GROUP + jobDetail.getKey().getGroup());
        } catch (SchedulerException ex) {
            log.error(ERROR_SCHEDULING_EMAIL, ex);
        }
    }

    private JobDetail buildJobDetail(final UserForm userForm) {
        final JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(USER_FORM, new Gson().toJson(userForm));

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), JOB_GROUP)
                .withDescription(JOB_DESCRIPTION)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(final JobDetail jobDetail, final Instant startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), TRIGGER_GROUP)
                .withDescription(TRIGGER_DESCRIPTION)
                .startAt(Date.from(startAt))
                .withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(DAY_OF_MONTH, HOUR, MINUTE))
                .build();
    }
}
