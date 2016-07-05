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
 * 邮箱自动更新任务
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: MailUpdateJob 
 * @author ws
 * @date 2015年12月17日 下午3:04:09 
 * @Copyright (c) 2015-2020 by caitu99
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MailUpdateJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(MailUpdateJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	
		String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_MAIL_UPDATE");
		logger.info("[MailUpdateJob] The jobFlag[SWITCH_JOB_MAIL_UPDATE] of SWITCH_JOB_MAIL_UPDATE is "+jobFlag);
		if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {

			logger.info("[MailUpdateJob] Run now .... ");
			String userId = (String) context.getJobDetail().getJobDataMap().get("userId");
			String cardTypeId = (String) context.getJobDetail().getJobDataMap().get("cardTypeId");
			logger.info("[MailUpdateJob] confirm userId is {},cardTypeId is {}",userId,cardTypeId);
			//AppConfig appConfig = SpringContext.getBean("appConfig");

			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			String url = PropertiesUtil.getContexrtParam("service.url") + "/api/mail/auto/update/1.0";

			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("userId", userId);
			paramMap.put("cardTypeId", cardTypeId);

			try {
				HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			} catch (Exception e) {
				logger.error("[MailUpdateJob] execute Error");
				throw new JobExecutionException(e);
			}
			logger.info("[MailUpdateJob] Ending now .... ");
		}else{
			logger.warn("[MailUpdateJob] The flag[SWITCH_JOB_MAIL_UPDATE]  is Closed");
		}
	}
}
