package com.caitu99.job.quarz.util;

/**
 * 星期一 ~ 星期日
 * @ClassName: Week 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author lys
 * @date 2015年3月5日 下午1:47:27
 */
public enum Week {

	/** 星期一 */
	MONDAY("星期一", "Monday", "Mon.", 1), 
	/** 星期二 */
	TUESDAY("星期二", "Tuesday", "Tues.", 2), 
	/** 星期三  */
	WEDNESDAY("星期三", "Wednesday", "Wed.", 3), 
	/** 星期四 */
	THURSDAY("星期四", "Thursday","Thur.", 4),
	/** 星期五 */
	FRIDAY("星期五", "Friday", "Fri.", 5), 
	/** 星期六 */
	SATURDAY("星期六","Saturday", "Sat.", 6), 
	/** 星期日 */
	SUNDAY("星期日", "Sunday", "Sun.", 7);

	private String name_cn;
	private String name_en;
	private String name_enShort;
	private int number;

	private Week(String name_cn, String name_en, String name_enShort, int number) {
		this.name_cn = name_cn;
		this.name_en = name_en;
		this.name_enShort = name_enShort;
		this.number = number;
	}

	/**
	 * 中文名获取
	 * @Title: getChineseName 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @return
	 * @date 2015年3月5日 下午1:48:13  
	 * @author lys
	 */
	public String getChineseName() {
		return name_cn;
	}

	public String getName() {
		return name_en;
	}

	/**
	 * 英文名获取
	 * @Title: getShortName 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @return
	 * @date 2015年3月5日 下午1:48:31  
	 * @author lys
	 */
	public String getShortName() {
		return name_enShort;
	}

	/**
	 * 编号获取
	 * @Title: getNumber 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @return
	 * @date 2015年3月5日 下午1:48:41  
	 * @author lys
	 */
	public int getNumber() {
		return number;
	}
}