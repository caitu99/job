/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.init.cron;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caitu99.job.jobs.ManualUpdateJob;
import com.caitu99.job.quarz.client.QuartzCronPersistenceClient;
import com.caitu99.job.quarz.util.PropertiesUtil;

/**
 * 邮箱自动更新任务初始化
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: MailUpdateManager 
 * @author ws
 * @date 2015年12月17日 下午3:30:30 
 * @Copyright (c) 2015-2020 by caitu99
 */
@Service
public class ManualUpdateManager {

	@Autowired
	private QuartzCronPersistenceClient quartzCronPersistenceClient;
	
	public void manualUpdateExec() throws SchedulerException{
		
		try {
			String cron = PropertiesUtil.getContexrtParam("CRON_JOB_MANUAL_UPDATE");
			quartzCronPersistenceClient.addJob("ManualUpdateJobName","ManualUpdateJobGroup",ManualUpdateJob.class, cron,null);
		} catch (SchedulerException e) {
			throw new SchedulerException(e);
		}
	}
}
