package com.caitu99.job.jobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caitu99.job.quarz.util.PropertiesUtil;
import com.caitu99.job.util.http.HttpClientUtils;

/**
 * 订单自动关闭任务
 * @ClassName: OrderCloseJob
 * @Description: (这里用一句话描述这个类的作用)
 * @author dzq
 * @date 2014年8月20日 下午6:08:11
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CouponOverdueJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(CouponOverdueJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			logger.info("[CouponOverdueJob] Run now .... ");
			
			String id = (String)context.getJobDetail().getJobDataMap().get("id");
			logger.info("[CouponOverdueJob] confirm id is {}" ,id);

			//发送http请求
			//String url = appConfig.serviceUrl + "/api/transaction/order/closeorder/1.0";
			String url = PropertiesUtil.getContexrtParam("service.url") + PropertiesUtil.getContexrtParam("coupon.overdue.url");

			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("id", id);

			HttpClientUtils.getInstances().doPost(url, "UTF-8",paramMap);
			logger.info("[CouponOverdueJob] Ending now .... ");
		} catch (Exception e) {
			logger.error("[CouponOverdueJob] Have Exception :"+e.getMessage(),e);
			throw new JobExecutionException(e);
		}
	}
}
