/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;

/** 
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: IntegralRechargeQueryJob 
 * @author Hongbo Peng
 * @date 2015年12月11日 上午9:33:39 
 * @Copyright (c) 2015-2020 by caitu99 
 */
public class IntegralRechargeQueryJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(IntegralRechargeQueryJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			logger.info("[IntegralRechargeQueryJob] Run now .... ");
			
			String id = (String)context.getJobDetail().getJobDataMap().get("id");
			logger.info("[IntegralRechargeQueryJob] confirm id is {}" ,id);

			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			String url = PropertiesUtil.getContexrtParam("service.url") + PropertiesUtil.getContexrtParam("integral.recharge.query.url");

			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("id", id);

			HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[IntegralRechargeQueryJob] Ending now .... ");
		} catch (Exception e) {
			logger.error("[IntegralRechargeQueryJob] Have Exception :"+e.getMessage(),e);
			throw new JobExecutionException(e);
		}
	}

}
