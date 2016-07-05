package com.caitu99.job.init;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.caitu99.job.init.cron.IntegralRemindManager;
import com.caitu99.job.init.cron.MailUpdateManager;
import com.caitu99.job.init.cron.ManualQueryAutoFindManager;
import com.caitu99.job.init.cron.ManualUpdateManager;


public class Initialization implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(Initialization.class);
	
	@Autowired
	IntegralRemindManager integralRemindManager;
	@Autowired
	MailUpdateManager mailUpdateManager;
	@Autowired
	ManualUpdateManager manualUpdateManager;
	@Autowired
	ManualQueryAutoFindManager manualQueryAutoFindManager;
	
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
			if (event.getApplicationContext().getParent() == null) {
				integralRemindManager.integralRemindExec();
				mailUpdateManager.mailUpdateExec();
				manualUpdateManager.manualUpdateExec();
				manualQueryAutoFindManager.manualQueryExec();
			}
		} catch (SchedulerException e) {
			logger.error("cron job error {}",e);
			e.printStackTrace();
		}
    }

}
