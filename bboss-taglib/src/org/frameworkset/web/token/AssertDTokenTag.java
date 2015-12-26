/*
 *  Copyright 2008 bbossgroups
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.frameworkset.web.token;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.MethodData;
import org.frameworkset.web.servlet.handler.HandlerUtils;

import com.frameworkset.common.tag.BaseTag;

/**
 * <p>Title: AssertDTokenTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2012-8-24
 * @author biaoping.yin
 * @version 3.6
 */
public class AssertDTokenTag extends BaseTag{
	
	@Override
	public void doFinally() {
		// TODO Auto-generated method stub
		super.doFinally();
	}

	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		int ret = super.doStartTag();
		if(TokenMethodHelper.isEnableToken != null)
		{
			try {
				Boolean enableToken = (Boolean)TokenMethodHelper.isEnableToken.invoke(null);
				if(!enableToken.booleanValue())
					return ret;
			} catch (RuntimeException e) {
				throw new JspException(e);
			} 
			catch (Exception e) {
				throw new JspException(e);
			} catch (Throwable e) {
				throw new JspException(e);
			}
			try {
				TokenMethodHelper.doDTokencheck.invoke(null,request, response);
			}
			 catch (RuntimeException e) {
					throw new JspException(e);
				} 
			catch (Exception e) {
				throw new JspException(e);
			} catch (Throwable e) {
				throw new JspException(e);
			}
		}
		
//		if(!TokenHelper.isEnableToken() )
//			return ret;
//		try {
//			TokenHelper.doDTokencheck(request, response);
//		} catch (IOException e) {
//			throw new JspException(e);
//		} catch (DTokenValidateFailedException e) {
//			throw new JspException(e);
//		}
		return ret;
	}

}
