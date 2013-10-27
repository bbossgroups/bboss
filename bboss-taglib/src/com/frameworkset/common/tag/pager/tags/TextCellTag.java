package com.frameworkset.common.tag.pager.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import com.frameworkset.util.StringUtil;

/**
 * <p>Title: TextCellTag</p>
 *
 * <p>Description: 输出文本的当前段</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TextCellTag extends TextSupportTag {
    private static Logger log = Logger.getLogger(TextCellTag.class);
    public int doEndTag() throws JspException
    {
        try {
            this.getJspWriter().print(StringUtil.toUTF(pagerTag.getListInfo().getCurValue()));
        } catch (IOException ex) {
            log.error(ex);
        }
        return super.doEndTag();
    }
}
