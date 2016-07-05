/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.job.mq.kafka;

import com.caitu99.job.proccess.JobTargetManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.kafka.core.KafkaMessage;
import org.springframework.integration.kafka.listener.MessageListener;
import org.springframework.integration.kafka.serializer.common.StringDecoder;
import org.springframework.integration.kafka.util.MessageUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/** 
 * kafka 消息消费者
 * @Description: (类职责详细描述,可空) 
 * @ClassName: ConsumerMessageListener 
 * @author Hongbo Peng
 * @date 2015年11月30日 下午6:00:19 
 * @Copyright (c) 2015-2020 by caitu99 
 */
@Service("consumerMessageListener")
public class ConsumerMessageListener implements MessageListener {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String JOB_TYPE = "jobType";
	
	@Autowired
	private ConsumerMessageAdapter consumerMessageAdapter;

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
    private JobTargetManager withdrawQueryManager;
    @Autowired
    private JobTargetManager newbieActivityPayQueryManager;
    @Autowired
    private JobTargetManager sxfQueryPayStatusManager;
    @Autowired
    private JobTargetManager lianlianPayTimeoutManager;
	
	@Override
	public void onMessage(KafkaMessage message) {
		try {
			/** 1.获取消息内容 */
			String msgStr = MessageUtils.decodePayload(message, new StringDecoder());
			logger.debug("Listen to the message is {}",msgStr);
			/** 2.消息内容为空直接返回 */
			if(StringUtils.isBlank(msgStr)){
				logger.warn("Listen to the message is null,topic is null");
				return ;
			}
			/** 3.消息内容处理 */
			JSONObject json = JSON.parseObject(msgStr);
			logger.info("start Consumer kafka Message is {}", json);
			//适配器模式
			//consumerMessageAdapter.setJobType(json.getString(JOB_TYPE));
			//boolean flag = consumerMessageAdapter.initiatingTask(json);
            boolean flag = false;
            switch (json.getString(JOB_TYPE)) {
                case "INTEGRAL_THAW": {
                    flag = integralThawManager.initiatingTask(json);
                    break;
                }
                case "INTERNAL_ORDER": {
                    flag = orderJobManager.initiatingTask(json);
                    break;
                }
                case "USER_COUPON_OVERDUE": {
                    flag = couponOverdueManager.initiatingTask(json);
                    break;
                }
                case "INTEGRAL_RECHARGE_QUERY_JOB": {
                    flag = integralRechargeQueryManager.initiatingTask(json);
                    break;
                }
                case "UNIFY_PUSH_MESSAGE_JOB": {
                    flag = pushMessageManager.initiatingTask(json); //统一消息推送任务
                    break;
                }
                case "UNION_PAY_ORDER_QUERY_JOB": {
                    flag = unionPayOrderQueryManager.initiatingTask(json);
                    break;
                }
                case "REALIZE_TRANSFER_JOB": {
                    flag = realizeTransferManager.initiatingTask(json);
                    break;
                }
                case "WITHDRAW_QUERY_JOB": {
                    flag = withdrawQueryManager.initiatingTask(json);
                    break;
                }
                case "NEWBIE_ACTIVITY_UNION_PAY_ORDER_QUERY_JOB": {
                    flag = newbieActivityPayQueryManager.initiatingTask(json);
                    break;
                }
                case "SXF_QUERY_PAY_STATUS_JOB": {
                    flag = sxfQueryPayStatusManager.initiatingTask(json);
                    break;
                }
                case "LIANLIAN_PAY_TIMEOUT": {
                    flag = lianlianPayTimeoutManager.initiatingTask(json);
                    break;
                }
                default: {
                    logger.error("unknown type {}", json.getString(JOB_TYPE));
                }
            }
            logger.info(" Consumer kafka Message is {}, {}",flag, json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("kafka消息消费发生异常：{}",e);
		}
	}
	
	

	
}
