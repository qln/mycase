package com.qln.cases.quartz.config;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qln.cases.quartz.HelloJob;

@Configuration
public class ScheduleConfiguration {

    @Bean(name = "scheduler")
    public Scheduler getScheduler() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = null;
        try {
            sched = sf.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return sched;
    }

    @Bean
    public JobDetail getHelloJob() {
        return newJob(HelloJob.class).withIdentity("job1", "group1").build();
    }

    @Bean
    public Trigger getTrigger() {
        return newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule("0/5 * * * * ?")).build();
    }
}
