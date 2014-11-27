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

public class OtherTag extends BaseTag {
	@Override
	public int doStartTag() throws JspException {
		Tag tag = super.findAncestorWithClass(this, Tag.class);
		if(tag instanceof CaseTag)
		{
			MatchTag  matchTag = (CaseTag)tag;
			if(!matchTag.isResolvedResult())
			{
				matchTag.setResolvedResult(true);
				return EVAL_BODY_INCLUDE;
			}
			else
			{
				return SKIP_BODY;
			}
		}
		else
		{
			throw new JspException("other tag must be included in case tag。");
		}
			
	}
}
