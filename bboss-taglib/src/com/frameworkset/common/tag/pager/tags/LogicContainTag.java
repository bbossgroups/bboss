package com.frameworkset.common.tag.pager.tags;

import com.frameworkset.util.RegexUtil;

/**
 * 逻辑包含标签，
 * 判断当前的值是否和pattern属性对应正则表达式相包含，包含则执行标签体中的代码
 * 否则不执行
 * <p>Title: LogicContainTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-6 20:26:14
 * @author biaoping.yin
 * @version 1.0
 */
public class LogicContainTag extends MatchTag {
	
	protected boolean match() {
		
		if(actualValue == null)
			return false;
		if(pattern != null)
		{
			if(RegexUtil.isContain(String.valueOf(actualValue),pattern))
				return true;
			else
				return false;
		}
		else if(this.getValue() != null)
		{
			return String.valueOf(actualValue).contains(String.valueOf(this.getValue()));
		}
		else
			throw new java.lang.IllegalArgumentException("Tag Contain must set value or pattern or expressionValue atrribute.");
	}	

}
