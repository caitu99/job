package com.caitu99.job.quarz.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class ProccessUtil {

	public static Date getTime(String configKey) throws Exception {
		try {
			String configTimer = PropertiesUtil.getContexrtParam(configKey);
			if (StringUtils.isBlank(configTimer)) {
				throw new IllegalArgumentException("The  configuration \"configTimer\" must be  not null,"
						+ "please check config files[job.properties]!");
			}
			Date date = new Date();
			String timer=configTimer.substring(0, configTimer.length()-1);
			if (StringUtils.isBlank(timer)) {
				throw new IllegalArgumentException("The  configuration \"timer\" must be not null,"
						+ "please check config files[job.properties]!");
			}
			if (-1 != configTimer.toUpperCase().indexOf("Y")) {//年
				return  DateUtil.addYear(date, Integer.valueOf(timer));
			}else if(-1 != configTimer.toUpperCase().indexOf("D")){//日
				return  DateUtil.addDay(date, Integer.valueOf(timer));
			}else if(-1 != configTimer.toUpperCase().indexOf("H")){//时
				return  DateUtil.addHour(date, Integer.valueOf(timer));
			}else if(-1 != configTimer.toUpperCase().indexOf("MIN")){//分
				return  DateUtil.addMinute(date, Integer.valueOf(configTimer.substring(0, configTimer.length()-3)));
			}else if(-1 != configTimer.toUpperCase().indexOf("S")){//秒
				return  DateUtil.addSecond(date, Integer.valueOf(timer));
			}else if(-1 != configTimer.toUpperCase().indexOf("M")){//月
				return DateUtil.addMonth(date, Integer.valueOf(timer));
			}else{
				return null;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static Integer getTimeOfSecond(String configKey) throws Exception {
		try {
			String configTimer = PropertiesUtil.getContexrtParam(configKey);
			if (StringUtils.isBlank(configTimer)) {
				throw new IllegalArgumentException("The  configuration \"configTimer\" must be  not null,"
						+ "please check config files[job.properties]!");
			}
			String timer=configTimer.substring(0, configTimer.length()-1);
			if (StringUtils.isBlank(timer)) {
				throw new IllegalArgumentException("The  configuration \"timer\" must be not null,"
						+ "please check config files[job.properties]!");
			}
			if (-1 != configTimer.toUpperCase().indexOf("Y")) {//年
				return  Integer.valueOf(timer) * 60 * 60 * 24 * 365; 
			}else if(-1 != configTimer.toUpperCase().indexOf("MIN")){//分
				return  Integer.valueOf(configTimer.substring(0, configTimer.length()-3)) * 60;
			}else if(-1 != configTimer.toUpperCase().indexOf("D")){//日
				return  Integer.valueOf(timer) * 60 * 60 * 24 * 30;
			}else if(-1 != configTimer.toUpperCase().indexOf("H")){//时
				return  Integer.valueOf(timer) * 60 * 60;
			}else if(-1 != configTimer.toUpperCase().indexOf("S")){//秒
				return  Integer.valueOf(timer);
			}else if(-1 != configTimer.toUpperCase().indexOf("M")){//月
				return Integer.valueOf(timer) * 60 * 60 * 24 * 30;
			}else{
				throw new Exception("The  configuration is wrong!");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static Long getDataOfB(String configKey) throws Exception {
		try {
			String configTimer = PropertiesUtil.getContexrtParam(configKey);
			if (StringUtils.isBlank(configTimer)) {
				throw new IllegalArgumentException("The  configuration \"configTimer\" must be  not null,"
						+ "please check config files[job.properties]!");
			}
			Long paramDate = null;
			String timer=configTimer.substring(0, configTimer.length()-1);
			if (StringUtils.isBlank(timer)) {
				throw new IllegalArgumentException("The  configuration \"timer\" must be not null,"
						+ "please check config files[job.properties]!");
			}
			if (-1 != configTimer.toUpperCase().indexOf("G")) {
				paramDate =  Long.valueOf(timer) * 1024 * 1024 * 1024; 
			}else if(-1 != configTimer.toUpperCase().indexOf("M")){
				paramDate = Long.valueOf(timer) * 1024 * 1024; 
			}else if(-1 != configTimer.toUpperCase().indexOf("KB")){
				paramDate =  Long.valueOf(configTimer.substring(0, configTimer.length()-2)) * 1024; 
			}else if(-1 != configTimer.toUpperCase().indexOf("B")){
				paramDate =  Long.valueOf(timer); 
			}else{
				throw new Exception("The  configuration is wrong!");
			}
			return paramDate;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
}
