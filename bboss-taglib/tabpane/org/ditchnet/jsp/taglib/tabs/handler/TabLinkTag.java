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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseBodyTag;


/**
 *	@author Todd Ditchendorf
 *	@since 0.8
 *	
 *	JSP Tag that renders an XHTML </code>a</code> element that handles targeting
 *	a specific tab on the current page or another page.
 */
public final class TabLinkTag extends BaseBodyTag {
	
	static final String QUESTION_MARK = "?";
	static final String EQUALS		  = "=";
	static final String AMPERSAND	  = "&amp;";
	
	public static final String PARAM_NAME_TAB_PANE_ID 
		= "orgDitchnetTabPaneId";
	
	private String id,href,selectedTabPaneId;
	
	public void setId(final String id) {
		this.id = id;
	}
	
	public void setHref(final String href) {
		this.href = href;
	}
	
	public void setSelectedTabPaneId(final String selectedTabPaneId) {
		this.selectedTabPaneId = selectedTabPaneId;
	}
	
	private boolean hrefHasQueryString() {
		return href.indexOf( QUESTION_MARK ) > -1;
	}
	
	private String getUrlParamString() {
		String prefix;
		if (hrefHasQueryString()) {
			prefix = AMPERSAND;
		} else {
			prefix = QUESTION_MARK;
		}
		StringBuffer buff = new StringBuffer();
		buff.append(prefix).append(PARAM_NAME_TAB_PANE_ID).append(EQUALS)
			.append(selectedTabPaneId);
		return buff.toString();
	}

	public int doAfterBody() throws JspException {
				
//		StringWriter evalResult = new StringWriter();
//		StringBuffer buff = evalResult.getBuffer();
		StringBuffer buff = new StringBuffer();
		
		buff.append("\n<a ");
		if (isHrefSameAsRequestURI()) {
			buff.append("onclick=\"org.ditchnet.jsp.")
				.append("TabUtils.tabLinkClicked(event,'")
				.append(selectedTabPaneId)
				.append("',false); return false;\" href=\"")
				.append(getRequest().getRequestURL());
		} else {
			buff.append("href=\"").append(href).append(getUrlParamString());
		}
		if (null != id && 0 != id.length()) {
			buff.append(" id=\"").append(id).append("\"");
		}
		buff.append("\">");
		String  evalResult = this.bodyContent.getString();
//		bodyContent.clearBody();
		
//		getJspBody().invoke(evalResult);
		buff.append(evalResult);
		buff.append("</a>\n");
		
//		getJspContext().getOut().print(buff);
		try {
//			getPreviousOut().print(buff);
			bodyContent.getEnclosingWriter().print(buff);
//			out.print(buff);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
	
	private boolean isHrefSameAsRequestURI() {
		return null == href;
	}
	
	private HttpServletRequest getRequest() {
//		PageContext pageContext = (PageContext)getJspContext();
//		HttpServletRequest request = 
//			(HttpServletRequest)pageContext.getRequest();
		return this.getHttpServletRequest();
	}

	@Override
	public void doFinally() {
		id = null;href = null;selectedTabPaneId = null;
		if(bodyContent != null)
			bodyContent.clearBody();
		super.doFinally();
	}
	
}
