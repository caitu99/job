package com.caitu99.job.quarz.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.caitu99.job.quarz.bean.SimpleJobBean;
import com.caitu99.job.quarz.util.QuarzKeyUtil;

/**
 * 自定义重复次数Schedule
 * @ClassName: RepeatSchedulePersistence 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author dzq
 * @date 2014年8月25日 下午4:46:20
 */
@Component
public class SimpleRepeatSchedulePersistence {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Scheduler persistenceScheduler;
	/**
	 * 启动
	 * @Title: run 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param jobBean
	 * @throws SchedulerException
	 * @date 2014年8月21日 下午3:43:54  
	 * @author dzq
	 */
	public void scheduleJob(SimpleJobBean bean) throws SchedulerException {
		try {
			/** 获取组名称 */
			String schedId = persistenceScheduler.getSchedulerInstanceId();
			logger.info("-------Simple Repeat Scheduling Job  -------------------");
			String jobName = null;
			if (null == bean.getJobName() || "".equals(bean.getJobName())) {
				jobName = QuarzKeyUtil.getJobName("SIMPLE_JOB_NAME",bean.getExeDate());
			}else {
				jobName = bean.getJobName();
			}
			JobDetail job = newJob(bean.getJobClazz())
					.withIdentity(new JobKey(jobName, bean.getJobGroupName()))
					.withDescription(QuarzKeyUtil.getJobDescription(bean.getExeDate()))
					.requestRecovery().build();
			if (null != bean.getParams() && !bean.getParams().isEmpty()) {
				for (String key : bean.getParams().keySet()) {
					job.getJobDataMap().put(key,bean.getParams().get(key));
				}
			}
			TriggerKey triggerKey = new TriggerKey("SIMPLE_TRIGER_" +QuarzKeyUtil.getTrigerName(bean.getExeDate()), schedId);  
			Trigger trigger = newTrigger()
					.withIdentity(triggerKey)
					.startAt(bean.getExeDate())
					.withSchedule(simpleSchedule()
					.withMisfireHandlingInstructionFireNow()
					.withIntervalInSeconds(bean.getIntervalInSeconds())
					.withRepeatCount(bean.getRepeatCount()))
					.build();
			if (persistenceScheduler.checkExists(triggerKey)) {
				persistenceScheduler.rescheduleJob(triggerKey, trigger);
			}else{
				persistenceScheduler.scheduleJob(job, trigger);
			}
			logger.info("{} will run at: {} ",job.getKey(), bean.getExeDate());
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
	/**
	 * 删除任务
	 * @Title: deleteJob 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param jobName
	 * @param JobGroupName
	 * @throws SchedulerException
	 * @date 2014年8月21日 下午4:48:45  
	 * @author dzq
	 */
	public void deleteJob(String jobName,String jobGroupName) throws SchedulerException{
		try {
			JobKey jobKey = new JobKey(jobName,jobGroupName);
			persistenceScheduler.deleteJob(jobKey);
			logger.info("{} will stoped ",jobKey);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

}
