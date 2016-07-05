/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.UnionPayOrderQueryJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

/** 
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: IntegralRechargeQueryManager 
 * @author Hongbo Peng
 * @date 2015年12月11日 上午9:28:29 
 * @Copyright (c) 2015-2020 by caitu99 
 */
@Service("unionPayOrderQueryManager")
public class UnionPayOrderQueryManager implements JobTargetManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String userId = json.getString("userId");
		String orderNo = json.getString("orderNo");
		String txnTime = json.getString("txnTime");
		String url = json.getString("url");
		unionPayOrderQuery(userId,orderNo,txnTime,url);
		return true;
	}

	public void unionPayOrderQuery(String userId,String orderNo, String txnTime, String url)throws SchedulerException{
		try {
			if (StringUtils.isBlank(userId)) {
				throw new IllegalArgumentException("The param  \"userId\" must be not null");
			}
			if (StringUtils.isBlank(orderNo)) {
				throw new IllegalArgumentException("The param  \"orderNo\" must be not null");
			}
			if (StringUtils.isBlank(txnTime)) {
				throw new IllegalArgumentException("The param  \"txnTime\" must be not null");
			}
			
			if(StringUtils.isBlank(url)){
				url = PropertiesUtil.getContexrtParam("service.url") + PropertiesUtil.getContexrtParam("unionpay.order.query.url");
			}
			
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_UNION_ORDER_QUERY");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("userId", userId);
			jobParamMap.put("orderNo", orderNo);
			jobParamMap.put("txnTime", txnTime);
			jobParamMap.put("url", url);
			String jobName = QuarzKeyUtil.getJobName("UnionPayOrderQueryJobName", paramDate);
			String jobGroupName ="UnionPayOrderQueryJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,UnionPayOrderQueryJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
}
