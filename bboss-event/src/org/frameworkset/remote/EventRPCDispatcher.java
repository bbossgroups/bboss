package org.frameworkset.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;

public class EventRPCDispatcher extends ReceiverAdapter implements
		org.frameworkset.spi.InitializingBean,
		org.frameworkset.spi.DisposableBean {
	private static Logger log = Logger.getLogger(EventRPCDispatcher.class);
	private JChannel channel;
	private RpcDispatcher rpcDispatcher;
	RpcDispatcherAnycastServerObject target;
	EventRemoteService eventService;
	final String GROUP = "EventRpcDispatcherMultipleCalls";

	public EventRPCDispatcher() {

	}
	
	public Address getLocalAddress()
	{
		return this.channel.getAddress();
	}
	
	public boolean containSelf( List<Address> addresses)
	{
		Address address = this.channel.getAddress();
		for(Address ad:addresses)
		{
			if(ad.equals(address) )
				return true;
		}
		return false;
	}
	public  List<Address> removeSelf( List<Address> addresses)
	{
		Address address = this.channel.getAddress();
		 List<Address> temp = new ArrayList();
		 temp.addAll(addresses);
		 temp.remove(address);
		 return temp;
		
	}
	public void callRemote(boolean excludeSelf,   Event<?> event)
			throws Exception {
		// we need to copy the vector, otherwise the modification below will
		// throw an exception because the underlying
		// vector is unmodifiable
		List<Address> v = new ArrayList<Address>();
		v.addAll(channel.getView().getMembers());
		if (excludeSelf) {
			v.remove(channel.getAddress());
			if(v.size() == 0)
				return ;
			boolean use_anycasting = true;
			rpcDispatcher.callRemoteMethods(v, "remoteEventChange",
					new Object[] {event}, new Class[] {   Event.class },
					new RequestOptions(ResponseMode.GET_NONE, 0,use_anycasting));
		} else {
			rpcDispatcher.callRemoteMethods(null, "remoteEventChange",
					new Object[] {event}, new Class[] {   Event.class },
					new RequestOptions(ResponseMode.GET_NONE, 0));
		}

	}
	public List<Address> getAddresses()
	{
		if(this.channel != null)
		{
			List<Address> v = new ArrayList<Address>();
			v.addAll(channel.getView().getMembers());
			return v;
		}
		return null;
	}
	public void callRemote(  List<Address> addresses,  Event<?> event)
			throws Exception {
		// we need to copy the vector, otherwise the modification below will
		// throw an exception because the underlying
		// vector is unmodifiable
		 
		   boolean use_anycasting = true;
			rpcDispatcher.callRemoteMethods(addresses, "remoteEventChange",
					new Object[] {event}, new Class[] {   Event.class },
					new RequestOptions(ResponseMode.GET_NONE, 0,use_anycasting));
		

	}

	public void callRemote(  Event<?> event) throws Exception {
		callRemote(false,  event);

	}

	private void shutdown() {
		if(channel != null)
		{
			log.debug("Event RpcDispatcher Service shutdownding.");
			channel.close();
			log.debug("Event RpcDispatcher Service shutdownded.");
		}
	}

	public void init() throws Exception {
		log.debug("Event RpcDispatcher Service starting.");
		channel = createChannel();
		rpcDispatcher = new RpcDispatcher(channel, eventService);
		rpcDispatcher.setMembershipListener(this);
		rpcDispatcher.setStateListener(this);
		channel.connect(GROUP);
		log.debug("Event RpcDispatcher Service started.");
//		BaseApplicationContext.addShutdownHook(new Runnable() {
//
//			public void run() {
//				shutdown();
//			}
//
//		});

	}

	private JChannel createChannel() throws Exception {
		
		return new JChannel(EventUtils.getProtocolConfigFile());
	}

	public void destroy() throws Exception {
		shutdown();
	}

	public void afterPropertiesSet() throws Exception {

//		channel = createChannel();
//		rpcDispatcher = new RpcDispatcher(channel, this, this, eventService);
//
//		channel.connect(GROUP);
		init() ;

	}

}
