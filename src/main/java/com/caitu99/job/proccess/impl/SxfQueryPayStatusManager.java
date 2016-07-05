package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.I0Itec.zkclient.DataUpdater;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.SxfQueryPayStatusJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.DateStyle;
import com.caitu99.job.quarz.util.DateUtil;
import com.caitu99.job.quarz.util.ProccessUtil;
import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.quarz.util.QuarzKeyUtil;
@Service("sxfQueryPayStatusManager")
public class SxfQueryPayStatusManager implements JobTargetManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String transactionNumber = json.getString("transactionNumber");
		try {
			if (StringUtils.isBlank(transactionNumber)) {
				throw new IllegalArgumentException("The param  \"transactionNumber\" must be not null");
			}
			
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_SXF_QUERY_PAY");
			//获取执行间隔时间
			String interval = PropertiesUtil.getContexrtParam("TIMER_JOB_SXF_QUERY_PAY_INTERVAL_IN_SECONDS");
			String tomorrowParamDate = DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD);
			Date date = DateUtil.StringToDate(tomorrowParamDate,DateStyle.YYYY_MM_DD);
			date = DateUtil.addDay(date, 1);
			date = DateUtil.addHour(date, 10);
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("transactionNumber", transactionNumber);
			String jobName = QuarzKeyUtil.getJobName("SxfQueryPayStatusJobName", paramDate);
			String jobGroupName ="SxfQueryPayStatusJobGroup";
			//当天执行
			quartzSimplePersistenceClient.addRepeatJob(jobName, jobGroupName, SxfQueryPayStatusJob.class, paramDate, jobParamMap, 150, Integer.valueOf(interval));
			//第二天执行1次
			quartzSimplePersistenceClient.addRepeatJob("tomorrow"+jobName,"tomorrow"+jobGroupName,SxfQueryPayStatusJob.class, date,jobParamMap,24,60*60);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		return true;
	}

}
