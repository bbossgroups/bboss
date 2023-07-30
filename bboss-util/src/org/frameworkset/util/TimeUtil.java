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

import com.frameworkset.util.SimpleStringUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.*;
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


    /**
     * 将字符串类型的日期转换为LocalDateTime类型数据
     *  Stream.of("2015-05-09T00:10:23.934596635Z",
     *                 "2015-05-09 00:10:23.123456789UTC",
     *                 "2015/05/09 00:10:23.123456789",
     *                 "2015-05-09 00:10:23.12345678",
     *                 "2015/05/09 00:10:23.1234567",
     *                 "2015-05-09T00:10:23.123456",
     *                 "2015-05-09 00:10:23.12345",
     *                 "2015/05-09T00:10:23.1234",
     *                 "2015-05-09 00:10:23.123",
     *                 "2015-05-09 00:10:23.12",
     *                 "2015-05-09 00:10:23.1",
     *                 "2015-05-09 00:10:23",
     *                 "2015-05-09 00:10",
     *                 "2015-05-09 01",
     *                 "2015-05-09"
     *         ).forEach(s -> {
     *             LocalDateTime date = LocalDateTime.parse(s, dateTimeFormatter);
     *             System.out.println(s + " localdate==> " + date);
     *
     *             System.out.println(s + " date==> " + par(s));
     *         });
     * @param localDateTime
     * @return
     */

    public static LocalDateTime localDateTime(String localDateTime){


        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter();
        LocalDateTime date = LocalDateTime.parse(localDateTime, dateTimeFormatter);
        return date;
    }
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }
    private static DateTimeFormatter dateTimeFormatter;
    private static DateTimeFormatter dateTimeFormatterDefault = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");
    private static Map<String,DateTimeFormatter> dateTimeFormatterMap = new LinkedHashMap<>();
    private static Object lock = new Object();
    public static DateTimeFormatter getDateTimeFormatter(){
        if(dateTimeFormatter != null){
            return dateTimeFormatter;
        }
        synchronized (lock) {
            if(dateTimeFormatter != null){
                return dateTimeFormatter;
            }
            DateTimeFormatter ISO_LOCAL_DATE = new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                    .optionalStart().appendLiteral('/').optionalEnd()
                    .optionalStart().appendLiteral('-').optionalEnd()
                    .optionalStart().appendValue(ChronoField.MONTH_OF_YEAR, 2)
                    .optionalStart().appendLiteral('/').optionalEnd()
                    .optionalStart().appendLiteral('-').optionalEnd()
                    .optionalStart().appendValue(ChronoField.DAY_OF_MONTH, 2)
                    .toFormatter();

            DateTimeFormatter ISO_LOCAL_TIME = new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.HOUR_OF_DAY, 2)
                    .optionalStart().appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                    .optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                    .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .optionalStart().appendZoneId()
                    .toFormatter();

            DateTimeFormatter dateTimeFormatter_ = new DateTimeFormatterBuilder()
                    .append(ISO_LOCAL_DATE)
                    .optionalStart().appendLiteral(' ').optionalEnd()
                    .optionalStart().appendLiteral('T').optionalEnd()
                    .optionalStart().appendOptional(ISO_LOCAL_TIME).optionalEnd()
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter(Locale.SIMPLIFIED_CHINESE);
            dateTimeFormatter = dateTimeFormatter_;
            return dateTimeFormatter;
        }
    }
    public static String changeLocalDateTime2String(LocalDateTime localDateTime){


//        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter();


        return localDateTime.format(dateTimeFormatterDefault);
    }
    public static String changeLocalDate2String(LocalDate localDateTime){


//        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter();


        return localDateTime.format(dateTimeFormatterDefault);
    }

    public static DateTimeFormatter getDateTimeFormatter(String dateFormat){
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterMap.get(dateFormat);
        if(dateTimeFormatter != null){
            return dateTimeFormatter;
        }
        synchronized (dateTimeFormatterMap){
            dateTimeFormatter = dateTimeFormatterMap.get(dateFormat);
            if(dateTimeFormatter != null){
                return dateTimeFormatter;
            }
            dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            dateTimeFormatterMap.put(dateFormat,dateTimeFormatter);
            return dateTimeFormatter;
        }
    }
    public static String changeLocalDateTime2String(LocalDateTime localDateTime,String dateFormat){


//        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter();

        if(SimpleStringUtil.isNotEmpty(dateFormat))
            return localDateTime.format(getDateTimeFormatter( dateFormat));
        else{
            return localDateTime.format(dateTimeFormatterDefault);
        }
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
