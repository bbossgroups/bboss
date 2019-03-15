package org.frameworkset.persitent.util;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.LinkConfigFile;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProviderParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/3/15 10:48
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLProviderParser extends ProviderParser {
	private static Logger logger = LoggerFactory.getLogger(SQLProviderParser.class);
	public SQLProviderParser(BaseApplicationContext applicationContext, String file, LinkConfigFile linkfile) {
		super(applicationContext, file, linkfile);
	}

	public SQLProviderParser(BaseApplicationContext applicationContext) {
		super(applicationContext);
	}

//	@Override
//	public Pro _getRealProperty(String name) {
//		return super._getRealProperty(name);
//	}
	@Override
	public Object getRealPropertyValue(Pro pro){
		String sqlfile = (String)pro.getExtendAttribute("sqlfile");
		if(sqlfile == null)
			return pro.getValue();
		else{
			String sqlname = (String)pro.getExtendAttribute("sqlname");
			if(sqlname == null)
			{
				logger.warn(new StringBuilder().append("The sql  ")
						.append(pro.getName()).append(" in the sql file ")
						.append(applicationContext.getConfigfile())
						.append(" is defined as a reference to the sql in another configuration file ")
						.append(sqlfile)
						.append(", but the name of the sql  statement to be referenced is not specified by the sqlname attribute, for example:\r\n")
						.append("<property name= \"querySqlTraces\"\r\n")
						.append("sqlfile= \"mapper/sqlTracesMapper.xml\"\r\n")
						.append("sqlname= \"queryTracesByCriteria\"/>").toString());
				return null;
			}
			else
			{
				SQLUtil.SQLRef ref = new SQLUtil.SQLRef(sqlname,sqlfile,pro.getName());
				return ref.getSQL();

			}
		}
	}
}
