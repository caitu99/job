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
import com.caitu99.job.jobs.IntegralRechargeQueryJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

/** 
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: IntegralRechargeQueryManager 
 * @author Hongbo Peng
 * @date 2015年12月11日 上午9:28:29 
 * @Copyright (c) 2015-2020 by caitu99 
 */
@Service("integralRechargeQueryManager")
public class IntegralRechargeQueryManager implements JobTargetManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String id = json.getString("id");
		integralRechargeQuery(id);
		return true;
	}

	public void integralRechargeQuery(String id)throws SchedulerException{
		try {
			if (StringUtils.isBlank(id)) {
				throw new IllegalArgumentException("The param  \"id\" must be not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_INTEGRAL_RECHARGE_QUERY");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("id", id);
			String jobName = QuarzKeyUtil.getJobName("IntegralRechargeQueryJobName", paramDate);
			String jobGroupName ="IntegralRechargeQueryJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,IntegralRechargeQueryJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
}
