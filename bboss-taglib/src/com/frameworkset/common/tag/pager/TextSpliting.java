package com.frameworkset.common.tag.pager;

import java.io.Serializable;

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
public class TextSpliting implements Serializable{

    /**
     * 获取给定页面的文本内容，页面号码超出页面的号码范围时，
     * 取首页（页面号码小于0）或者最后一页（页面号码大于最后一页的页码）
     * @param text String 文本内容
     * @param pageNumber int 页面号码
     * @param pageSize int 每页显示的文本长度
     * @return TextListInfo 封装返回信息：
     *                      总页面数，
     *                      当前页面号码，
     *                      当前页面显示的文本
     *
     */
    public static TextListInfo splitStringByPageNumber(String text,int pageNumber,int pageSize)
    {
        return splitString(text,(pageNumber -1) * pageSize,pageSize);
    }
    public static TextListInfo splitString(String text,int offset,int pageSize)
    {
        TextListInfo listInfo = new TextListInfo();
        if(text == null && text.length() == 0)
        {
            listInfo.setTotal(0);
            listInfo.setCurPage(0);
            listInfo.setCurValue("");
            return listInfo;
        }
        int length = text.length();

        int total = length / pageSize ;
        if(length % pageSize != 0)
            total += 1;

        if(offset < 0)
            offset = 0;
        else
        {

            if (offset >= length) {
                int size = length % pageSize;
                if(size == 0)
                {
                    offset = length - pageSize ;
                }
                else
                {
                    offset = length - size ;
                }
            }
        }
        int end = offset + pageSize < length? offset + pageSize: length ;
        String curvalue = text.substring(offset,end);
        listInfo.setCurValue(curvalue);
        listInfo.setCurPage(offset/pageSize + 1);
        listInfo.setTotal(total);
        return listInfo;
    }

    public static void main(String[] args)
    {
        String test = "中华人民共和国成立了！！！中华人民共和国万岁！！！！";
        for(int i = 1; i <= 13; i ++)
        System.out.println( TextSpliting.splitStringByPageNumber(test,i,2));
    }

}
