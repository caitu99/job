package com.caitu99.job.quarz.client;

import java.util.Map;

import org.quartz.Job;
import org.quartz.SchedulerException;

public interface QuartzCronPersistenceClient {

	
	/**
	 * 注册执行化任务
	 * @Title: join 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param clazz 执行任务类
	 * @param date 	执行时间
	 * @throws SchedulerException
	 * @date 2014年8月19日 下午4:59:46  
	 * @author dzq
	 */
	void addJob(Class<? extends Job> clazz,String cronExpression) throws SchedulerException;
	
	/**
	 * 入库注册任务
	 * @Title: regist 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @date 2014年8月19日 下午3:34:14  
	 * @author dzq
	 * @param jobParamMap 
	 * @param JobClass 
	 */
	void addJob(Class<? extends Job> clazz,String cronExpression,Map<String, Object> jobParamMap)
			throws SchedulerException;
	
	/**
	 * 入库注册任务
	 * @Title: regist 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @date 2014年8月19日 下午3:34:14  
	 * @author dzq
	 * @param jobParamMap 
	 * @param JobClass 
	 */
	void addJob(String groupName,Class<? extends Job> clazz,String cronExpression,Map<String, Object> jobParamMap)
			throws SchedulerException;
	
	/**
	 * 入库注册任务
	 * @Title: regist 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @date 2014年8月19日 下午3:34:14  
	 * @author dzq
	 * @param jobParamMap 
	 * @param JobClass 
	 */
	void addJob(String jobName,String groupName,Class<? extends Job> clazz,String cronExpression,Map<String, Object> jobParamMap)
			throws SchedulerException;
	
	/**
	 * 删除任务
	 * @Title: deleteJob 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param jobName
	 * @param jobGroupName
	 * @date 2014年8月21日 下午4:13:27  
	 * @author dzq
	 */
	void deleteJob(String jobName,String jobGroupName)throws SchedulerException;
	
}
