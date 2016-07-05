package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.WithdrawQueryJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

@Service("withdrawQueryManager")
public class WithdrawQueryManager implements JobTargetManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String id = json.getString("id");
		try {
			if (StringUtils.isBlank(id)) {
				throw new IllegalArgumentException("The param  \"id\" must be not null");
			}
			
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_WITHDRAW_QUERY");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("id", id);
			String jobName = QuarzKeyUtil.getJobName("WithdrawQueryJobName", paramDate);
			String jobGroupName ="WithdrawQueryJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,WithdrawQueryJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		return null;
	}

}
