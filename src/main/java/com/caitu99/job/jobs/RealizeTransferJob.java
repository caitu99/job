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
 * @Description: (定时返现) 
 * @ClassName: RealizeTransferJob 
 * @author Hongbo Peng
 * @date 2016年2月25日 上午10:02:44 
 * @Copyright (c) 2015-2020 by caitu99
 */
public class RealizeTransferJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(RealizeTransferJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.info("[RealizeTransferJob] Run now .... ");
			
			String realizeRecordId = (String)context.getJobDetail().getJobDataMap().get("realizeRecordId");
			logger.info("[RealizeTransferJob] confirm realizeRecordId is {}" ,realizeRecordId);

			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			String url = PropertiesUtil.getContexrtParam("service.url") + PropertiesUtil.getContexrtParam("realize.transfer.url");

			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("realizeRecordId", realizeRecordId);

			HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[RealizeTransferJob] Ending now .... ");
		} catch (Exception e) {
			logger.error("[RealizeTransferJob] Have Exception :"+e.getMessage(),e);
			throw new JobExecutionException(e);
		}
	}

}
