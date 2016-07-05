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
 * 新手任务银联支付订单结果查询
 * @Description: (类职责详细描述,可空) 
 * @ClassName: NewbieActivityPayQueryJob 
 * @author xiongbin
 * @date 2016年5月12日 下午5:25:47 
 * @Copyright (c) 2015-2020 by caitu99
 */
public class NewbieActivityPayQueryJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(NewbieActivityPayQueryJob.class);
	
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		try {
			logger.info("[NewbieActivityPayQueryJob] Run now .... ");
			
			String userId = (String)context.getJobDetail().getJobDataMap().get("userId");
			String orderNo = (String)context.getJobDetail().getJobDataMap().get("orderNo");
			String txnTime = (String)context.getJobDetail().getJobDataMap().get("txnTime");
			String caibi = (String)context.getJobDetail().getJobDataMap().get("caibi");
			String tubi = (String)context.getJobDetail().getJobDataMap().get("tubi");
			logger.info("[NewbieActivityPayQueryJob] confirm userId is {},orderNo is {}" ,userId,orderNo);

			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			String url = PropertiesUtil.getContexrtParam("service.url") + "/api/activities/newbie/pay/order/query/1.0";
			logger.info("url:"+url);
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("userId", userId);
			paramMap.put("orderNo", orderNo);
			paramMap.put("txnTime", txnTime);
			paramMap.put("caibi", caibi);
			paramMap.put("tubi", tubi);

			HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[NewbieActivityPayQueryJob] Ending now .... ");
		} catch (Exception e) {
			logger.error("[NewbieActivityPayQueryJob] Have Exception :"+e.getMessage(),e);
			throw new JobExecutionException(e);
		}
	}

}
