package com.frameworkset.sqlparams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 红色预警处置工作统计传值对象
 * @author 郑巧伟
 *
 */
public class RedYjChuZhiTongJiDto {
	
	private int tjType;		//统计类型
	private String startDate;  //开始日期
	private String endDate;    //结束日期
	
	private String rxtsjStartDate; //入系统库开始时间
	private String rxtsjEndDate;  //入系统库结束时间
	private String yjfbsjStartDate;  //预警发布开始时间
	private String yjfbsjEndDate;   //预警发布结束时间
	
	private int year;  //年
	private int jiDu; //季度
	private int isDate; //是否是日期类型
	
	
	
	public String getRxtsjStartDate() {
		return rxtsjStartDate;
	}	
	private void setRxtsjStartDate(String rxtsjStartDate) {
		this.rxtsjStartDate = rxtsjStartDate;
	}
	
	public String getRxtsjEndDate() {
		return rxtsjEndDate;
	}	
	private void setRxtsjEndDate(String rxtsjEndDate) {
		this.rxtsjEndDate = rxtsjEndDate;
	}
	
	public String getYjfbsjStartDate() {
		return yjfbsjStartDate;
	}
	private void setYjfbsjStartDate(String yjfbsjStartDate) {
		this.yjfbsjStartDate = yjfbsjStartDate;
	}
	
	public String getYjfbsjEndDate() {
		return yjfbsjEndDate;
	}	
	private void setYjfbsjEndDate(String yjfbsjEndDate) {
		this.yjfbsjEndDate = yjfbsjEndDate;
	}
	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
		try {
			this.setRxtsjStartDate(convertStrDateFmtToAnother(startDate,"yyyy-MM-dd" , "yyyyMMdd"));
			this.setYjfbsjStartDate(convertStrDateFmtToAnother(startDate, "yyyy-MM-dd", "yyyy/MM/dd"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			startDate = null;
			this.rxtsjStartDate = null;
			this.yjfbsjStartDate = null;
			throw new RuntimeException("日期格式错误");
		}
		
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
		try {
			this.setRxtsjEndDate(convertStrDateFmtToAnother(endDate,"yyyy-MM-dd" , "yyyyMMdd"));
			this.setYjfbsjEndDate(convertStrDateFmtToAnother(endDate, "yyyy-MM-dd", "yyyyMMdd"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			endDate = null;
			this.rxtsjEndDate = null;
			this.yjfbsjEndDate = null;
			throw new RuntimeException("日期格式错误");
		}
	}
	
	public int getTjType() {
		return tjType;
	}
	public void setTjType(int tjType) {
		this.tjType = tjType;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getJiDu() {
		return jiDu;
	}
	public void setJiDu(int jiDu) {
		this.jiDu = jiDu;
	}
	public int getIsDate() {
		return isDate;
	}
	public void setIsDate(int isDate) {
		this.isDate = isDate;
	}
	
	/**
	 * 将日期字符串由一种日期格式转为另一种日期格式
	 * @param strDate
	 * @param originalFmt 原有日期格式
	 * @param fmt 要转换的日期格式
	 * @return
	 * @throws ParseException
	 */
	private String convertStrDateFmtToAnother(String strDate ,String originalFmt ,String fmt) throws ParseException{
		SimpleDateFormat formater = new SimpleDateFormat(originalFmt);
		Date d = formater.parse(strDate);
		formater = new SimpleDateFormat(fmt);
		return formater.format(d);
	}
	
	
}
