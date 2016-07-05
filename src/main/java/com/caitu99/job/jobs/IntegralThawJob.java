package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;

/**
 * 订单自动关闭任务
 * @ClassName: OrderCloseJob
 * @Description: (这里用一句话描述这个类的作用)
 * @author dzq
 * @date 2014年8月20日 下午6:08:11
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IntegralThawJob implements Job {
	

	private static final Logger logger = LoggerFactory.getLogger(IntegralThawJob.class);
	
	
	@Override
	public void execute(JobExecutionContext context){

		Long userId = (Long) context.getJobDetail().getJobDataMap().get("userId");
		String orderNo = (String) context.getJobDetail().getJobDataMap().get("orderNo");
		try {
			logger.info("[IntegralThawJob] thaw userId is : {}, orderNo is : {}" ,userId,orderNo);
			
			//AppConfig appConfig = (AppConfig)SpringContext.getBean("appConfig");
		
			//解冻财分处理
			//发送http请求
			String url = PropertiesUtil.getContexrtParam("service.url") + "/api/transaction/thaw/1.0";

			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("userId", userId.toString());
			paramMap.put("orderNo", orderNo);
			
			HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			
		} catch (Exception e) {
			logger.error("[IntegralThawJob] thaw userId is : {}, orderNo is : {}" ,userId,orderNo);
			logger.error("[IntegralThawJob] Have Exception : {}",e);
		}
	}
}
