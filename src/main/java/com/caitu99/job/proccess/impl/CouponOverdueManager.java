package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.CouponOverdueJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

@Service("couponOverdueManager")
public class CouponOverdueManager implements JobTargetManager {
	
	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String id = json.getString("id");
		couponOverdue(id);
		return true;
	}
	
	/**
	 * 券积分自动过期
	 * @Title: closeOrder
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param id
	 * @date 2014年8月20日 上午9:25:04
	 * @author dzq
	 */
	public void couponOverdue(String id)throws SchedulerException{
		try {
			if (StringUtils.isBlank(id)) {
				throw new IllegalArgumentException("The param  \"id\" must be not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_COUPON_OVERDUE");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("id", id);
			String jobName = QuarzKeyUtil.getJobName("CouponOverdueJobName", paramDate);
			String jobGroupName ="CouponOverdueJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,CouponOverdueJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}


	
}
