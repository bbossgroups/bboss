package com.frameworkset.common.tag.pager.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.pager.TextListInfo;
import com.frameworkset.common.tag.pager.TextSpliting;

/**
 * <p>Title: TextPagerTag</p>
 *
 * <p>Description:
 *      文本分页标签,属性如下：
            scope－text的有效范围，分别request、session、pageContext，缺省为request
            parameter—request参数名称
            attribute－属性名称，与request、session、pageContext组合使用
            text－文本值，直接指定待分页的文本，如果不指定则按相关属性从request、session、pageCongtext中获取属性值
            size－每页显示的文本段长度，缺省为200
            id－标签标识
</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TextPagerTag extends BaseTag {
    private Logger log = Logger.getLogger(TextPagerTag.class);
    /**
     * 请求范围
     * request 缺省
     * session
     * pageContext
     */
    private String scope;
    /**request参数名称，与request组合使用*/
    private String parameter;
    /**属性名称，与request、session、pageContext组合使用*/
    private String attribute;
    /**文本*/
    private String text;

    /**
     * 每页显示的文本段长度
     */
    private int size = 200;

    /**
     * 当前页面号码
     */
    private int pageNumber;

    /**分页信息*/
    private TextListInfo listInfo;

    private StringBuffer pageURI;

    private StringBuffer queryString;

    public static void main(String[] args) {
        TextPagerTag textpagertag = new TextPagerTag();
    }

    public int doStartTag() {
        if (scope == null) {
            scope = "request";
        }
        HttpServletRequest request = this.getHttpServletRequest();
        HttpSession session = request.getSession(false);
        if (text == null) {
            if (parameter != null) {
                text = request.getParameter(parameter);
            }
            if (this.attribute != null) {
                Object value = null;
                if (scope.equals("request")) {
                    value = request.getAttribute(attribute);
                } else if (session != null && scope.equals("session")) {
                    value = session.getAttribute(attribute);
                } else {
                    value = pageContext.getAttribute(attribute);
                }
                this.text = (String) value;
            }
        }
        if (text == null) {
            text = "";
        }

        try {
            this.pageNumber = Integer.parseInt(request.getParameter(
                "pageNumber").trim());
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            pageNumber = 1;
        }

        listInfo = TextSpliting.splitStringByPageNumber(text,pageNumber,size);
        pageURI = new StringBuffer(request.getRequestURI());
        queryString = new StringBuffer(0);
        return EVAL_BODY_INCLUDE;
    }

    public void addParam(String name)
    {
    	HttpServletRequest request = this.getHttpServletRequest();
//        HttpSession session = request.getSession(false);
        String[] values = request.getParameterValues(name);
        if(values != null)
        {
            for(int i = 0; i < values.length; i ++)
            {
                this.addParam(name,values[i]);
            }
        }
        else
        {
            String paravalue = (String)request.getAttribute(name);
            if(paravalue != null)
                this.addParam(name,paravalue);
        }

    }

    public void addParam(String name,String value)
    {
        if(queryString.length() > 0)
        {
            this.queryString.append("&amp;"+name + "=" +
                                    java.net.URLEncoder.encode(value));
        }
        else
            this.queryString.append(name + "=" +
                                    java.net.URLEncoder.encode(value));
    }

    protected String getPageURL(int pageNumber)
    {
        StringBuffer pageUrl = new StringBuffer(pageURI.toString());
        if(queryString.length() > 0)
            pageUrl.append("?")
                   .append(queryString.toString())
                   .append("&amp;pageNumber=")
                   .append(pageNumber);
        else
            pageUrl.append("?")
                   .append("pageNumber=")
                   .append(pageNumber);
        return pageUrl.toString();
    }

    protected String getPageURI()
    {
        return pageURI.toString();
    }

    protected String getParams()
    {
        return queryString.toString();
    }


    public int doEndTag() throws JspException {
        this.text = null;
        
        return super.doEndTag();
    }


    public String getScope() {
        return scope;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getParameter() {
        return parameter;
    }

    public String getText() {
        return text;
    }


    public int getPageNumber() {
        return pageNumber;
    }

    public int getSize() {
        return size;
    }

    public TextListInfo getListInfo() {
        return listInfo;
    }


    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setListInfo(TextListInfo listInfo) {
        this.listInfo = listInfo;
    }


}
