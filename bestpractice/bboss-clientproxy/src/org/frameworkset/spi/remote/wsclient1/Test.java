package org.frameworkset.spi.remote.wsclient1;

import javax.xml.ws.Holder;

import org.frameworkset.spi.remote.wsclient.ObjectFactory;
import org.frameworkset.spi.remote.wsclient.TableOfZstFykz;


public class Test {
	public static void main(String args[])
	{
		WSServiceImplService services = new WSServiceImplService();
		WSService port = services.getWSServiceImplPort();
		// create parameter
        ObjectFactory factory = new ObjectFactory();
        Holder<TableOfZstFykz> fi00O06 = new Holder<TableOfZstFykz>(factory.createTableOfZstFykz());
        //返回参数设置
        
		System.out.println(port.sayHello("aaa"));
	}

}
