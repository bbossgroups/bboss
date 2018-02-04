package com.frameworkset.common.poolman.sql;/*
 *  Copyright 2008 biaoping.yin
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

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.annotation.PrimaryKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseIdGenerator implements IdGenerator{
	private KeyValue dummyKey = new KeyValue(0);
	private ReentrantLock lock = new ReentrantLock();
	private Object genDummyKey(Class type){


			Object value = null;
			long _value = dummyKey.increamentAndGet();
			if (type == int.class)
				value = (int) _value;
			else if (type == Integer.class)
				value = new Integer((int) _value);
			else if (type == Long.class)
				value = new Long(_value);
			else
				value = _value;
			return value;

	}


	@Override
	/**
	 * 对于Interger等数字封装类型，或者String类型，oldValue不为空时，无需再次设置主键
	 */
	public Object getNextId(PrimaryKey pka,String dbName, Class type,String proName) {
		Object value = null;
		if(type == String.class){//正式环境可用
				return this.getNextId();

		}

		else if(type == Long.class || type == Integer.class||type == int.class || type == long.class){//数字主键

			if(pka.tableName() == null)
				return genDummyKey(type);
			else
				return genDummyKey(type,pka,dbName,  proName);


		}
		else{//为测试而实现的内容，不能用于生产环境
			return this.getNextId();
		}

	}
	private Map<String,KeyValue> currentValue = new HashMap<String,KeyValue>();

	private Long _get(PrimaryKey pka,String dbname,String proName){
		try {
			PreparedDBUtil dbUtil = new PreparedDBUtil();
			StringBuilder sql = new StringBuilder();
			if(pka.id() == null || pka.id().equals("")) {
				sql.append("select max(").append(proName)
						.append(")+1 as pkvalue from ")
						.append(pka.tableName())
				;
			}
			else
			{
				sql.append("select max(").append(pka.id())
						.append(")+1 as pkvalue from ")
						.append(pka.tableName())
				;
			}
			dbUtil.preparedSelect(dbname, sql.toString());
			Long currentPk = dbUtil.executePreparedForObject(Long.class);
			if(currentPk == null)
				currentPk = new Long(0);
			return currentPk;
		}
		catch (Exception e){
			throw new java.lang.RuntimeException(e);
		}
	}
	static class KeyValue{
		private ReentrantLock lock = new ReentrantLock();
		long value = 0;
		public KeyValue(long value){
			this.value = value;
		}
		public long increamentAndGet(){
			try {
				lock.lock();
				value++;
				return value;
			}
			finally {
				lock.unlock();
			}
		}
	}
	private Object genDummyKey(Class type,PrimaryKey pka,String dbname,String proName){
		KeyValue current = currentValue.get(pka.tableName());
		Long _value = null;
		if(current == null) {
			try {
				lock.lock();
				current = currentValue.get(pka.tableName());
				if (current == null) {
					current = new KeyValue(_get(pka, dbname, proName));
					currentValue.put(pka.tableName(), current);

				}
			} finally {
				lock.unlock();
			}
		}


		_value = current.increamentAndGet();

		Object value = null;

		if (type == int.class)
			value =  _value.intValue();
		else if (type == Integer.class)
			value = new Integer( _value.intValue());
		else if (type == Long.class)
			value = _value;
		else
			value = _value;
		return value;

	}


}
