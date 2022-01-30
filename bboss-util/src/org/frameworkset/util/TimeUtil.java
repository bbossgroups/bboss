/*
 *  Copyright 2008-2010 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.frameworkset.util;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: TimeUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-4-22 上午09:49:08
 * @author biaoping.yin
 * @version 1.0
 */
public class TimeUtil {
	public static boolean enablePersistentLocalDate = false;
	static{
		String _enablePersistentLocalDate = SystemPropertyUtils.getEnvironmentVariableValue("enablePersistentLocalDate");
		if(_enablePersistentLocalDate != null &&
				_enablePersistentLocalDate.equalsIgnoreCase("true")){
			enablePersistentLocalDate = true;
		}
	}

	public static TimeUnit getTimeUnitByName(String timeUnit,TimeUnit defaultTimeUnit)
	{
		TimeUnit timeUnit_ = TimeUnit.SECONDS;
		if (timeUnit.equals("TimeUnit.SECONDS")) {
			
			timeUnit_ = TimeUnit.SECONDS;
		} else if (timeUnit.equals("TimeUnit.DAYS")) {
			try
			{
				timeUnit_ = TimeUnit.valueOf("DAYS");
			}
			catch(Exception e)
			{
				timeUnit_ = defaultTimeUnit;
			}
		}
		if (timeUnit.equals("TimeUnit.HOURS")) {
			try
			{
				timeUnit_ = TimeUnit.valueOf("HOURS");
			}
			catch(Exception e)
			{
				timeUnit_ = defaultTimeUnit;
			}
//			timeUnit_ = TimeUnit.HOURS;
		} else if (timeUnit.equals("TimeUnit.MICROSECONDS")) {
			timeUnit_ = TimeUnit.MICROSECONDS;
		} else if (timeUnit.equals("TimeUnit.MILLISECONDS")) {
			timeUnit_ = TimeUnit.MILLISECONDS;
		} else if (timeUnit.equals("TimeUnit.MINUTES")) {
			try
			{
				timeUnit_ = TimeUnit.valueOf("MINUTES");
			}
			catch(Exception e)
			{
				timeUnit_ = defaultTimeUnit;
			}
		} else if (timeUnit.equals("TimeUnit.NANOSECONDS")) {
			timeUnit_ = TimeUnit.NANOSECONDS;
		}
		return timeUnit_;
	}

	public static Object convertLocalDatetime(LocalDateTime localDateTime){
		if (null == localDateTime) {
			return null;
		}
		if(enablePersistentLocalDate){
			return localDateTime;
		}
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		Instant instant = zonedDateTime.toInstant();
		Date date = Date.from(instant);
		return date;
	}

	public static Object convertLocalDate(LocalDate localDate){

		if (null == localDate) {
			return null;
		}
		if(enablePersistentLocalDate){
			return localDate;
		}
		ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
		return Date.from(zonedDateTime.toInstant());
	}
	public static Object convertLocalDate(Object localDate){

		if (null == localDate) {
			return null;
		}
		if(localDate instanceof LocalDateTime){
			return convertLocalDatetime((LocalDateTime)localDate);

		}
		else if(localDate instanceof LocalDate){
			return convertLocalDate((LocalDate)localDate);

		}
		return localDate;
	}

	public static TimeUnit getTimeUnit(String timeUnit,TimeUnit defaultUnit)
	{
		TimeUnit timeUnit_ = TimeUnit.SECONDS;
		
			try
			{
				timeUnit_ = TimeUnit.valueOf(timeUnit);
			}
			catch(Exception e)
			{
				timeUnit_ = defaultUnit;
			}
		
		return timeUnit_;
	}
	
	public static Date addDates(Date date,int days)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
		
	}
	
	public static Date addDateSeconds(Date date,int seconds)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
		
	}

	public static Date addDateMonths(Date date,int months)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();

	}

	public static Date addDateHours(Date date,int hours)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, hours);
		return c.getTime();

	}

	public static Date addDateMinitues(Date date,int Minitues)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, Minitues);
		return c.getTime();

	}

	public static Date addDateYears(Date date,int years)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		return c.getTime();

	}

}
