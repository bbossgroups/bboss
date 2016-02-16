package org.frameworkset.spi.remote.hession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestfulHessianHandlerFactory extends HessianHandlerFactory {

	public RestfulHessianHandlerFactory() {
		// TODO Auto-generated constructor stub
	}
	protected String getServiceName(HttpServletRequest request,HttpServletResponse response)
	{
		String service=request.getRequestURI();
		int idx = service.lastIndexOf('/');
		if(idx > 0)
		{
			return service.substring(idx+1);
		}
		else
		{
			throw new HessionException(service + " is not a hessian service address.");
		}
	}
	
	public static void main(String[] args)
	{
		String service="aa/";
		int idx = service.lastIndexOf('/');
		
		
		System.out.println( service.substring(idx+1));
	}
}
