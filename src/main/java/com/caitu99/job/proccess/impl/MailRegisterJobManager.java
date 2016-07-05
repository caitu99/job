package com.caitu99.job.proccess.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.jobs.MailUpdateJob;
import com.caitu99.job.proccess.JobTargetManager;
import com.caitu99.job.quarz.client.QuartzSimplePersistenceClient;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

@Service("mailUpdateJobManager")
public class MailRegisterJobManager implements JobTargetManager {
	
	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	
	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		String userId = json.getString("userId");
		String cardTypeId = json.getString("cardTypeId");
		String dateLong = json.getString("dateLong");//注册执行时间
		registerMailUpdateJob(userId,cardTypeId,dateLong);
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
	public void registerMailUpdateJob(String userId,String cardTypeId,String dateLong)throws SchedulerException{
		try {
			if (StringUtils.isBlank(userId)||StringUtils.isBlank(cardTypeId)
					|| StringUtils.isBlank(dateLong)) {
				throw new IllegalArgumentException("The param  \"userId\" or \"cardTypeId\" or \"dateLong\" must be not null");
			}
			//获取配置时间
			Date paramDate = DateUtils.parseDate(dateLong);
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("userId", userId);
			jobParamMap.put("cardTypeId", cardTypeId);
			String jobName = QuarzKeyUtil.getJobName(
						"mailUpdateJobName#"+userId+"#"+cardTypeId, paramDate);
			String jobGroupName ="mailUpdateJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,MailUpdateJob.class, paramDate,jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}


	
}
