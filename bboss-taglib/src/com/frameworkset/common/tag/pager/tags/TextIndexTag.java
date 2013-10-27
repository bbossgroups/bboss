package com.frameworkset.common.tag.pager.tags;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.pager.TextListInfo;
import com.frameworkset.util.StringUtil;
import org.apache.log4j.Logger;

/**
 * <p>Title: TextIndexTag</p>
 *
 * <p>Description: 生成文本分页索引
 *          共几页
 *          首页
 *          上一页
 *          下一页
 *          尾页
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TextIndexTag extends TextSupportTag {
    private static Logger log = Logger.getLogger(TextIndexTag.class);
    public int doEndTag() throws JspException
    {
        TextListInfo listInfo = pagerTag.getListInfo();
        if(listInfo.getTotal() > 0 && listInfo.getTotal() != 1)
        {
            try
            {
                StringBuffer output = new StringBuffer();
                output.append("<br/>");
                output.append(StringUtil.toUTF("第" + listInfo.getCurPage() + "页"));
                output.append(StringUtil.toUTF("  共" + listInfo.getTotal() + "页"))

                    .append("<br/>");

                if (listInfo.getCurPage() == 1 ) {
                    output.append("<a href=\"")
                          .append(pagerTag.getPageURL(listInfo.getCurPage() + 1))
                          .append("\">")
                          .append(StringUtil.toUTF("下一页"))
                          .append("</a><br/>");
                    output.append("<a href=\"")
                          .append(pagerTag.getPageURL(listInfo.getTotal()))
                          .append("\">")
                          .append(StringUtil.toUTF("末页"))
                          .append("</a>");
                }
                else if (listInfo.getCurPage() == listInfo.getTotal()) {
                    output.append("<a href=\"")
                          .append(pagerTag.getPageURL(1))
                          .append("\">")
                          .append(StringUtil.toUTF("首页"))
                          .append("</a><br/>");
                    output.append("<a href=\"")
                          .append(pagerTag.getPageURL(listInfo.getCurPage() - 1))
                          .append("\">")
                          .append(StringUtil.toUTF("上一页"))
                          .append("</a>");
                }
                else
                {
                    output.append("<a href=\"")
                          .append(pagerTag.getPageURL(1))
                          .append("\">")
                          .append(StringUtil.toUTF("首页"))
                          .append("</a><br/>");
                    output.append("<a href=\"")
                          .append(pagerTag.getPageURL(listInfo.getCurPage() - 1))
                          .append("\">")
                          .append(StringUtil.toUTF("上一页"))
                          .append("</a>");
                    output.append("<a href=\"")
                            .append(pagerTag.getPageURL(listInfo.getCurPage() + 1))
                            .append("\"><br/>")
                            .append(StringUtil.toUTF("下一页"))
                            .append("</a><br/>");
                    output.append("<a href=\"")
                          .append(pagerTag.getPageURL(listInfo.getTotal()))
                          .append("\">")
                          .append(StringUtil.toUTF("末页"))
                          .append("</a>");
                }
                this.getJspWriter().print(output.toString());
            }
            catch(Exception e)
            {
                log.error(e);
            }
        }
        return super.doEndTag();
    }

}
