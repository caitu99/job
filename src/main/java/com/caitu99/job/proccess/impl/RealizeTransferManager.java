package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.RealizeTransferJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

@Service("realizeTransferManager")
public class RealizeTransferManager implements JobTargetManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		try {
			String realizeRecordId = json.getString("realizeRecordId");
			String shopName = json.getString("shopName");
			Date paramDate = null;
			if (StringUtils.isBlank(realizeRecordId)) {
				throw new IllegalArgumentException("The param  \"realizeRecordId\" must be not null");
			}

			if( StringUtils.isBlank(shopName) ){
				//获取配置时间
				paramDate = ProccessUtil.getTime("TIMER_JOB_REALIZE_TRANSFER");
			}else{
				paramDate = ProccessUtil.getTime(shopName);
			}
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("realizeRecordId", realizeRecordId);
			String jobName = QuarzKeyUtil.getJobName("RealizeTransferJobName", paramDate);
			String jobGroupName ="RealizeTransferJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,RealizeTransferJob.class, paramDate,jobParamMap);
			return true;
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

}
