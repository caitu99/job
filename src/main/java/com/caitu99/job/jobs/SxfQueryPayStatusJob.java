package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;
import com.caitu99.job.util.spring.SpringContext;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SxfQueryPayStatusJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(SxfQueryPayStatusJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			logger.info("[SxfQueryPayStatusJob] Run now .... ");
			
			String transactionNumber = (String)context.getJobDetail().getJobDataMap().get("transactionNumber");
			logger.info("[SxfQueryPayStatusJob] confirm transactionNumber is {}" ,transactionNumber);

			String url = PropertiesUtil.getContexrtParam("service.url") + "/api/transaction/unionpaysmart/job/1.0";
//			String url =  "http://192.168.25.153:8091/api/transaction/unionpaysmart/job/1.0";
			logger.info("url:"+url);
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("transactionNumber", transactionNumber);

			String flag = HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[SxfQueryPayStatusJob] flag is {}",flag);
			if("AGAIN".equals(flag)){
				logger.info("[SxfQueryPayStatusJob] 需要再次执行");
			}else{
				logger.info("[SxfQueryPayStatusJob] 执行完成，删除JOB");
				//删除JOb
				Scheduler persistenceScheduler = SpringContext.getBean("persistenceScheduler");
				persistenceScheduler.deleteJob(context.getJobDetail().getKey());
			}
		} catch (Exception e) {
			logger.error("[SxfQueryPayStatusJob] Have Exception :"+e.getMessage(),e);
		}

	}

}
