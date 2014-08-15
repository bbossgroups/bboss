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

package org.frameworkset.spi.assemble.callback;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.spi.assemble.AssembleUtil;
import org.frameworkset.spi.assemble.ManagerImport;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.PathMatcher;

import com.frameworkset.util.SimpleStringUtil;


/**
 * <p>Title: DefaultAssembleCallbackResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-3 下午11:04:59
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultAssembleCallbackResolver implements AssembleCallbackResolver{
	private Map<String,AssembleCallback> docAssembleCallbacks = new HashMap<String,AssembleCallback>();
	
	
	private final static PathMatcher pathMatcher = new AntPathMatcher();
	public AssembleCallback resolverAssembleCallback(String docbasetype) {
		AssembleCallback assembleCallback = docAssembleCallbacks.get(docbasetype);
		
		return assembleCallback != null ? assembleCallback:AssembleUtil.defaultAssembleCallback;
	}
	
	public void registAssembleCallback(AssembleCallback assembleCallback)
	{
		docAssembleCallbacks.put(assembleCallback.getDocbaseType(), assembleCallback);
	}
	
	/**
	 * docbase:
	 * classpath::conf,相对于.
	 * web::/WEB-INF/ 
	 * 
	 * @param docbaseImport
	 * @return
	 * @throws AssembleCallbackException
	 */
	public List<ManagerImport> getManagerImports(ManagerImport docbaseImport) throws AssembleCallbackException
	{
	
		String docbaseType = docbaseImport.isClasspathBase()?AssembleCallback.classpathprex:AssembleCallback.webprex;
		String docbase = docbaseImport.getDocbase();
		String contextFile = docbaseImport.getFile();
		AssembleCallback assembleCallback = this.resolverAssembleCallback(docbaseImport.isClasspathBase()?AssembleCallback.classpathprex:AssembleCallback.webprex);
		if(assembleCallback == null)
			throw new AssembleCallbackException("GetManagerImports("+docbaseType+","+docbase+","+contextFile+"):获取docbaseType[" + docbaseType + "]失败,AssembleCallback is null.");
		String absoluteParentPath = assembleCallback.getDocbasePath(docbase);
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~assembleCallback.getRootPath():" + assembleCallback.getRootPath() + "," + assembleCallback.getClass().getName());
		List<ManagerImport> managerImports = new ArrayList<ManagerImport>();
		
		if(contextFile.indexOf("*") == -1 && contextFile.indexOf("?") == -1)
		{
		
			ManagerImport managerimport = new ManagerImport();
			managerimport.setFile(contextFile);
			managerimport.setRealPath(SimpleStringUtil.getRealPath(absoluteParentPath, contextFile));
			managerimport.setWebbased(docbaseImport.isWebBase());
			managerImports.add(managerimport);
			return managerImports;
		}
		else
		{
			if(docbaseImport.isClasspathBase())
				throw new AssembleCallbackException("GetManagerImports("+docbaseType+
						","+docbase+","+contextFile+"):classpath上下文环境中的配置文件不能通过*,?进行批量配置导入.");
		}
		File parent = new File(absoluteParentPath);
		if(!parent.exists())
			throw new AssembleCallbackException("GetManagerImports("+docbaseType+","+docbase+","+contextFile+")失败："+absoluteParentPath + " do not exist.");
//		String docContextFilePattern = StringUtil.getRealPath(docbase, contextFile);
		String docContextFilePattern = contextFile;
		
		FileFilter fileFilter = docbaseImport.isWebBase()?
								new WebContextFileFilter(managerImports,absoluteParentPath,docContextFilePattern):
								new ClassPathContextFileFilter(managerImports,absoluteParentPath,docContextFilePattern);
		
		getSubFolderManagerImports( parent,fileFilter);
		return managerImports;
	}
	
	private void getSubFolderManagerImports(File parent,FileFilter fileFilter)
	{
		File[] files = parent.listFiles(fileFilter);
		for(int i = 0; files != null && i < files.length; i ++)
		{
			File file = files[i];
			getSubFolderManagerImports(file,fileFilter);
		}
		
	}
	public List<ManagerImport> getManagerImports(String docbaseType,String docbase,String contextFile) throws AssembleCallbackException
	{
		ManagerImport docbaseImport = new ManagerImport();
		
		if(docbase != null)
		{
			if(docbaseType == null)
			{
				docbaseImport.setDocbase(docbase);
			}
			else  if(docbase.startsWith(docbaseType))
				docbaseImport.setDocbase(docbase);
			else
			{
				
				docbaseImport.setDocbase(docbaseType + docbase);
			}
				
		}
		docbaseImport.setFile(contextFile);
		return getManagerImports(docbaseImport);
		
	}
	
	static class WebContextFileFilter implements java.io.FileFilter
	{
		private String absoluteParentPathPattern;
		private String[] contextFilePattern;
		private String absoluteParentPath;
		private String absoluteWEBINFPath;
		List<ManagerImport> managerImports;
		private String[] ignoreDirs = new String[]{".svn","classes"};
		private String[] includeDirs = new String[]{"WEB-INF"};
		private String[] includeFiles = new String[]{".xml"};
		WebContextFileFilter(List<ManagerImport> managerImports,String absoluteParentPath,String contextFilePattern)
		{
			this.absoluteParentPathPattern = absoluteParentPath.endsWith("/")?absoluteParentPath + "*":absoluteParentPath + "/*";
//			this.contextFilePattern = contextFilePattern;
			String[] patterns = contextFilePattern.split(",");
			this.contextFilePattern = new String[patterns.length];
			int i = 0;
			for(String pattern:patterns)
			{
				if(pattern.trim().startsWith("/"))
					this.contextFilePattern[i] = pattern.trim().substring(1);
				else
					this.contextFilePattern[i] = pattern.trim();
				i ++;
				
			}
			this.absoluteParentPath = absoluteParentPath;
			this.absoluteWEBINFPath = SimpleStringUtil.getRealPath(absoluteParentPath, "WEB-INF");
			this.managerImports = managerImports;
		}
		
		public boolean accept(File pathname) {
			if(contextFilePattern == null || contextFilePattern.length == 0)
				return false;
			String path = pathname.getAbsolutePath();			
			path = path.replace('\\', '/');
			if(pathname.isDirectory())
			{
			
				if(!path.startsWith(this.absoluteWEBINFPath))
					return false;
				if(SimpleStringUtil.containKey(ignoreDirs, pathname.getName()))
					return false;
				return true;
			}
			else
			{
				if(!pathname.getName().endsWith(".xml"))
				{
					return false;
				}
				
			}
			
			String subpath = pathMatcher.extractPathWithinPattern(absoluteParentPathPattern, path);
			
			for(String pattern:contextFilePattern)
			{
				if(pathMatcher.match(pattern, subpath))
				{
					ManagerImport managerimport = new ManagerImport();
					managerimport.setFile(subpath);
					managerimport.setRealPath(SimpleStringUtil.getRealPath(absoluteParentPath, subpath));
					managerimport.setWebbased(true);
					managerImports.add(managerimport);
					break;
				}
			}
			
			return false;
		}
		
		 
		
	}
	
	
	static class ClassPathContextFileFilter implements java.io.FileFilter
	{
		private String absoluteParentPathPattern;
		private String[] contextFilePattern;
		private String absoluteParentPath;
		private String absoluteWEBINFPath;
		List<ManagerImport> managerImports;
		private String[] ignoreDirs = new String[]{".svn","classes"};
		private String[] includeDirs = new String[]{"WEB-INF"};
		private String[] includeFiles = new String[]{".xml"};
		ClassPathContextFileFilter(List<ManagerImport> managerImports,String absoluteParentPath,String contextFilePattern)
		{
			this.absoluteParentPathPattern = absoluteParentPath.endsWith("/")?absoluteParentPath + "*":absoluteParentPath + "/*";
//			this.contextFilePattern = contextFilePattern;
			String[] patterns = contextFilePattern.split(",");
			this.contextFilePattern = new String[patterns.length];
			int i = 0;
			for(String pattern:patterns)
			{
				if(pattern.trim().startsWith("/"))
					this.contextFilePattern[i] = pattern.trim().substring(1);
				else
					this.contextFilePattern[i] = pattern.trim();
				i ++;
				
			}
			this.absoluteParentPath = absoluteParentPath;
			this.absoluteWEBINFPath = SimpleStringUtil.getRealPath(absoluteParentPath, "WEB-INF");
			this.managerImports = managerImports;
		}
		
		public boolean accept(File pathname) {
			if(contextFilePattern == null || contextFilePattern.length == 0)
				return false;
			String path = pathname.getAbsolutePath();			
			path = path.replace('\\', '/');
			if(pathname.isDirectory())
			{
			
				
				if(SimpleStringUtil.containKey(ignoreDirs, pathname.getName()))
					return false;
				return false;
			}
			else
			{
				if(!pathname.getName().endsWith(".xml"))
				{
					return false;
				}
				
			}
			
			String subpath = pathMatcher.extractPathWithinPattern(absoluteParentPathPattern, path);
			
			for(String pattern:contextFilePattern)
			{
				if(pathMatcher.match(pattern, subpath))
				{
					ManagerImport managerimport = new ManagerImport();
					managerimport.setFile(subpath);
					managerimport.setRealPath(SimpleStringUtil.getRealPath(absoluteParentPath, subpath));
					managerimport.setWebbased(true);
					managerImports.add(managerimport);
					break;
				}
			}
			
			return false;
		}
		
		 
		
	}

}
