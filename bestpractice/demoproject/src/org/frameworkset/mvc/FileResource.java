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
	 * 杩欎釜鏂囦欢鏄笉鏄ā鏉?
	 */
	public boolean isTemplate() {
		return isTemplate;
	}

	
	/**
	 * 杩欎釜瀹炰緥浠ｈ〃鐨勬槸涓?釜鏂囦欢杩樻槸涓?釜鏂囦欢澶?
	 */
	public boolean isDirectory() {
		return isDirectory;
	}
	
	/**
	 * 杩欎釜鏂囦欢鏄惁琚姞閿?
	 */
	public boolean isLock() {
		return isLock;
	}
	
	/**
	 * 杩欎釜鏂囦欢鎴栨枃浠跺す鐨勭浉瀵硅矾寰?
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
	 * 杩欎釜鏂囦欢璧勬簮鑳戒笉鑳芥垚涓烘ā鏉?
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
	 * 鏂囦欢璧勬簮鐨勭被鍨?姣忎釜绫诲瀷鍦╰emplateManage/image鐩綍涓嬫湁涓浉搴旂殑gif鏂囦欢 
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
