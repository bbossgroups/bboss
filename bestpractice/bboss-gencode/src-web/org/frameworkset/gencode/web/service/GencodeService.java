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

import org.frameworkset.gencode.web.entity.Gencode;
import org.frameworkset.gencode.web.entity.GencodeCondition;

import com.frameworkset.util.ListInfo;

/**
 * <p>
 * Title: GencodeService
 * </p>
 * <p>
 * Description: 代码生成管理服务接口
 * </p>
 * <p>
 * bboss
 * </p>
 * <p>
 * Copyright (c) 2015
 * </p>
 * 
 * @Date 2015-04-18 20:44:21
 * @author yinbp
 * @version v1.0
 */
public interface GencodeService {
	public void addGencode(Gencode gencode) throws GencodeException;

	public void deleteGencode(String id) throws GencodeException;

	public void deleteBatchGencode(String... ids) throws GencodeException;

	public void updateGencode(Gencode gencode) throws GencodeException;

	public Gencode getGencode(String id) throws GencodeException;

	public ListInfo queryListInfoGencodes(GencodeCondition conditions,
			long offset, int pagesize) throws GencodeException;

	public List<Gencode> queryListGencodes(GencodeCondition conditions)
			throws GencodeException;

	
}