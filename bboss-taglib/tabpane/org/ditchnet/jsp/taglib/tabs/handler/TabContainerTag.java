/*
 * The contents of this file are subject to the GNU Lesser General Public
 * License Version 2.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/lesser.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Developer:
 * Todd Ditchendorf, todd@ditchnet.org
 *
 */

/**
 *	@author Todd Ditchendorf
 *	@version 0.8
 *	@since 0.8
 */
package org.ditchnet.jsp.taglib.tabs.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.ditchnet.jsp.util.JspResponseWriter;
import org.ditchnet.xml.Xhtml;

import com.frameworkset.common.ecs.Script;
import com.frameworkset.common.tag.BaseBodyTag;


/**
 *	@author Todd Ditchendorf
 *	@since 0.8
 *	
 *	JSP Tag that renders a collection of tabs.
 */
public final class TabContainerTag extends BaseBodyTag {
	
	public static final String COOKIE_PREFIX = "org.ditchnet.jsp.tabs";

	private String id;
	private String skin;
	private List children;
	private String selectedTabPaneId;
	private String urlSelectedTabPaneId;
//	private int selectedIndex = -1;
	private String selectedIndex = null;
//	private int cookieSelectedIndex = -1;
	private String cookieSelectedIndex ;
	private String jsTabListener;
	JspResponseWriter out ;
	
