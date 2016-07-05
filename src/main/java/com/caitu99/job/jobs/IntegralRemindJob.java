/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;

/** 
 * 
 * @Description: (积分到期提醒) 
 * @ClassName: IntegralRemindJob 
 * @author Hongbo Peng
 * @date 2015年12月3日 上午11:43:14 
 * @Copyright (c) 2015-2020 by caitu99 
 */
public class IntegralRemindJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(IntegralRemindJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			logger.info("to do cron job for integral remind");
			String serviceUrl = PropertiesUtil.getContexrtParam("service.url");
			String integarlRemindUrl = PropertiesUtil.getContexrtParam("integral.remind.url");
			String url = serviceUrl + integarlRemindUrl;
			HttpClientUtils.getInstances().doPost(url, "UTF-8","");
		} catch (Exception e) {
			logger.error("[IntegralThawJob] Have Exception : {}",e);
		}
	}

}
