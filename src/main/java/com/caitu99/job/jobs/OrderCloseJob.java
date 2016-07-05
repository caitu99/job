package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;
import com.caitu99.job.util.spring.SpringContext;

/**
 * 订单自动关闭任务
 * @ClassName: OrderCloseJob
 * @Description: (这里用一句话描述这个类的作用)
 * @author dzq
 * @date 2014年8月20日 下午6:08:11
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OrderCloseJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(IntegralThawJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_ORDER_CLOSE");
			logger.info("[OrderCloseJob] The jobFlag[ConfirmAppointOrderJob] of SWITCH_JOB_ConfirmAppointOrderJob is "+jobFlag);
			if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {

				logger.info("[OrderCloseJob] Run now .... ");
				String orderNo = (String) context.getJobDetail().getJobDataMap().get("orderNo");
				String userId = (String) context.getJobDetail().getJobDataMap().get("userId");
				logger.info("[OrderCloseJob] confirm orderNo is {}" +orderNo);
				//AppConfig appConfig = SpringContext.getBean("appConfig");

				//发送http请求
				//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
				String url = PropertiesUtil.getContexrtParam("service.url") + "/api/transaction/order/closeorder/1.0";

				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("userId", userId);
				paramMap.put("orderNo", orderNo);

				String result = HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
				
				if("success".equals(result)){
					logger.info("[OrderCloseJob] 执行完成，删除JOB");
					//删除JOb
					Scheduler persistenceScheduler = SpringContext.getBean("persistenceScheduler");
					persistenceScheduler.deleteJob(context.getJobDetail().getKey());
				}
				logger.info("[OrderCloseJob] Ending now .... ");
			}else{
				logger.warn("[OrderCloseJob] The flag[SWITCH_JOB_ORDER_CLOSE]  is Closed");
				throw new JobExecutionException("[OrderCloseJob] The flag[SWITCH_JOB_ORDER_CLOSE]  is Closed");
			}
		} catch (Exception e) {

			throw new JobExecutionException("request error");
		}
	}
}
