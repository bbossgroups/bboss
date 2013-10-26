package org.frameworkset.mvc;

import java.util.Date;


public class FileResource implements java.io.Serializable {
	private String uri;
	private long size;
	private String name;
	private boolean isDirectory;
	private boolean isTemplate;
	private String templateId;
//	private Template template;
	private Date modifyTime;
	
	
	private boolean isLock;
	private String checkoutUser;
	private String checkoutUserName;
	private Date checkoutTime;
	
	
	public Date getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(Date checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public String getCheckoutUser() {
		return checkoutUser;
	}

	public void setCheckoutUser(String checkoutUser) {
		this.checkoutUser = checkoutUser;
	}

//	public Template getTemplate() {
//		return template;
//	}
//
//	public void setTemplate(Template template) {
//		this.template = template;
//	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * 这个文件是不是模�?
	 */
	public boolean isTemplate() {
		return isTemplate;
	}

	
	/**
	 * 这个实例代表的是�?��文件还是�?��文件�?
	 */
	public boolean isDirectory() {
		return isDirectory;
	}
	
	/**
	 * 这个文件是否被加�?
	 */
	public boolean isLock() {
		return isLock;
	}
	
	/**
	 * 这个文件或文件夹的相对路�?
	 */
	public String getUri() {
		return uri;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 这个文件资源能不能成为模�?
	 * @return
	 */
	public boolean canbeTemplate(){
		if(isDirectory)
			return false;
		if(name==null || name.trim().length()==0)
			return false;
		String t = name.toLowerCase();
		if(t.endsWith(".html")||t.endsWith(".htm")){
			return true;
		}
		return false;
	}
	
	public boolean canEdit(){
		if(isDirectory)
			return false;
		if(name==null || name.trim().length()==0)
			return false;
		String t = name.toLowerCase();
		if(t.endsWith(".html")||t.endsWith(".htm")||t.endsWith(".xml")||t.endsWith(".css")){
			return true;
		}
		return false;
	}
	/**
	 * 文件资源的类�?每个类型在templateManage/image目录下有个相应的gif文件 
	 */
	public String getType(){
		if(isDirectory)
			return "folder";
		if(name==null || name.trim().length()==0)
			return "unknown";
		String t = name.toLowerCase();
		if(t.endsWith(".html")||t.endsWith(".htm")||t.endsWith(".xml")||t.endsWith(".css")){
			return "html";
		}
		if(t.endsWith(".gif")||t.endsWith(".jpg")||t.endsWith(".jpeg")){
			return "image";
		}
		if(t.endsWith(".css")||t.endsWith(".txt")){
			return "txtfile";
		}
		if(t.endsWith(".rar")||t.endsWith(".zip")){
			return "zip";
		}
		return "unknown";		
	}

	public String getCheckoutUserName() {
		return checkoutUserName;
	}

	public void setCheckoutUserName(String checkoutUserName) {
		this.checkoutUserName = checkoutUserName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
