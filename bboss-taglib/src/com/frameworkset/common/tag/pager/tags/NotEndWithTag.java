package com.frameworkset.common.tag.pager.tags;

public class NotEndWithTag extends MatchTag
{

	
	/**
	 *  Description: 等于
	 * @return  boolean
	 * @see com.frameworkset.common.tag.pager.tags.MatchTag#match()
	 */
//	protected boolean match() {				 
//		if(actualValue == null && getValue() == null)
//			return true;
//		if(getValue() == null || actualValue == null)
//		{
//			return false;
//		}
//		if(String.valueOf(getValue()).compareTo(String.valueOf(actualValue)) == 0)
//			return true;
//		return false;
//	}
	protected boolean match() {				 
//		if(actualValue == null && getValue() == null)
//			return true;
//		if(getValue() == null || actualValue == null)
//		{
//			return false;
//		}
		return !endWith();
	}

}
