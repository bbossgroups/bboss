package org.frameworkset.security.session.domain;

import java.util.Arrays;
import java.util.List;

import com.frameworkset.util.SimpleStringUtil;

public class App {
	
	/**
	 * session共享对应的cookie 路径名称，多个应用path以,号分隔
	 */
	private String path;
	private String attributeNamespace;
	private String uuid;
	private String domain;
	private boolean currentApp ;
	/**
	 * session共享区属性，多个属性以,号分隔
	 * 只要不包含在这个属性列表区的的属性都是应用特定的属性
	 */
	private String shareSessionAttrs;
	private transient List<String> _shareSessionAttrs;
	public App() {
		// TODO Auto-generated constructor stub
	}
	public void initUUID()
	{
		if(this.domain == null || this.domain.equals(""))
		{
			if(this.attributeNamespace == null || this.attributeNamespace.equals(""))
			{
				this.uuid = path;
			}
			else
			{
				this.uuid = this.attributeNamespace;
			}
		}
		else
		{
			if(this.path.equals("") || path.equals("/"))
				this.uuid = this.domain;
			else
			{
				if(this.domain.endsWith("/") && this.path.startsWith("/"))
					this.uuid = this.domain + this.path.substring(1);
				else if(this.domain.endsWith("/") && !this.path.startsWith("/"))
					this.uuid = this.domain + this.path;
				else if(!this.domain.endsWith("/") && this.path.startsWith("/"))
					this.uuid = this.domain + this.path;
				else
					this.uuid = this.domain + "/" + this.path;
			}
		}
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getShareSessionAttrs() {
		return shareSessionAttrs;
	}
	public void setShareSessionAttrs(String shareSessionAttrs) {
		this.shareSessionAttrs = shareSessionAttrs;
	}
	
	public void init()
	{
		
		
		if(SimpleStringUtil.isNotEmpty(this.shareSessionAttrs))
		{
			String[] temp = shareSessionAttrs.split(",");
			this._shareSessionAttrs = Arrays.asList(temp);
		}
	}
	
	public List<String> get_shareSessionAttrs() {
		return _shareSessionAttrs;
	}

	public String getAttributeNamespace() {
		return attributeNamespace;
	}

	public void setAttributeNamespace(String attributeNamespace) {
		this.attributeNamespace = attributeNamespace;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	public boolean isCurrentApp() {
		return currentApp;
	}
	public void setCurrentApp(boolean currentApp) {
		this.currentApp = currentApp;
	}
	public String getUuid() {
		return uuid;
	}
	
	

}
