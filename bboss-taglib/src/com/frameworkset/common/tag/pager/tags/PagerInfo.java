/*
 * Created on 2005-3-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.common.tag.pager.tags;

import java.io.Serializable;

/**
 * @author biaoping.yin
 * created on 2005-3-31
 * version 1.0 
 */
public interface PagerInfo extends Serializable {
    
    /**
     * 返回页面的数据条数
     * @return 数据条数
     */
    public int getDataSize();

}
