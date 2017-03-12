package org.frameworkset.remote;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.event.Event;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;

public class RpcDispatcherAnycastServerObject extends ReceiverAdapter {
   
    private final JChannel c;
    private final RpcDispatcher d;

    public RpcDispatcherAnycastServerObject(JChannel channel,EventRemoteService eventService) throws Exception {
        c=channel;
        d=new RpcDispatcher(c, eventService);
        d.setMembershipListener(this);
        d.setStateListener(this);
    }

   

    public void callRemote( boolean excludeSelf,String eventfqn,Event<?> event) throws Exception {
        // we need to copy the vector, otherwise the modification below will throw an exception because the underlying
        // vector is unmodifiable
        List<Address> v=new ArrayList<Address>(c.getView().getMembers());
        if(excludeSelf) 
        {
        	v.remove(c.getAddress());
        	d.callRemoteMethods(v, "remoteEventChange", new Object[]{}, new Class[]{String.class,Event.class}, new RequestOptions(ResponseMode.GET_NONE, 0));
        }
        else
        {
        	d.callRemoteMethods(null, "remoteEventChange", new Object[]{}, new Class[]{String.class,Event.class}, new RequestOptions(ResponseMode.GET_NONE, 0));
        }
        
       

    }
    
    public void callRemote(  String eventfqn,Event<?> event) throws Exception {
    	 callRemote( false, eventfqn,event);
       

    }

    public void shutdown() {
        c.close();
    }


}