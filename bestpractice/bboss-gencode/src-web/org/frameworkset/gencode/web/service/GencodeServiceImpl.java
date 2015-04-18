/**
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

package org.frameworkset.gencode.web.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.gencode.web.entity.Gencode;
import org.frameworkset.gencode.web.entity.GencodeCondition;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;

/**
 * <p>
 * Title: GencodeServiceImpl
 * </p>
 * <p>
 * Description: 代码生成管理业务处理类
 * </p>
 * <p>
 * bboss
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2015-04-18 20:44:21
 * @author yinbp
 * @version v1.0
 */
public class GencodeServiceImpl implements GencodeService {

	private static Logger log = Logger.getLogger(GencodeServiceImpl.class);

	private ConfigSQLExecutor executor;

	public void addGencode(Gencode gencode) throws GencodeException {
		// 业务组件
		try {
			executor.insertBean("addGencode", gencode);
		} catch (Throwable e) {
			throw new GencodeException("add Gencode failed:", e);
		}

	}

	public void deleteGencode(String id) throws GencodeException {
		try {
			executor.delete("deleteByKey", id);
		} catch (Throwable e) {
			throw new GencodeException("delete Gencode failed::id=" + id, e);
		}

	}

	public void deleteBatchGencode(String... ids) throws GencodeException {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			executor.deleteByKeys("deleteByKey", ids);
			tm.commit();
		} catch (Throwable e) {

			throw new GencodeException("batch delete Gencode failed::ids="
					+ ids, e);
		} finally {
			tm.release();
		}

	}

	public void updateGencode(Gencode gencode) throws GencodeException {
		try {
			executor.updateBean("updateGencode", gencode);
		} catch (Throwable e) {
			throw new GencodeException("update Gencode failed::", e);
		}

	}

	public Gencode getGencode(String id) throws GencodeException {
		try {
			Gencode bean = executor
					.queryObject(Gencode.class, "selectById", id);
			return bean;
		} catch (Throwable e) {
			throw new GencodeException("get Gencode failed::id=" + id, e);
		}

	}

	public ListInfo queryListInfoGencodes(GencodeCondition conditions,
			long offset, int pagesize) throws GencodeException {
		ListInfo datas = null;
		try {
			datas = executor.queryListInfoBean(Gencode.class,
					"queryListGencode", offset, pagesize, conditions);
		} catch (Exception e) {
			throw new GencodeException("pagine query Gencode failed:", e);
		}
		return datas;

	}

	public List<Gencode> queryListGencodes(GencodeCondition conditions)
			throws GencodeException {
		try {
			List<Gencode> beans = executor.queryListBean(Gencode.class,
					"queryListGencode", conditions);
			return beans;
		} catch (Exception e) {
			throw new GencodeException("query Gencode failed:", e);
		}

	}
}