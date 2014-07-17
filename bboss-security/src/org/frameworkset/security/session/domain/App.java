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
	/**
	 * session共享区属性，多个属性以,号分隔
	 * 只要不包含在这个属性列表区的的属性都是应用特定的属性
	 */
	private String shareSessionAttrs;
	private transient List<String> _shareSessionAttrs;
	public App() {
		// TODO Auto-generated constructor stub
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
	
	

}
