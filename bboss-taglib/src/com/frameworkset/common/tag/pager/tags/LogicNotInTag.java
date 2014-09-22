package com.frameworkset.common.tag.pager.tags;

import com.frameworkset.util.ValueObjectUtil;

/**
 * 
 * <p>Title: LogicNotInTag</p>
 *
 * <p>Description: 判断计算属性或者expression属性对应的表达式得到值是否包含
 * 在scope属性或表达式对应的值范围之内，值范围以逗号分隔，如果不包含则执行notin标签体对应的代码，否则不执行</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * 
 * @Date 2007-12-7 10:36:55
 * @author biaoping.yin
 * @version 1.0
 */
public class LogicNotInTag extends MatchTag {
	
	protected boolean match() {

		if(actualValue == null)
			return true;
		this.scopes = this.getScopes();
//		String tmp = String.valueOf(this.actualValue);
		for(int i = 0; i < this.scopes.length; i ++)
		{
			if(ValueObjectUtil.typecompare(actualValue,scopes[i]) == 0)
				return false;
		}
		return true;
		
	}

}
