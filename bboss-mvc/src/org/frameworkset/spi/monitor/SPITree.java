/*
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
package org.frameworkset.spi.monitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.LinkConfigFile;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProviderManagerInfo;
import org.frameworkset.web.servlet.context.WebApplicationContext;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.util.StringUtil;

/**
 * 
 * 
 * <p>Title: SPITree.java</p>
 *
 * <p>Description: SPI监控树</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Sep 8, 2008 3:18:50 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class SPITree extends COMTree {
    
    static final List<String> rootfiles = BaseApplicationContext.getRootFiles();
    static boolean useSQLFile = true;
//	static final List traceFiles = ServiceProviderManager.getInstance().getTraceFiles();
	public boolean hasSon(ITreeNode father) {
		String id = father.getId(); 
		String type = father.getType();
		String path = father.getPath();
		
		if(father.isRoot())
		{
		    return useSQLFile  || (rootfiles != null && rootfiles.size() > 0);
//			return traceFiles != null && traceFiles.size() > 0;
		}
		else if(type.equals("applicationmodule"))
		{
			
		    BaseApplicationContext context = null;
		    context = BaseApplicationContext.getBaseApplicationContext(path);
//		    if(!path.equals(BaseApplicationContext.mvccontainer_identifier))
//		    	context = BaseApplicationContext.getBaseApplicationContext(path);
//		    else
//		    	context = BaseApplicationContext.getBaseApplicationContext(path,BaseApplicationContext.container_type_mvc);
		    if(context == null)
		        return false;
		    return context.getTraceFiles() != null && context.getTraceFiles().size() > 0; 
		}
		else if(type.equals("configfile"))
		{
		    String applicationid = path;
		    BaseApplicationContext context = BaseApplicationContext.getBaseApplicationContext(applicationid);
			LinkConfigFile lnk = context.getLinkConfigFile(id);
			boolean ret = lnk != null && (lnk.hasSubLinks() || lnk.hasPros() || lnk.hasMGRService());
//			if(ret)
//				return ret;
//			ret = lnk.hasMGRService();
			return ret;
		}
		
		else if(type.equals("sqlapplicationmodule"))
		{
		    BaseApplicationContext context = SQLUtil.getInstance(path).getSqlcontext();
		    if(context == null)
		        return false;
		    return context.getTraceFiles() != null && context.getTraceFiles().size() > 0; 
		}
		else if(type.equals("sqlconfigfile"))
		{
		    String applicationid = path;
		    BaseApplicationContext context = SQLUtil.getInstance(applicationid).getSqlcontext();
			LinkConfigFile lnk = context.getLinkConfigFile(id);
			boolean ret = lnk != null && (lnk.hasSubLinks() || lnk.hasPros() || lnk.hasMGRService());
//			if(ret)
//				return ret;
//			ret = lnk.hasMGRService();
			return ret;
		}
		else
		{
			return false;
		}
		
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String id = father.getId();
		String type = father.getType();
		String path = father.getPath();
		List traceFiles = null;
		BaseApplicationContext context = null;
		boolean isroot = father.isRoot(); 
		if(isroot)
		{
//		    traceFiles = ServiceProviderManager.getInstance().getTraceFiles();
		    traceFiles = rootfiles;
		}
		else if(type.equals("applicationmodule"))
		{
		    
		     context = BaseApplicationContext.getBaseApplicationContext(path);
            if(context == null)
                return true;
            traceFiles =  context.getTraceFiles(); 
		}
		else if(type.equals("configfile"))
		{
		    String applicationid = path;
		    context = BaseApplicationContext.getBaseApplicationContext(applicationid);
			LinkConfigFile lnk = context.getLinkConfigFile(id);
			if(lnk != null)
			{
				traceFiles = context.getLinkConfigFile(id).getLinkConfigFiles();
			}
		}
		else if(type.equals("sqlapplicationmodule"))
		{
			context = SQLUtil.getInstance(path).getSqlcontext();
//		     context = BaseApplicationContext.getBaseApplicationContext(path);
            if(context == null)
                return true;
            traceFiles =  context.getTraceFiles(); 
		}
		else if(type.equals("sqlconfigfile"))
		{
		    String applicationid = path;
//		    context = BaseApplicationContext.getBaseApplicationContext(applicationid);
		    context = SQLUtil.getInstance(applicationid).getSqlcontext();
			LinkConfigFile lnk = context.getLinkConfigFile(id);
			if(lnk != null)
			{
				traceFiles = context.getLinkConfigFile(id).getLinkConfigFiles();
			}
		}
		String foldertype = isroot?"applicationmodule":"configfile";
		if(isroot)
		{
		    for(int i = 0; rootfiles != null && i < rootfiles.size(); i ++ )
	        {
	            
	            {
	                Map params = new HashMap();
	                String uuid = rootfiles.get(i);
	                BaseApplicationContext context_root = BaseApplicationContext.getBaseApplicationContext(uuid);
	                if(context_root instanceof WebApplicationContext)
	                {
	                	uuid = BaseApplicationContext.mvccontainer_identifier;
	                	path = uuid;
	                }
	                else
	                {
	                	path = rootfiles.get(i);
	                }
	                uuid = "root:" + uuid;
//	                params.put("nodeLink",StringUtil.getRealPath(request , "/monitor/configfileDetail.jsp"));
	                this.addNode(father, uuid, 
	                        uuid, 
	                        foldertype, 
	                        false, 
	                        curLevel, 
	                        "", 
	                        (String)null, 
	                        (String)null,
	                        path,
	                        params);
	            }
	        }
		    List<String> sqlfiles = SQLUtil.getSQLFiles();
		    foldertype = isroot?"sqlapplicationmodule":"sqlconfigfile";
		    for(int i = 0; sqlfiles != null && i < sqlfiles.size(); i ++ )
	        {
	            
	            {
	                Map params = new HashMap();
	                String uuid = sqlfiles.get(i);
	                path = sqlfiles.get(i);
	                uuid = "sql:" + uuid;
	                params.put("nodeLink",StringUtil.getRealPath(request , "/monitor/spi/sqlconfigfileDetail.jsp"));
	                this.addNode(father, uuid, 
	                        uuid, 
	                        foldertype, 
	                        true, 
	                        curLevel, 
	                        "", 
	                        (String)null, 
	                        (String)null,
	                        path,
	                        params);
	            }
	        }
		    
		    return true;
		}
		if(type.equals("sqlapplicationmodule") || type.equals("sqlconfigfile"))
			
			foldertype = "sqlconfigfile";
		
			
		for(int i = 0; traceFiles != null && i < traceFiles.size(); i ++ )
		{
		    LinkConfigFile file = (LinkConfigFile)traceFiles.get(i);
		    
		    {
    			LinkConfigFile lnk = (LinkConfigFile)traceFiles.get(i);
    			
    			Map params = new HashMap();
    			
    			params.put("nodeLink",StringUtil.getRealPath(request , "/monitor/configfileDetail.jsp"));
    			this.addNode(father, lnk.getIdentity(), 
    					lnk.getConfigFile(), 
    					foldertype, 
    					true, 
    					curLevel, 
    					"", 
    					(String)null, 
    					(String)null,
    					path,
    					params);
		    }
		    
		}
		
		
		if(type.equals("configfile") || type.equals("sqlconfigfile"))
		{
			LinkConfigFile lnk = context.getLinkConfigFile(id);
			Map services = lnk.getMgrServices();
			Map params = new HashMap();
			params.put("nodeLink",StringUtil.getRealPath(request ,type.equals("sqlconfigfile")?"/monitor/spi/sqlconfigfileDetail.js": "/monitor/managerserviceDetail.jsp"));
			if(services != null && services.size() > 0)
			{
				Iterator its = services.values().iterator();
				while(its.hasNext())
				{
					ProviderManagerInfo spi = (ProviderManagerInfo)its.next();
					this.addNode(father, spi.getId(), 
							spi.getId(), 
							"spiservice", 
							true, 
							curLevel, 
							"", 
							(String)null, 
							(String)null,
							path,
							params);
				}
			}
			
			Map pros = lnk.getProperties();
			
			if(pros != null && pros.size() > 0)
			{
				Iterator its = pros.values().iterator();
				
				while(its.hasNext())
				{
					Pro pro = (Pro)its.next();
					String type_ = "property";
					Map pparams = new HashMap();
					pparams.put("nodeLink",StringUtil.getRealPath(request , "/monitor/spi/proDetail.jsp"));
					if(pro.isBean())
					{
						type_ = "bean";
						pparams.put("nodeLink",StringUtil.getRealPath(request , "/monitor/spi/beanDetail.jsp"));
					}
					else if(pro.isRefereced())
					{
						type_ = "ref";
						pparams.put("nodeLink",StringUtil.getRealPath(request , "/monitor/spi/refDetail.jsp"));
					}
					
					
					this.addNode(father, pro.getName() + "", 
					             pro.getName() + "", 
					             type_, 
							true, 
							curLevel, 
							"", 
							(String)null, 
							(String)null,
							path,
							pparams);
				}
			}
			
		}
		return true;
	}

}
