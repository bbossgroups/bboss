package com.frameworkset.common.tag.pager.tags;
/**
 * 
 * <p>Title: LogicInTag</p>
 *
 * <p>Description: 判断计算属性或者expression属性对应的表达式得到值是否包含
 * 在scope属性或表达式对应的值范围之内，值范围以逗号分隔，如果包含则执行in标签体对应的代码，否则不执行</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 长沙科创计算机系统集成有限公司</p>
 * @Date 2007-12-7 10:36:55
 * @author biaoping.yin
 * @version 1.0
 */
public class LogicInTag extends MatchTag {
	
	protected boolean match() {

		if(actualValue == null)
			return false;
		String tmp = String.valueOf(this.actualValue);
		for(int i = 0; i < this.getScopes().length; i ++)
		{
			if(tmp.equals(scopes[i]))
				return true;
		}
		return false;
		
	}

}
