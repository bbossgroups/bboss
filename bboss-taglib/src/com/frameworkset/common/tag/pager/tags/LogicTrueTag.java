package com.frameworkset.common.tag.pager.tags;


public class LogicTrueTag  extends MatchTag {

	/**
	 *  Description: 大于等于
	 * @return boolean
	 * @see com.frameworkset.common.tag.pager.tags.MatchTag#match()
	 */
//	protected boolean match() {
//		if(actualValue == null)
//			return false;
//		if(String.valueOf(actualValue).compareTo(String.valueOf(getValue())) >= 0)
//			return true;
//		else
//			return false;
//	}
	
	protected boolean match() {
		if(actualValue == null )
			return false;
		else
		{
			if(typeof == null)
			{
				if(actualValue instanceof Boolean  )
					return (((Boolean)actualValue).booleanValue());
				else if(actualValue instanceof String && actualValue.equals("false"))
				{
					return false;
				}
				else
					return true;
			}
			else
			{
				return istypeof();
					
			}
			
		}
	}
	
	
}
