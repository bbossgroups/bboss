package org.frameworkset.camel;

import org.apache.activemq.camel.component.CamelEndpointLoader;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.camel.CamelContext;

public class CMQEndpointLoader extends CamelEndpointLoader{
	 private CamelContext camelContext;
	  
	 private CMQComponent component;
	public CMQEndpointLoader() {
		super();
		
	}

	public CMQEndpointLoader(CamelContext camelContext) {
		super(camelContext);
		
	}
	
	
	 public CMQComponent getComponent() {
	        if (component == null) {
	            component = camelContext.getComponent("cmq", CMQComponent.class);
	        }
	        return component;
	    }

	    public void setComponent(CMQComponent component) {
	    	 super.setComponent(component);
	        this.component = component;
	       
	    }


	    protected String getQueueUri(ActiveMQQueue queue) {
	        return "cmq:" + queue.getPhysicalName();
	    }

	   
}
