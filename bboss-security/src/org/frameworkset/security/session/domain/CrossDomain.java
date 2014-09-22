package org.frameworkset.security.session.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;

public class CrossDomain {
	private String rootDomain;
	/**
	 * 改为使用rootDomain属性
	 */
	@Deprecated 
	private String domain;
	
	private List<App> domainApps;
	/**
	 * session共享对应的cookie 路径名称，多个应用path以,号分隔
	 */
	private String path;
	
	private transient List<String> _paths;
	/**
	 * session共享区属性，多个属性以,号分隔
	 * 只要不包含在这个属性列表区的的属性都是应用特定的属性
	 */
	private String shareSessionAttrs;
	private transient List<String> _shareSessionAttrs;
	public CrossDomain() {
		// TODO Auto-generated constructor stub
	}
	
	
	public List<String> get_shareSessionAttrs() {
		return _shareSessionAttrs;
	}

	public void init()
	{
		
		if(SimpleStringUtil.isNotEmpty(path))
		{
			String[] temp = path.split(",");
			this._paths = Arrays.asList(temp);
		}
		if(SimpleStringUtil.isEmpty(rootDomain))
			this.rootDomain = this.domain;
		
		if(SimpleStringUtil.isNotEmpty(this.shareSessionAttrs))
		{
			String[] temp = shareSessionAttrs.split(",");
			this._shareSessionAttrs = Arrays.asList(temp);
			if(this.domainApps != null)
			{
				appsIdxs = new HashMap<String,App>();
				for(App app:domainApps)
				{
					
					if(SimpleStringUtil.isEmpty(app.getAttributeNamespace()))
					{
						if(StringUtil.isEmpty(app.getDomain()))
						{
							String ns = this.rootDomain.replace('.', '_') + "#";
							if(app.getPath().equals("/"))
							{
								ns = "ROOT_"+ns;
							}
							else if(app.getPath().startsWith("/"))
							{
								ns = app.getPath().substring(1)+"_"+ns;
							}
							else
							{
								ns = app.getPath()+"_"+ns;
							}
							app.setAttributeNamespace(ns);
						}
						else
						{
							String ns = app.getDomain().replace('.', '_') + "#";
							if(app.getPath().equals("/"))
							{
								ns = "ROOT_"+ns;
							}
							else if(app.getPath().startsWith("/"))
							{
								ns = app.getPath().substring(1)+"_"+ns;
							}
							else
							{
								ns = app.getPath()+"_"+ns;
							}
							app.setAttributeNamespace(ns);
						}
					}
					else
					{
						if(!app.getAttributeNamespace().endsWith("#"))
							app.setAttributeNamespace(app.getAttributeNamespace()+"#");
					}
					app.initUUID();
					appsIdxs.put(app.getUuid(), app);
					if(app.isCurrentApp())
						this.currentApp = app;
				}
			}
		}
		
	}
	public App getApp(String contextPath)
	{
		if(appsIdxs == null)
			return null;
		if(currentApp != null)
		{
			return currentApp;
		}
		return currentApp = this.appsIdxs.get(contextPath);
	}
	private App currentApp;
	private Map<String,App> appsIdxs ;
	public List<String> get_paths() {
		return _paths;
	}
	
	public boolean isShareAttribute(String attribute)
	{
		if(SimpleStringUtil.isEmpty(this.shareSessionAttrs))
			return true;
		return this.shareSessionAttrs.contains(attribute);
	}
	
	public String wraperAttributeName(String appkey,String contextpath, String attribute)
	{
		
		App app = this.getApp(contextpath);
		if(app == null)
			return attribute;
		if(isShareAttribute(attribute))
		{
			return attribute;
		}
		return app.getAttributeNamespace() + attribute;
	}
	
	/**
	 * 如果属性石共享属性或者是contextpath应用的属性则返回相应的属性，否则返回null
	 * @param appkey
	 * @param contextpath
	 * @param attribute
	 * @return
	 */
	public String dewraperAttributeName(String appkey,String contextpath, String attribute)
	{
		
		App app = this.getApp(contextpath);
		if(app == null)
			return attribute;
		if(isShareAttribute(attribute))
		{
			return attribute;
		}
		if(attribute.startsWith(app.getAttributeNamespace()))
			return attribute.substring(app.getAttributeNamespace().length());
		else
			return null;
	}
	public List<App> getDomainApps() {
		return domainApps;
	}
	public void setDomainApps(List<App> domainApps) {
		this.domainApps = domainApps;
	}


	public String getRootDomain() {
		return rootDomain;
	}


	public void setRootDomain(String rootDomain) {
		this.rootDomain = rootDomain;
	}


	public String getDomain() {
		return domain;
	}


	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	

}
