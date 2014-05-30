package com.frameworkset.common.tag.pager.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * 用来输出Collection,Map,ListInfo,String,Array的长度
 * @author yinbp
 *
 */
public class SizeTag extends BaseValueTag{
	private int increament = 0;
	@Override
	public int doStartTag() throws JspException {
		 init();
//		    dataSet = searchDataSet(this,PagerDataSet.class);
//		    t_formula = dataSet.getFormula(getExpression());
		 Object obj = this.evaluateActualValue();
		 int len = this.length(obj);
		 try {
			out.print(len + increament);
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		return Tag.SKIP_BODY;
	}
	public int getIncreament() {
		return increament;
	}
	public void setIncreament(int increament) {
		this.increament = increament;
	}

}
