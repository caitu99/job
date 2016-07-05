/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.proccess;

import org.quartz.SchedulerException;

import com.alibaba.fastjson.JSONObject;

/** 
 * Job管理器
 * @Description: (类职责详细描述,可空) 
 * @ClassName: JobManager 
 * @author lawrence
 * @date 2015年12月2日 下午1:36:34 
 * @Copyright (c) 2015-2020 by caitu99 
 */
public interface JobTargetManager  {
	
	/**
	 * 初始化任务接口	
	 * @Description: (方法职责详细描述,可空)  
	 * @Title: proccess 
	 * @param message
	 * @return
	 * @date 2015年12月2日 下午1:37:56  
	 * @author lawrence
	 */
	Boolean initiatingTask(JSONObject json) throws SchedulerException;
}
