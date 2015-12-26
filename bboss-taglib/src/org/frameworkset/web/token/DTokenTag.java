package org.frameworkset.web.token;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseTag;
/**
 * 动态令牌标签
 * @author yinbp
 *
 */
public class DTokenTag extends BaseTag {
	 
	
	/**
	 * 参数有3个值（如果没有指定默认为input类型）：
	 * json 输出json表达式，名称和值的分隔符默认为'号，如果要指定为"号，那么需要
	 * 通过jsonsplit参数来指定
	 * 
	 * input输出input hidden
	 * 
	 * param(等号表达式)
	 * 
	 * 
	 * 
	 */
	private String element = "input";
	private String jsonsplit ="'";
	/**
	 * 令牌所属窗体唯一标识，一个窗体只需要一个令牌，如果在同一个窗体
	 * 中dttoken标签需要出现多次，比如在逻辑判断中出现多次，那么就需要指定
	 * fid属性，除此以外都不需要指定fid属性
	 * 
	 */
	private String fid ;
	private boolean cache = true;
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getJsonsplit() {
		return jsonsplit;
	}
	public void setJsonsplit(String jsonsplit) {
		this.jsonsplit = jsonsplit;
	}
	@Override
	public void doFinally() {
		
		super.doFinally();
		element = "input";
		jsonsplit ="'";
		this.cache = true;
		this.fid = null;
	}
	@Override
	public int doStartTag() throws JspException {
		
		int ret = super.doStartTag();
		
		if(TokenMethodHelper.isEnableToken != null)
		{
			try {
				Boolean enableToken = (Boolean)TokenMethodHelper.isEnableToken.invoke(null);
				if(!enableToken.booleanValue())
					return ret;
			} catch (RuntimeException e) {
				throw new JspException(e);
			} 
			catch (Exception e) {
				throw new JspException(e);
			} catch (Throwable e) {
				throw new JspException(e);
			}
			try {
				out.print(TokenMethodHelper.buildDToken.invoke(null, element,this.jsonsplit,request,fid,this.cache));
//				TokenMethodHelper.doDTokencheck.invoke(null,request, response);
			}
			 catch (RuntimeException e) {
					throw new JspException(e);
				} 
			catch (Exception e) {
				throw new JspException(e);
			} catch (Throwable e) {
				throw new JspException(e);
			}
		}
		
//		if(!TokenHelper.isEnableToken() )
//		{
//			return ret;
//		}
//		
//		try {
//			out.print(TokenHelper.getTokenService().buildDToken(element,this.jsonsplit,request,fid,this.cache));
//		} catch (IOException e) {
//			throw new JspException(e);
//		}
//		catch (RuntimeException e) {
//			//
//		} 
		return ret;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public boolean isCache() {
		return cache;
	}
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	
	

}
