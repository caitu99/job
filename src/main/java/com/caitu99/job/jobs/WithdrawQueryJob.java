package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;

public class WithdrawQueryJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(WithdrawQueryJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.info("[WithdrawQueryJob] Run now .... ");
			
			String id = (String)context.getJobDetail().getJobDataMap().get("id");
			logger.info("[WithdrawQueryJob] confirm id is {}" ,id);

			String url = PropertiesUtil.getContexrtParam("service.url") + "/api/transaction/withdraw/query/1.0";
			logger.info("url:"+url);
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("id", id);

			String flag = HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[WithdrawQueryJob] flag is {}",flag);
			JSONObject json = JSONObject.parseObject(flag);
			if(json == null || "-1".equals(json.getString("code"))){
				logger.info("[WithdrawQueryJob] 查询失败，需要再次查询");
				throw new JobExecutionException("需要再次执行");
			}else{
				logger.info("[WithdrawQueryJob] Ending now .... ");
				
			}
		} catch (Exception e) {
			logger.error("[WithdrawQueryJob] Have Exception :"+e.getMessage(),e);
			throw new JobExecutionException(e);
		}
	}

}
