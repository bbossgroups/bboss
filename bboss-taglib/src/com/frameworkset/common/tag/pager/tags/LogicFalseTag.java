package com.frameworkset.common.tag.pager.tags;


public class LogicFalseTag  extends MatchTag {

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
			return true;
		else
		{
			if(typeof == null)
			{
				if(actualValue instanceof Boolean  )
					return !(((Boolean)actualValue).booleanValue());
				else if(actualValue instanceof String && actualValue.equals("false"))
				{
					return true;
				}
				else if(actualValue instanceof Number)
				{
					if(actualValue instanceof Integer)
						return ((Number)actualValue).intValue() <= 0;
					else if(actualValue instanceof Long)
						return ((Number)actualValue).longValue() <= 0L;
					else if(actualValue instanceof Float)
						return ((Number)actualValue).floatValue() <= 0.0f;
					else if(actualValue instanceof Double)
						return ((Number)actualValue).doubleValue() <= 0.0d;
					else if(actualValue instanceof Short)
						return ((Number)actualValue).shortValue() <= 0;
					else if(actualValue instanceof Byte)
						return ((Number)actualValue).byteValue() <= 0;
					else
						return ((Number)actualValue).intValue()<= 0;
					
					
				}
				else
					return false;
			}
			else
			{
				return !this.istypeof();
			}
		}
	}
	
}
