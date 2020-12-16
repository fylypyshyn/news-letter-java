package com.newsletter.axon.service.quartz;

public final class QuartzConstants {

    public static final String USER_FORM = "userForm";
    public static final int DAY_OF_MONTH = 1;
    public static final int HOUR = 12;
    public static final int MINUTE = 0;
    public static final String JOB_DESCRIPTION = "Send Email Job";
    public static final String JOB_GROUP = "email-jobs";
    public static final String TRIGGER_GROUP = "email-triggers";
    public static final String TRIGGER_DESCRIPTION = "Send Email Trigger";
    public static final String EMAIL_SCHEDULED_SUCCESSFULLY_NAME = "Email Scheduled Successfully! Name: ";
    public static final String GROUP = "; Group: ";
    public static final String ERROR_SCHEDULING_EMAIL = "Error scheduling email";
    public static final String EXECUTING_JOB_WITH_KEY = "Executing Job with key {}";

    private QuartzConstants() {
    }
}
