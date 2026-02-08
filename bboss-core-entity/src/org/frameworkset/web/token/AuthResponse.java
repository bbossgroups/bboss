package org.frameworkset.web.token;
/**
 * Copyright 2008 biaoping.yin
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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/11/2 21:22
 * @author biaoping.yin
 * @version 1.0
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse implements Serializable {
	public static final String code_200 = "200";
	public static final String code_404 = "404";
	public static final String code_403 = "403";

    public static final String code_405 = "405";
	public static final String code_500 = "500";
	/**
	 * ok
	 */
	private String code = code_200;
	private String message = null;
	private Map<String,Object> user;
	private String systemid;
	private String systemName;
	private String language;
	private String defaultmodulename;
	private String clientIP;
	private Boolean fromsso;
	private String sessionId;


	private Date authTime;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String,Object> getUser() {
		return user;
	}

	public void setUser(Map<String,Object> user) {
//		user.setPasswordText(null);
//		user.setUserPassword(null);
		this.user = user;
	}


	public String getSystemid() {
		return systemid;
	}

	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDefaultmodulename() {
		return defaultmodulename;
	}

	public void setDefaultmodulename(String defaultmodulename) {
		this.defaultmodulename = defaultmodulename;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public Boolean isFromsso() {
		return fromsso;
	}

	public void setFromsso(Boolean fromsso) {
		this.fromsso = fromsso;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Date getAuthTime() {
		return authTime;
	}

	public void setAuthTime(Date authTime) {
		this.authTime = authTime;
	}

}
