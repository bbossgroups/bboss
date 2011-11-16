package com.frameworkset.common.tag.pager.util;

import java.io.Serializable;

import com.frameworkset.common.tag.pager.tags.PagerTag;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class IndexHelper implements Serializable{
    private PagerTag pagerTag = null;
    public static void main(String[] args) {
        //IndexHelper indexhelper = new IndexHelper();
    }

    public IndexHelper(PagerTag pagerTag)
    {
        this.pagerTag = pagerTag;
    }
}
