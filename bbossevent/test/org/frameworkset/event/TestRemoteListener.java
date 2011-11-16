package org.frameworkset.event;

import org.frameworkset.spi.remote.RPCHelper;

public class TestRemoteListener {
	
	public static void main(String[] args)
	{
		RPCHelper.getRPCHelper().startRPCServices();
		ExampleListener listener = new ExampleListener();
		listener.init();
	}

}
