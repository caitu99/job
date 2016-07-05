/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;

/**
 * 银联支付订单结果查询
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: UnionPayOrderQueryJob 
 * @author ws
 * @date 2015年12月31日 下午2:07:33 
 * @Copyright (c) 2015-2020 by caitu99
 */
public class UnionPayOrderQueryJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(UnionPayOrderQueryJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			logger.info("[UnionPayOrderQueryJob] Run now .... ");
			
			String userId = (String)context.getJobDetail().getJobDataMap().get("userId");
			String orderNo = (String)context.getJobDetail().getJobDataMap().get("orderNo");
			String txnTime = (String)context.getJobDetail().getJobDataMap().get("txnTime");
			String url = (String)context.getJobDetail().getJobDataMap().get("url");
			logger.info("[UnionPayOrderQueryJob] confirm userId is {},orderNo is {}" ,userId,orderNo);

			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			if(StringUtils.isBlank(url)){
				url = PropertiesUtil.getContexrtParam("service.url") + PropertiesUtil.getContexrtParam("unionpay.order.query.url");
			}
			
			logger.info("url:"+url);
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("userId", userId);
			paramMap.put("orderNo", orderNo);
			paramMap.put("txnTime", txnTime);

			HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[UnionPayOrderQueryJob] Ending now .... ");
		} catch (Exception e) {
			logger.error("[UnionPayOrderQueryJob] Have Exception :"+e.getMessage(),e);
			throw new JobExecutionException(e);
		}
	}

}
