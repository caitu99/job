package com.caitu99.job.init.cron;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caitu99.job.jobs.ManualQueryAutoFindJob;
import com.caitu99.job.quarz.client.QuartzCronPersistenceClient;
import com.caitu99.job.quarz.util.PropertiesUtil;

/**
 * 手动查询自动发现任务初始化
 * @Description: (类职责详细描述,可空) 
 * @ClassName: ManualQueryAutoFindManager 
 * @author xiongbin
 * @date 2015年12月22日 上午9:38:00 
 * @Copyright (c) 2015-2020 by caitu99
 */
@Service
public class ManualQueryAutoFindManager {

	@Autowired
	private QuartzCronPersistenceClient quartzCronPersistenceClient;
	
	public void manualQueryExec() throws SchedulerException{
		
		try {
			String cron = PropertiesUtil.getContexrtParam("CRON_JOB_MANUAL_QUERY_AUTO_FIND");
			quartzCronPersistenceClient.addJob("ManualQueryAutoFindJobName","ManualQueryAutoFindJobGroup",ManualQueryAutoFindJob.class, cron,null);
		} catch (SchedulerException e) {
			throw new SchedulerException(e);
		}
	}
}
