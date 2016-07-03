package org.ditchnet.jsp.taglib.tabs.handler;

import java.io.OutputStream;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.pager.tags.PagerTag;

public class ParamTag extends BaseTag {
	private String name  = null;
	private String value = null;

	private String defaultValue = null;

    private boolean encode = true;
    public void setEncode(boolean encode)
  {
      this.encode = encode;
  }

  public boolean getEncode()
  {
      return this.encode;
  }


	/**
	 * request:parameter
	 * 		  :attribute
	 */

	private String type = null;

	public final void setName(String val) {
		name = val;
	}

	public final String getName() {
		return name;
	}

	public final void setValue(String val) {
		value = val;
	}

	public final String getValue() {
		return value;
	}

	public int doStartTag() throws JspException {
		super.doStartTag();
//		if(value != null ||
//		   type == null ||
//		   (!type.equals(PagerTag.ATTRIBUTE) && !type.equals(PagerTag.PARAMETER)))
//
//    			pagerTag.addParam(name, value,defaultValue,encode);
//
//		else
//		{
//			pagerTag.addParamByRequest(name,type,defaultValue,encode);
//		}

		return EVAL_BODY_INCLUDE;
	}

	public void release() {
		name = null;
		value = null;
		super.release();
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {
		// TODO Auto-generated method stub

	}
	/**
	 * Description:
	 * @return String

	 */
	public String getType() {
		return type;
	}

	/**
	 * Description:
	 * @return void

	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * Description:
	 * @return defaultValue

	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Description:defaultValue
	 * @return void

	 */
	public void setDefaultValue(String string) {
		defaultValue = string;
	}


}
