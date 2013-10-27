package com.frameworkset.common.tag.pager.tags;

import com.frameworkset.common.tag.BaseTag;

/**
 * <p>Title: TextSupportTag</p>
 *
 * <p>Description: 初始化文本分页标签</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class TextSupportTag extends BaseTag {
    protected TextPagerTag pagerTag;
    public int doStartTag()
    {
        pagerTag = (TextPagerTag)this.findAncestorWithClass(this,TextPagerTag.class);
        return EVAL_BODY_INCLUDE;
    }
}
