package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: ManualUpdateJob 
 * @author ws
 * @date 2015年12月17日 下午3:35:38 
 * @Copyright (c) 2015-2020 by caitu99
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ManualUpdateJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(ManualUpdateJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	
		String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_MANUAL_UPDATE");
		logger.info("[ManualUpdateJob] The jobFlag[SWITCH_JOB_MANUAL_UPDATE] of SWITCH_JOB_MANUAL_UPDATE is "+jobFlag);
		if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {
			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			String url = PropertiesUtil.getContexrtParam("service.url") + "/api/manual/auto/update/1.0";

			Map<String,String> paramMap = new HashMap<String,String>();
			//无传参

			try {
				HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			} catch (Exception e) {
				logger.error("[ManualUpdateJob] execute Error");
				throw new JobExecutionException(e);
			}
			logger.info("[ManualUpdateJob] Ending now .... ");
		}else{
			logger.warn("[ManualUpdateJob] The flag[SWITCH_JOB_MANUAL_UPDATE]  is Closed");
		}
	}
}
