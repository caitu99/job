/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;

/** 
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: PushMessageJob 
 * @author Hongbo Peng
 * @date 2015年12月23日 下午2:13:36 
 * @Copyright (c) 2015-2020 by caitu99 
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PushMessageJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(PushMessageJob.class);
	
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			logger.info("[PushMessageJob] Run now .... ");
			
			String messageId = (String)context.getJobDetail().getJobDataMap().get("messageId");
			logger.info("[PushMessageJob] confirm messageId is {}" ,messageId);

			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			String url = PropertiesUtil.getContexrtParam("service.url") + PropertiesUtil.getContexrtParam("unify.push.message.url");
			logger.info("url:{}",url);
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("messageId", messageId);

			HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[PushMessageJob] Ending now .... ");
		} catch (Exception e) {
			logger.error("[PushMessageJob] Have Exception :"+e.getMessage(),e);
			throw new JobExecutionException(e);
		}
	}

}
