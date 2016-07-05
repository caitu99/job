package com.caitu99.job.jobs;

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
 * 手动查询自动发现任务
 * @Description: (类职责详细描述,可空) 
 * @ClassName: ManualQueryAutoFindJob 
 * @author xiongbin
 * @date 2015年12月22日 上午9:42:40 
 * @Copyright (c) 2015-2020 by caitu99
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ManualQueryAutoFindJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(ManualQueryAutoFindJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_MANUAL_QUERY_AUTO_FIND");
		logger.info("[ManualQueryAutoFindJob] The jobFlag[SWITCH_JOB_MANUAL_QUERY_AUTO_FIND] of SWITCH_JOB_MANUAL_QUERY_AUTO_FIND is " + jobFlag);
		if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {
			logger.info("[ManualQueryAutoFindJob] Run now .... ");

			//发送http请求
			String url = PropertiesUtil.getContexrtParam("service.url") + "/api/integral/manual/auto/find/userAll/1.0";

			try {
				HttpClientUtils.getInstances().doPost(url,"UTF-8","");
			} catch (Exception e) {
				logger.error("[ManualQueryAutoFindJob] execute Error");
				throw new JobExecutionException(e);
			}
			logger.info("[ManualQueryAutoFindJob] Ending now .... ");
		}else{
			logger.warn("[ManualQueryAutoFindJob] The flag[SWITCH_JOB_MANUAL_QUERY_AUTO_FIND]  is Closed");
		}
	}
}
