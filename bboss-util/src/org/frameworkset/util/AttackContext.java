package org.frameworkset.util;
/**
 * Copyright 2020 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2021/12/1 10:02
 * @author biaoping.yin
 * @version 1.0
 */
public class AttackContext {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain chain;
	private ReferHelper referHelper;
	private String[] values;
	private String paramName;
	private String attackRule;
	private int position;
	private boolean redirected;
	public static final int XSS_ATTACK = 0;
	public static final int SENSITIVE_ATTACK = 1;

	public int getAttackType() {
		return attackType;
	}

	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}

	private int attackType ;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public FilterChain getChain() {
		return chain;
	}

	public void setChain(FilterChain chain) {
		this.chain = chain;
	}

	public ReferHelper getReferHelper() {
		return referHelper;
	}

	public void setReferHelper(ReferHelper referHelper) {
		this.referHelper = referHelper;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getAttackRule() {
		return attackRule;
	}

	public void setAttackRule(String attackRule) {
		this.attackRule = attackRule;
	}

	public boolean isRedirected() {
		return redirected;
	}

	public void setRedirected(boolean redirected) {
		this.redirected = redirected;
	}
}
