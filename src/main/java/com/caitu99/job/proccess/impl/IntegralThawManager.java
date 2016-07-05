package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.IntegralThawJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

@Service("integralThawManager")
public class IntegralThawManager implements JobTargetManager {
	
	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String orderNo = json.getString("orderNo");
		Long userId = Long.parseLong(json.getString("userId"));
		thawIntegral(userId, orderNo);
		return true;
	}
	
	/**
	 * 自动解冻企业财分
	 * 	
	 * @Description: (方法职责详细描述,可空)  
	 * @Title: thawIntegral 
	 * @param userId
	 * @throws SchedulerException
	 * @date 2015年12月1日 上午11:47:25  
	 * @author ws
	 */
	public void thawIntegral(Long userId, String orderNo)throws SchedulerException{
		try {
			if (StringUtils.isBlank(orderNo)) {
				throw new IllegalArgumentException("The param  \"orderNo\"  must be not null");
			}
			if (null == userId) {
				throw new IllegalArgumentException("The param  \"userId\"  must be not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_INTEGRAL_THAW");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("orderNo", orderNo);
			jobParamMap.put("userId", userId);
			String jobName = QuarzKeyUtil.getJobName("IntegralThawJobName", paramDate);
			String jobGroupName ="IntegralThawJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,IntegralThawJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}


	
}
