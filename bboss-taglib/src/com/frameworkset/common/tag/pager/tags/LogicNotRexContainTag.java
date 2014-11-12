package com.frameworkset.common.tag.pager.tags;

import com.frameworkset.util.RegexUtil;

public class LogicNotRexContainTag extends MatchTag{
	protected boolean match() {
		
		if(actualValue == null)
			return true;
		if(this.pattern != null)
		{
			if(!RegexUtil.isContain(String.valueOf(actualValue),pattern))
				return true;		
			else
				return false;
		}
		else if(this.getValue() != null)
		{
			return !String.valueOf(actualValue).contains(String.valueOf(this.getValue()));
		}
		else
			throw new java.lang.IllegalArgumentException("Tag notcontain must set value or pattern or expressionValue atrribute.");
	}	

}
