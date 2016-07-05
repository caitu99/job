/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job;

import org.junit.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import com.caitu99.job.proccess.impl.OrderJobManager;

/** 
 * 例子
 * @Description: (类职责详细描述,可空) 
 * @ClassName: DemoTest 
 * @author lawrence
 * @date 2015年12月2日 下午12:09:13 
 * @Copyright (c) 2015-2020 by caitu99 
 */
public class DemoTest extends AbstractJunitTest{

	@Autowired
	private OrderJobManager orderJobManager;

	@Test
	public void testOrderClose(){
		//开启一个订单关闭任务
		String orderNo = "123123123";
//		try {
//			orderJobManager.closeOrder(orderNo);
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
	}
}
