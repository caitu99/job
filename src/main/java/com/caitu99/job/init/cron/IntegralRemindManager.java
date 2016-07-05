/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.init.cron;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caitu99.job.jobs.IntegralRemindJob;
import com.caitu99.job.quarz.client.QuartzCronPersistenceClient;
import com.caitu99.job.quarz.util.PropertiesUtil;

/** 
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: IntegralRemindJob 
 * @author Hongbo Peng
 * @date 2015年12月3日 上午11:37:42 
 * @Copyright (c) 2015-2020 by caitu99 
 */
@Service
public class IntegralRemindManager {

	@Autowired
	private QuartzCronPersistenceClient quartzCronPersistenceClient;
	
	public void integralRemindExec() throws SchedulerException{
		
		try {
			String cron = PropertiesUtil.getContexrtParam("CRON_JOB_INTEGRAL_REMIND");
			quartzCronPersistenceClient.addJob("IntegralRemindJobName","IntegralRemindJobGroup",IntegralRemindJob.class, cron,null);
		} catch (SchedulerException e) {
			throw new SchedulerException(e);
		}
	}
}
