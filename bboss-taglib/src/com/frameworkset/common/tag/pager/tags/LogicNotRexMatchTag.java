package com.frameworkset.common.tag.pager.tags;

import com.frameworkset.util.RegexUtil;

public class LogicNotRexMatchTag extends MatchTag {
	
	protected boolean match() {
		
		if(actualValue == null)
			return true;
		if(!RegexUtil.isMatch(String.valueOf(actualValue),pattern))
			return true;
		else
			return false;
	}

}
