package com.frameworkset.common.tag.pager.tags;

import com.frameworkset.util.RegexUtil;

public class LogicNotRexContainTag extends MatchTag{
	protected boolean match() {
		
		if(actualValue == null)
			return false;
		if(!RegexUtil.isContain(String.valueOf(actualValue),pattern))
			return true;
		else
			return false;
	}	

}
