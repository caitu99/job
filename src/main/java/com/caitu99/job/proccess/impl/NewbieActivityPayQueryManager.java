package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.NewbieActivityPayQueryJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

@Service("newbieActivityPayQueryManager")
public class NewbieActivityPayQueryManager implements JobTargetManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String userId = json.getString("userId");
		String orderNo = json.getString("orderNo");
		String txnTime = json.getString("txnTime");
		String caibi = json.getString("caibi");
		String tubi = json.getString("tubi");
		newbieActivityPayQuery(userId,orderNo,txnTime,caibi,tubi);
		return true;
	}

	public void newbieActivityPayQuery(String userId,String orderNo, String txnTime,String caibi,String tubi)throws SchedulerException{
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
			if (StringUtils.isBlank(caibi)) {
				throw new IllegalArgumentException("The param  \"caibi\" must be not null");
			}
			if (StringUtils.isBlank(tubi)) {
				throw new IllegalArgumentException("The param  \"tubi\" must be not null");
			}
			
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_UNION_ORDER_QUERY");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("userId", userId);
			jobParamMap.put("orderNo", orderNo);
			jobParamMap.put("txnTime", txnTime);
			jobParamMap.put("caibi", caibi);
			jobParamMap.put("tubi", tubi);
			String jobName = QuarzKeyUtil.getJobName("NewbieActivityPayQueryJobName", paramDate);
			String jobGroupName ="NewbieActivityPayQueryJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,NewbieActivityPayQueryJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
}
