package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.OrderCloseJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

@Service("orderJobManager")
public class OrderJobManager implements JobTargetManager {
	
	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String orderNo = json.getString("orderNo");
		String userId = json.getString("userId");
		closeOrder(orderNo,userId);
		return true;
	}
	
	/**
	 * 自动关闭订单
	 * @Title: closeOrder
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param orderNo
	 * @date 2014年8月20日 上午9:25:04
	 * @author dzq
	 */
	public void closeOrder(String orderNo,String userId)throws SchedulerException{
		try {
			if (StringUtils.isBlank(orderNo)||StringUtils.isBlank(orderNo)) {
				throw new IllegalArgumentException("The param  \"orderNo\" or \"userId\" must be not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_ORDER_CLOSE");
			String interval = PropertiesUtil.getContexrtParam("TIMER_JOB_ORDER_CLOSE_INTERVAL_IN_SECONDS");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("orderNo", orderNo);
			jobParamMap.put("userId", userId);
			String jobName = QuarzKeyUtil.getJobName("OrderCloseJobName", paramDate);
			String jobGroupName ="OrderCloseJobGroup";
			quartzSimplePersistenceClient.addRepeatJob(jobName,jobGroupName,OrderCloseJob.class, paramDate, jobParamMap, 10, Integer.valueOf(interval));
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}


	
}
