package org.frameworkset.web.ws;

import org.frameworkset.spi.remote.clientproxy.WSServiceInMVC;
import org.frameworkset.spi.remote.webservice.JaxWsProxyFactory;



public class WSClient {

	public static void main(String[] args) {
		WSServiceInMVC wsservice = JaxWsProxyFactory.getWSClient("http://localhost:8080/" +
				"demo/cxfservices/mysfirstMVCwsservicePort",WSServiceInMVC.class);
		System.out.print(wsservice.sayMvsHello("¶à¶à"));

	}

}
