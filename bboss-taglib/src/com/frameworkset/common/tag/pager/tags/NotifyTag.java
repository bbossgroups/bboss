/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager.tags;



import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;



/**
 * 根据列表分页是否有数据来输出相应的提示信息
 * @author biaoping.yin
 * created on 2005-4-4
 * version 1.0
 */
public class NotifyTag extends PagerTagSupport {
	private int colid = -1;
	private String colName;
	private String property;
	private int index = -1;
	
	/**
	 * @return int
	 */
	public int getIndex() {
		return index;
	}
	
	public int getColid() {
		return this.colid;
	}
	/**
	 * @param colid
	 */
	public void setColid(int colid) {
		this.colid = colid;
	}
	/**
	 * @return String 返回属性名称
	 */
	public String getColName() {
		return colName;
	}
	/**
	 * @param string
	 */
	public void setColName(String string) {
		colName = string;
	}
	
	protected PagerDataSet searchDataSet(Tag obj,Class clazz) {
		PagerDataSet dataSet = null;
		if (this.getIndex() < 0) {
			dataSet =
				(PagerDataSet) findAncestorWithClass(obj, clazz);
		} else {
			HttpServletRequest request = this.getHttpServletRequest();
			//int idx = index();
			java.util.Stack stack =
				(java.util.Stack) request.getAttribute(
					PagerDataSet.PAGERDATASET_STACK);
			
			dataSet = (PagerDataSet) stack.elementAt(this.getIndex());
		}
		
		return dataSet;
	}
//    private Notification notification;
    public int doStartTag() throws JspException
    {
    	super.doStartTag();
//        this.setMeta();
		//System.out.println("cell dataset:" + dataSet);
		if (this.getColid() != -1) {
		    PagerDataSet dataSet = searchDataSet(this,PagerDataSet.class);
			if (getProperty() == null)
			{
				Collection datas = (Collection)dataSet.getValue(dataSet.getRowid(), getColid());
				if(datas.size() <=0)
				{
				    //this.notification.setOutput(true);
				    return EVAL_BODY_INCLUDE;
				}
			}
			else
			{
			    Collection datas = (Collection)dataSet.getValue(dataSet.getRowid(),getColid(),getProperty());
			    if(datas.size() <=0)
			    {
			        //this.notification.setOutput(true);
				    return EVAL_BODY_INCLUDE;
			    }
			}
		} else if (getColName() != null) {
		    PagerDataSet dataSet = searchDataSet(this,PagerDataSet.class);
			if (getProperty() == null)
			{
			    Collection datas = (Collection)dataSet.getValue(dataSet.getRowid(), this.getColName());
			    if(datas.size() <=0)
			    {
			        //this.notification.setOutput(true);
				    return EVAL_BODY_INCLUDE;
			    }
			}
			else
			{
			    Collection datas = (Collection)dataSet.getValue(dataSet.getRowid(),getColName(),getProperty());
			    if(datas.size() <=0)
			    {
			        //this.notification.setOutput(true);
				    return EVAL_BODY_INCLUDE;
			    }

			}
			//outStr = dataSet.getString(dataSet.getRowid(), this.getColName());
		}
        else
        {
        	if(pagerContext != null )
        	{
        		if(pagerContext.isNotifyed())
        			return SKIP_BODY;
        		pagerContext.setNotifyed(true);
//				out.print(super.pagerContext.getItemCount());
        		if(!pagerContext.ListMode())
        		{
        			if(pagerContext.getItemCount() <= 0 )
        			{
        				if(pagerContext.getDataResultSize() <= 0)
        					return EVAL_BODY_INCLUDE;
        			}
        			
        		}
        		else
        		{
        			if(pagerContext.getDataSize() <= 0)
        				return EVAL_BODY_INCLUDE;
        		}
        	}
			else
			{
//				out.println(super.pagerContext.getDataSize());
				 return EVAL_BODY_INCLUDE;
			}
//        	PagerInfo pager = (PagerInfo) findAncestorWithClass(this, PagerTag.class);
//            if(pager.getDataSize() <= 0)
//            {
//                //this.notification.setOutput(true);
//                return EVAL_BODY_INCLUDE;
//            }
        }

        return SKIP_BODY;

    }

	public void setMeta()
	{
//	    if(this.isExportMeta())
//	    {
//	        if(this.notification == null)
//	        {
//	            this.notification = new Notification();
//	            //notification.setNotification(this.getBodyContent().getString());	            
//	        }
//	        
//	        /**
//		     * 保存dataModel
//		     */
//		    if(!notification.isHasAdded())
//		    {			    
//		        //如果没有最上层的dataSet元数据则在pagerTag中添加添加，
//		        //否则直接搜索父dataSet元数据，将notification添加到其元数据模型中
//		        if(pagerContext != null && !pagerContext.getMetaDatas().hasDataModel())
//		        {
//		        	pagerContext.getMetaDatas().setNotification(notification);
//			        
//		        }
//		        else
//		        {
//		            //直接保存到上一级dataSet的元数据模型中
//		            PagerDataSet data_father = (PagerDataSet)findAncestorWithClass(this, PagerDataSet.class);
//		            data_father.getDataModel().getMetaDatas().setNotification(notification);		            		            
//		        }
//		        notification.setHasAdded(true);		        
//		    }
//	    }
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
	public int doEndTag() throws JspException
	{
		this.colid = -1;
		this.colName = null;
		this.index = -1;
		this.property = null;
		return super.doEndTag();
	}
	 
}
