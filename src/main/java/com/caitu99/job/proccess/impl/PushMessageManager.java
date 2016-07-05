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
import com.caitu99.job.jobs.PushMessageJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.DateUtil;
import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

/** 
 * 
 * @Description: (类职责详细描述,可空) 
 * @ClassName: PushMessageManager 
 * @author Hongbo Peng
 * @date 2015年12月23日 下午2:25:23 
 * @Copyright (c) 2015-2020 by caitu99 
 */
@Service("pushMessageManager")
public class PushMessageManager implements JobTargetManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	/* (non-Javadoc)
	 * @see com.caitu99.job.proccess.JobTargetManager#initiatingTask(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String messageId = json.getString("messageId");
		pushMessage(messageId);
		return true;
	}

	public void pushMessage(String messageId) throws SchedulerException{
		try {
			if (StringUtils.isBlank(messageId)) {
				throw new IllegalArgumentException("The param  \"messageId\" must be not null");
			}
			//计算时间规则 默认配置10-23点发送，不在就顺延
			Date paramDate = null;
			String rule = PropertiesUtil.getContexrtParam("unify.push.message.rule");
			if(StringUtils.isBlank(rule)){
				throw new IllegalArgumentException("The config  \"unify.push.message.rule\" must be not null");
			}
			String[] rules = rule.split("-");
			int startHour = Integer.valueOf(rules[0]);
			int endHour = Integer.valueOf(rules[1]);
			Date date = new Date();
			int nowHour = DateUtil.getHour(date);
			if(nowHour < startHour){
				//小于开始时间
				paramDate = DateUtil.addHour(date, startHour-nowHour);
			} else if(nowHour >= endHour){
				//大于结束时间
				paramDate = DateUtil.addHour(date, 24-endHour + startHour);
			} else {
				//可发送时间范围内
				paramDate = DateUtil.addSecond(date, 5);
			}
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("messageId", messageId);
			String jobName = QuarzKeyUtil.getJobName("UnifyPushMessageJobName", paramDate);
			String jobGroupName ="UnifyPushMessageJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,PushMessageJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
}
