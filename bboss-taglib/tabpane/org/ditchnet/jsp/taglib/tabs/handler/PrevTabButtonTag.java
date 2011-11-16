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

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseBodyTag;


/**
 *	@author Todd Ditchendorf
 *	@since 0.8
 *	
 *	JSP Tag that renders an XHTML </code>input</code> element of type 
 *	</code>button</code> with a JavaScript handler to switch the focus of
 *	a specified tab container to the previous tab index.
 */
public final class PrevTabButtonTag extends BaseBodyTag {
	
	private String id;
	private String tabContainerId;
	
	public void setId(final String id) {
		this.id = id;
	}
	
	public void setTabContainerId(final String tabContainerId) {
		this.tabContainerId = tabContainerId;
	}
	
	public int doAfterBody() throws JspException {
		
		//StringWriter evalResult = new StringWriter();
//		StringBuffer buff		= evalResult.getBuffer();
		StringBuffer buff		= new StringBuffer();
		
		buff.append("\n\t<input type=\"button\" ")
			.append("class=\"ditch-prev-tab-button\" ")
			.append("onclick=\"org.ditchnet.jsp.")
			.append("TabUtils.prevTabButtonClicked(event,'")
			.append(tabContainerId).append("',false); return false;\"");
		if (null != id && 0 != id.length()) {
			buff.append(" id=\"").append(id).append("\"");
		}
		buff.append(" value=\"");
		//getJspBody().invoke(evalResult);
		String evalResult = this.bodyContent.getString();
		buff.append(evalResult);
		buff.append("\" />\n");
		
//		getJspContext().getOut().print(buff);
		try {
//			this.getPreviousOut().print(buff);
			bodyContent.getEnclosingWriter().print(buff);
//			out.print(buff);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bodyContent.clearBody();
		return SKIP_BODY;
	}
	
}
