/*
 *  Copyright 2008 biaoping.yin
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
package com.frameworkset.tag.logic;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.pager.tags.MatchTag;

public class NoTag extends BaseTag{

	public NoTag() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int doAfterBody() throws JspException {
		// TODO Auto-generated method stub
		return super.doAfterBody();
	}

	@Override
	public int doStartTag() throws JspException {
		Tag tag = super.findAncestorWithClass(this, Tag.class);
		if(tag instanceof MatchTag)
		{
			MatchTag  matchTag = (MatchTag)tag;
			if(!matchTag.isEvalbody())
			{
				throw new JspException("no tag must be included in a logic tag whith attribute evalbody=\"true\"。");
			}
			matchTag.setHasno(true);
			if(matchTag.isResult())
			{
				return SKIP_BODY;
			}
			else
			{
				matchTag.setResolvedResult(true);
				return EVAL_BODY_INCLUDE;
			}
		}
		else
		{
			throw new JspException("no tag must be included in logic tag。");
		}
		
	}

}
