package org.frameworkset.web.token.ws;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "TokenService", targetNamespace = "org.frameworkset.web.token.ws.TokenService")
public interface TokenService {
	public @WebResult(name = "authTempToken", partName = "partAuthTempToken")
	TokenGetResponse genAuthTempToken(
			@WebParam(name = "appid", partName = "partAppid") String appid,
			@WebParam(name = "secret", partName = "partSecret") String secret,
			@WebParam(name = "account", partName = "partAccount") String account)
			throws Exception;

	public @WebResult(name = "dualToken", partName = "partDualToken")
	TokenGetResponse genDualToken(
			@WebParam(name = "appid", partName = "partAppid") String appid,
			@WebParam(name = "secret", partName = "partSecret") String secret,
			@WebParam(name = "account", partName = "partAccount") String account)
			throws Exception;

	
	public @WebResult(name = "tempToken", partName = "partTempToken") TokenGetResponse genTempToken() throws Exception;
	public @WebResult(name = "ticket", partName = "partTicket")  
	TokenGetResponse genTicket(
			@WebParam(name = "account", partName = "partAccount") String account,
			@WebParam(name = "worknumber", partName = "partWorknumber") String worknumber,
			@WebParam(name = "appid", partName = "partAppid") String appid,
			@WebParam(name = "secret", partName = "partSecret") String secret)throws Exception;
}
