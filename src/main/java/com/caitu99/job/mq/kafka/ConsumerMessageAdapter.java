/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.mq.kafka;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.caitu99.job.proccess.JobTargetManager;

/**
 * 
 * @Description: (类职责详细描述,可空)
 * @ClassName: ConsumerMessageCall
 * @author lawrence
 * @date 2015年12月2日 下午1:35:20
 * @Copyright (c) 2015-2020 by caitu99
 */
@Component("consumerMessageAdapter")
public class ConsumerMessageAdapter implements JobTargetManager {

	private JobTargetManager traget;
	
	@Autowired
	private JobTargetManager orderJobManager;
	@Autowired
	private JobTargetManager integralThawManager;
	@Autowired
	private JobTargetManager couponOverdueManager;
	@Autowired
	private JobTargetManager integralRechargeQueryManager;
	@Autowired
	private JobTargetManager mailUpdateJobManager;
	@Autowired
	private JobTargetManager pushMessageManager;
	@Autowired
	private JobTargetManager unionPayOrderQueryManager;
	@Autowired
	private JobTargetManager realizeTransferManager;
	@Autowired
	private JobTargetManager newbieActivityPayQueryManager;
	@Autowired
	private JobTargetManager lianlianPayTimeoutManager;
	/**
	 * 有新的类型时，请在此写入
	 * @Description: (方法职责详细描述,可空)  
	 * @Title: setJobType 
	 * @param jobType
	 * @date 2015年12月2日 下午2:17:22  
	 * @author lawrence
	 */
	public void setJobType(String jobType) {
		if ("INTEGRAL_THAW".equals(jobType)) {
			this.setTraget(integralThawManager);//解冻定时任务
		}
		else  if ("INTERNAL_ORDER".equals(jobType))
		{
			this.setTraget(orderJobManager);		//订单关闭任务
		}
		else if ("USER_COUPON_OVERDUE".equals(jobType))
		{
			this.setTraget(couponOverdueManager); //券积分到期任务
		}
		else if ("INTEGRAL_RECHARGE_QUERY_JOB".equals(jobType))
		{
			this.setTraget(integralRechargeQueryManager); //券积分到期任务
		}
		else if ("UNIFY_PUSH_MESSAGE_JOB".equals(jobType)){
			this.setTraget(pushMessageManager); //统一消息推送任务
		}
		else if ("UNION_PAY_ORDER_QUERY_JOB".equals(jobType))
		{
			this.setTraget(unionPayOrderQueryManager); //银联充值结果查询
		}
		else if ("REALIZE_TRANSFER_JOB".equals(jobType))
		{
			this.setTraget(realizeTransferManager); //银联充值结果查询
		}
		else if ("NEWBIE_ACTIVITY_UNION_PAY_ORDER_QUERY_JOB".equals(jobType))
		{
			this.setTraget(newbieActivityPayQueryManager); //新手任务银联支付结果查询
		}
		else if ("LIANLIAN_PAY_TIMEOUT".equals(jobType))
		{
			this.setTraget(lianlianPayTimeoutManager); //连连支付结果查询
		}
		else{
			//其余类型请写入此处
		}
	}
	
	
	
	
	
	public ConsumerMessageAdapter() {

	}

	public ConsumerMessageAdapter(JobTargetManager traget) {
		this.traget = traget;
	}

	@Override
	public Boolean initiatingTask(JSONObject json) throws SchedulerException {
		if(null == this.traget){
			return false;
		}
		return traget.initiatingTask(json);
	}
	
	public JobTargetManager getTraget() {
		return traget;
	}

	public void setTraget(JobTargetManager traget) {
		this.traget = traget;
	}

}
