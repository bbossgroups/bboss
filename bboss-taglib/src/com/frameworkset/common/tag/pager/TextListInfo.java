package com.frameworkset.common.tag.pager;

import java.io.Serializable;

/**
 * <p>Title: TextListInfo</p>
 *
 * <p>Description: 手机wap网站文本分页数据，包括页面总数量，当页显示的文字</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TextListInfo implements Serializable{
    /**页面总记录数*/
    private int total;
    private int curPage;
    /**当前页面显示的文本*/
    private String curValue;

    public static void main(String[] args) {
        TextListInfo textlistinfo = new TextListInfo();
    }

    public String getCurValue() {
        return curValue;
    }

    public int getTotal() {
        return total;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurValue(String curValue) {
        this.curValue = curValue;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public String toString()
    {
        return "[total:"+total + "][current page:"+curPage + "][curvalue:" + curValue + "]";
    }
}