	private boolean enablecookie = false;
	
	
	public void setId(final String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setSkin(final String skin) {
		this.skin = skin.toLowerCase();
	}
	
	public String getSkin() {
		if (null == skin || 0 == skin.length()) {
			skin = "default";
		}
		return skin;
	}
	
	public void setSelectedTabPaneId(final String selectedTabPaneId) {
		this.selectedTabPaneId = selectedTabPaneId;
	}
	
	public String getSelectedTabPaneId() {
		return selectedTabPaneId;
	}
	
	void setSelectedIndex(final String selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	String getSelectedIndex() {
		return selectedIndex;
	}
	
	private void setCookieSelectedIndex(final String selectedIndex) {
		this.cookieSelectedIndex = selectedIndex;
	}
	
	private String getCookieSelectedIndex() {
		return cookieSelectedIndex;
	}
	
	private void setUrlSelectedTabPaneId(final String selectedId) {
		this.urlSelectedTabPaneId = selectedId;
	}
	
	private String getUrlSelectedTabPaneId() {
		return urlSelectedTabPaneId;
	}
	
	public void setJsTabListener(final String jsTabListener) {
		this.jsTabListener = jsTabListener;
	}
	
	public String getJsTabListener() {
		return jsTabListener;
	}
	
	List getChildren() {
		if (null == children) {
			children = new ArrayList();
		}
		return children;
	}
	
	int getChildCount() {
		return getChildren().size();
	}
	
	void addChild(final TabPane child) {
		getChildren().add(child);
	}
	
	public int doEndTag()throws JspException
	{
		
		
		
		return super.doEndTag();
	}
	@Override
	public void doFinally() {
		children = null;
		selectedTabPaneId = null;
		urlSelectedTabPaneId = null;
		selectedIndex = null;
		cookieSelectedIndex = null;
		jsTabListener = null;
		this.enablecookie = false;
//		this.accesscontrol = null;
//		this.request = null;
//		this.response = null;
//		this.out = null;
		try {
			if(out != null)
			{
				out.close();
				this.out = null;
			}
		} catch (Exception e) {
			
		}
		if(bodyContent != null)
			bodyContent.clearBody();
		super.doFinally();
	}
//	protected AccessControl accesscontrol;
	public void setPageContext(PageContext pageContext) 
	{
//		System.out.println("setPageContext(PageContext pageContext)");
		super.setPageContext(pageContext);
//		accesscontrol = AccessControl.getInstance();
//		
//		accesscontrol.checkAccess(super.request,super.response,false);
		
	}
	public int doStartTag()  throws JspException
	{
		//response.setHeader("Pragma", "no-cache"); 
		int ret = super.doStartTag();
		this.children = null;
		
		//consumeCookie();
		return ret;
	}
	private boolean existSelectIndex()
	{
		/**
		 * 容器已经处理空的情况
		 */
		if(selectedIndex == null || selectedIndex.equals(""))
			return false;
		for(int i =0 ; i < this.children.size(); i ++)
		{
			TabPane pane = (TabPane)this.children.get(i);
			
			if(pane.getId().equals(this.selectedIndex))
				return true;
		}
		return false;
	}
	public int doAfterBody() throws JspException{
		if(children == null || children.size() == 0)
		{
			return SKIP_BODY;
		}
//		try {
//			getPreviousOut().clearBuffer();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		consumeCookie();
		//consumeQueryStringParam();
				
//		StringWriter discardResult = new StringWriter();
//		StringWriter evalResult    = new StringWriter();
		out = new JspResponseWriter();
		out.startElement(Xhtml.Tag.DIV);
		out.attribute(Xhtml.Attr.CLASS,TabConstants.TAB_SKIN_CLASS_NAME+getSkin());
		out.startElement(Xhtml.Tag.DIV);
		out.attribute(Xhtml.Attr.ID,id);
//		out.attribute(Xhtml.Attr.ENABLECOOKIE,this.enablecookie + "");
		out.attribute(Xhtml.Attr.CLASS,TabConstants.TAB_CONTAINER_CLASS_NAME);
		out.startElement(Xhtml.Tag.DIV);
		out.attribute(Xhtml.Attr.CLASS,TabConstants.TAB_WRAP_CLASS_NAME);

		//getJspBody().invoke(evalResult);
		String evalResult = bodyContent.getString();
		
		bodyContent.clearBody();
		
//		evalResult.getBuffer().delete(0,evalResult.getBuffer().length());
		//determineSelectedIndex();
//		children = null;
//		getJspBody().invoke(evalResult);

		int i = 0;
		
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			TabPane tabPane = (TabPane)iter.next();
			out.startElement(Xhtml.Tag.DIV);
			out.attribute(Xhtml.Attr.ID,tabPane.getId()+TabConstants.TAB_ID_SUFFIX);
			out.attribute(Xhtml.Attr.NAME,tabPane.getId()+TabConstants.TAB_ID_SUFFIX);
			if (null != getJsTabListener() && getJsTabListener().length() > 0) {
				out.attribute(Xhtml.Attr.ONCLICK,
							  "org.ditchnet.jsp.TabUtils.tabClicked(event," + this.enablecookie +");" +
				getJsTabListener().trim() + "(new org.ditchnet.jsp.TabEvent(this));");
			} else {
				out.attribute(Xhtml.Attr.ONCLICK,
							  "org.ditchnet.jsp.TabUtils.tabClicked(event," + this.enablecookie +");");
			}
			if (tabPane.getId().equals(getSelectedIndex())) {
				out.attribute(Xhtml.Attr.CLASS,TabConstants.TAB_CLASS_NAME + " " +
											   TabConstants.FOCUSED_CLASS_NAME);
			} else {
				out.attribute(Xhtml.Attr.CLASS,TabConstants.TAB_CLASS_NAME + " " + 
											   TabConstants.UNFOCUSED_CLASS_NAME);
			}
			out.startElement(Xhtml.Tag.SPAN);
			out.attribute(Xhtml.Attr.CLASS,TabConstants.TAB_BG_LEFT_CLASS_NAME);
			out.text(" ");
			out.endElement(Xhtml.Tag.SPAN);
			out.startElement(Xhtml.Tag.A);
			out.attribute(Xhtml.Attr.ID,tabPane.getId()+TabConstants.TAB_A_ID_SUFFIX);
			out.attribute(Xhtml.Attr.HREF,getTabUrl(tabPane) +
										  TabLinkTag.QUESTION_MARK +
										  TabLinkTag.PARAM_NAME_TAB_PANE_ID +
										  TabLinkTag.EQUALS +
										  tabPane.getId());
			if (null != getJsTabListener() && getJsTabListener().length() > 0) {
				out.attribute(Xhtml.Attr.ONCLICK,				
				getJsTabListener().trim() + ";return false;");
			} else {
				out.attribute(Xhtml.Attr.ONCLICK,"return false;");
			}
//			out.attribute(Xhtml.Attr.ONCLICK,"return false;");
			
			if (null != tabPane.getTabTitle() &&
				0 < tabPane.getTabTitle().length()) {
				out.text(tabPane.getTabTitle());
			}
			out.text(" ");
			out.endElement(Xhtml.Tag.A);
			out.endElement(Xhtml.Tag.DIV);
			i++;
		}
		out.startElement(Xhtml.Tag.BR);
		out.attribute(Xhtml.Attr.CLASS,TabConstants.CLEAR_CLASS_NAME);
		out.endElement(Xhtml.Tag.BR);
		out.endElement(Xhtml.Tag.DIV);
		out.comment(TabConstants.TAB_WRAP_CLASS_NAME);
		out.startElement(Xhtml.Tag.DIV);
		out.attribute(Xhtml.Attr.CLASS,TabConstants.TAB_PANE_WRAP_CLASS_NAME);
//		out.text(evalResult.getBuffer().toString());
		out.text(evalResult);
		out.endElement(Xhtml.Tag.DIV);
		out.comment(TabConstants.TAB_PANE_WRAP_CLASS_NAME);
		out.endElement(Xhtml.Tag.DIV);
		out.comment(TabConstants.TAB_CONTAINER_CLASS_NAME);
		out.endElement(Xhtml.Tag.DIV);
		out.comment(TabConstants.TAB_SKIN_CLASS_NAME+getSkin());
		if(!existSelectIndex())
		{
			TabPane pane = (TabPane)this.children.get(0);
			this.selectedIndex = pane.getId();
			
			
			 Script scr = new  Script();
			
			scr.setLanguage("javascript");
			scr.setTagText("org.ditchnet.jsp.TabUtils.doclickevt(document.getElementById('" + pane.getId() +TabConstants.TAB_ID_SUFFIX + "'));");
//			scr.setTagText("document.getElementById('" + pane.getId() +TabConstants.TAB_ID_SUFFIX + "').click();");			
			out.text(scr.toString());
			
		}
		
		
		try {
//			this.getPreviousOut().print(out.getBuffer());
//			bodyContent.getEnclosingWriter().print(out.getBuffer());
			String ret = out.getBuffer().toString();
			out = null;
			bodyContent.getEnclosingWriter().print(ret);
//			JspWriter out_ = this.getJspWriter();
//			out_.print(ret);
//			out_.flush();
			
		} catch (IOException e) {			
		}
		
		return SKIP_BODY;
	}
	

	void determineSelectedIndex() {
		//check url first
		TabPane child;
		for (int i = 0; i < getChildCount(); i++) {
			child = (TabPane)getChildren().get(i);
			
			if (child.getId().equals(getUrlSelectedTabPaneId())) {
//				setSelectedIndex(child.getId());
				setSelectedIndex(child.getId());
				return;
			}
		}
		//then check cookie
		if(this.enablecookie)
		{
			if (getCookieSelectedIndex() != null && !getCookieSelectedIndex().equals("")) {
				setSelectedIndex(cookieSelectedIndex);
				return; 
			}
		}
		//then check jsp tag attr
//		for (int i = 0; i < getChildCount(); i++) {
//			child = (TabPane)getChildren().get(i);
//			if (child.getId().equals(getSelectedTabPaneId())) {
			if(this.getSelectedTabPaneId() != null && !getSelectedTabPaneId().equals(""))
				setSelectedIndex(getSelectedTabPaneId());
			else
			{
				this.setSelectedIndex(((TabPane)getChildren().get(0)).getId());
			}
				return;
//			}
//		}
	}
	
	private String getTabUrl(final TabPane tabPane) {
		HttpServletRequest request = this.getHttpServletRequest();
//        HttpSession session = request.getSession(false);
		return request.getRequestURL().toString();
	}
	
	void consumeQueryStringParam() {
		HttpServletRequest request = this.getHttpServletRequest();
//        HttpSession session = request.getSession(false);
		String tabPaneIdParamValue = request.getParameter( 
										TabLinkTag.PARAM_NAME_TAB_PANE_ID );
		if (null == tabPaneIdParamValue 
			|| 0 == tabPaneIdParamValue.length()) {
			return;
		}
		setUrlSelectedTabPaneId( tabPaneIdParamValue );
	}
	
	void consumeCookie() {
		Cookie[] cookies = getPageCookies();
		Cookie cookie;
		String prefix,value;
		for (int i = 0; i < cookies.length; i++) {
			cookie = cookies[i];
			if (isDitchnetTabCookie(cookie)) {
				int index = cookie.getName().indexOf(":")+1;
				if (isCookieForThisContainer(cookie,index)) {
					try {
//						setCookieSelectedIndex(
//								Integer.parseInt(cookie.getValue()));
						setCookieSelectedIndex(
								cookie.getValue());
					} catch (NumberFormatException e) { }
				}
			}
		}
	}
	
	
	
	
	
	private Cookie[] getPageCookies() {
		HttpServletRequest request = this.getHttpServletRequest();
//        HttpSession session = request.getSession(false);
		Cookie[] cookies = request.getCookies();
		if (null == cookies) {
			cookies = new Cookie[0];
		}
		return cookies;
	}
	
	private boolean isDitchnetTabCookie(final Cookie cookie) {
		return 0 == cookie.getName().indexOf(COOKIE_PREFIX)	;
	}
	
	private boolean isCookieForThisContainer(final Cookie cookie,
											 final int index) {
		return cookie.getName().substring(index).equals(getId());
	}

	public boolean isEnablecookie() {
		return enablecookie;
	}

	public void setEnablecookie(boolean enablecookie) {
		this.enablecookie = enablecookie;
	}
	
}
