package test;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.pager.tags.CellTag;
import com.frameworkset.util.StringUtil;

public class TestCellTag extends CellTag{
	private String customAttribute;//自定义自己的标签属性
	public int doStartTag() throws JspException {
	    init();//出事化cell标签，必须要实现
	    Object actualValue =  super.getObjectValue();//获取到当前cell标签运算出来的值
	    
	    //接下来就可以做自己的事情了,我们只是输出自定义属性的值和cell对应的值到jsp页面上。
	    if(StringUtil.isNotEmpty(customAttribute))
	    {
	    	try {
				out.print("Custom Attribute value is "+this.customAttribute+",cell value is "+actualValue);
			} catch (IOException e) {
				throw new JspException(e);
			}
	    }
	    return SKIP_BODY;//返回控制标签事件生命周期的常量
	}
	/**
	 * 必须定义标签属性的get/set方法
	 */
	public String getCustomAttribute() {
		return customAttribute;
	}
	public void setCustomAttribute(String customAttribute) {
		this.customAttribute = customAttribute;
	}
	/**
	 * 必须在doFinally方法中释放属性的值
	 */
	@Override
	public void doFinally() {
		this.customAttribute = null;
		super.doFinally();
	}
	
}
