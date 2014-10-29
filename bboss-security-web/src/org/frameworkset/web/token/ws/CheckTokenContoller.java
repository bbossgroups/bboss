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
package org.frameworkset.web.token.ws;

import javax.jws.WebService;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.token.TokenException;
import org.frameworkset.web.token.TokenHelper;
import org.frameworkset.web.token.TokenResult;
import org.frameworkset.web.token.TokenService;
import org.frameworkset.web.token.TokenStore;

/**
 * <p>Title: CheckTokenContoller.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月16日
 * @author biaoping.yin
 * @version 3.8.0
 */
@WebService(name="CheckTokenService",targetNamespace="org.frameworkset.web.token.ws.CheckTokenService")
public class CheckTokenContoller implements CheckTokenService{
	
	public @ResponseBody(datatype="json") TokenCheckResponse checkToken(String appid,String secret,String token) 
	{
		TokenCheckResponse tokenCheckResponse = new TokenCheckResponse();
		try {
			TokenResult result = TokenHelper.getTokenService().checkToken(appid,secret,token);
			tokenCheckResponse.setResultcode(TokenStore.RESULT_OK );
			tokenCheckResponse.setValidateResult(TokenService.assertDToken(result.getResult()));
			tokenCheckResponse.setUserAccount(result.getAccount());
			tokenCheckResponse.setWorknumber(result.getWorknumber());
			
		} catch (TokenException e) {
			tokenCheckResponse.setResultcode(e.getMessage());
		}
		 catch (Exception e) {
			 tokenCheckResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
		}
		return tokenCheckResponse;
	}
	public @ResponseBody(datatype="json") TokenCheckResponse checkTicket(String appid,String secret,String ticket) 
	{
		TokenCheckResponse tokenCheckResponse = new TokenCheckResponse();
		try {
			TokenResult result = TokenHelper.getTokenService().checkToken(appid,secret,ticket);
			tokenCheckResponse.setResultcode(TokenStore.RESULT_OK );
			tokenCheckResponse.setValidateResult(TokenService.assertDToken(result.getResult()));
			tokenCheckResponse.setUserAccount(result.getAccount());
			tokenCheckResponse.setWorknumber(result.getWorknumber());
			
		} catch (TokenException e) {
			tokenCheckResponse.setResultcode(e.getMessage());
		}
		 catch (Exception e) {
			 tokenCheckResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
		}
		return tokenCheckResponse;
	}
	public @ResponseBody(datatype="json") TokenCheckResponse checkTempToken(String token)
	{
		TokenCheckResponse tokenCheckResponse = new TokenCheckResponse();
			try {
				tokenCheckResponse.setValidateResult(TokenService.assertDToken(TokenHelper.getTokenService().checkTempToken(token)));
				tokenCheckResponse.setResultcode(TokenStore.RESULT_OK);
			} catch (TokenException e) {
				tokenCheckResponse.setResultcode(e.getMessage());
			}
			 catch (Exception e) {
				 tokenCheckResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
				}
			return tokenCheckResponse;
	}



}
