/*
 *  Copyright 2008 biaoping.yin
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
package org.apache.cxf.transport.servlet;



import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.cxf.BusFactory;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.resource.ResourceManager;
import org.frameworkset.spi.remote.webservice.WSLoader;
import org.frameworkset.spi.remote.webservice.WSUtil;
/**
 * 
 * <p>Title: RPCCXFServlet.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-4 ÏÂÎç04:56:02
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCCXFServlet extends AbstractCXFServlet

{
	
    
    public void loadBus(ServletConfig servletConfig) throws ServletException {
        if(WSUtil.webservice_enable)
        {        	
    //        super.loadBus(servletConfig);        
        	loadBusNoConfig(servletConfig);
            // You could add the endpoint publish codes here
//        	org.apache.cxf.transport.servlet.AbstractCXFServlet.LOG.info("LOAD_BUS_WITHOUT_APPLICATION_CONTEXT");
    //        Bus bus = getBus();
    //        BusFactory.setDefaultBus(bus); 
        	WSLoader loader = new WSLoader();
        	ClassLoader classLoader = this.getClass().getClassLoader();
        	loader.loadAllWebService( classLoader);
//            ProList webservices = WSUtil.webservices;
//            if(webservices != null)
//            {
//                for(int i = 0;i < webservices.size(); i ++)
//                {
//                    try
//                    {
//                        Pro pro = webservices.getPro(i);
//                        Object webservice = pro.getBeanObject();
//                        String servicePort = pro.getStringExtendAttribute("servicePort");
//                        if(servicePort == null || servicePort.trim().equals(""))
//                            throw new java.lang.IllegalArgumentException("web service ["+pro.getName() + "] config error: must config servicePort attribute for web service ["+pro.getName() + "]"  );
//                        String mtom = pro.getStringExtendAttribute("mtom");
//                        Endpoint ep = Endpoint.publish("/" + servicePort, webservice);
//                        SOAPBinding binding = (SOAPBinding) ep.getBinding();
//                        if(mtom != null && mtom.equalsIgnoreCase("true"))
//                            binding.setMTOMEnabled(true);
//                    }
//                    catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                    
//                }
//            }
        }
        else
        {
            System.out.println("CXF not started,rpc.webservice.enable = false. Please check config file [org/frameworkset/spi/manager-rpc-service.xml] to enable you cxf webservice.");
        }
        
        // You can als use the simple frontend API to do this
//        ServerFactoryBean factroy = new ServerFactoryBean();
//        factory.setBus(bus);
//        factory.setServiceClass(GreeterImpl.class);
//        factory.setAddress("/Greeter");
//        factory.create();              
    }
    
    public static Logger getLogger()
    {
        return LogUtils.getL7dLogger(RPCCXFServlet.class);
    }

//    public void loadBus(ServletConfig servletConfig)
//        throws ServletException
//    {
//        loadBusNoConfig(servletConfig);
//    }

    private void loadBusNoConfig(ServletConfig servletConfig)
        throws ServletException
    {
        if(bus == null)
        {
        	org.apache.cxf.transport.servlet.AbstractCXFServlet.LOG.info("LOAD_BUS_WITHOUT_APPLICATION_CONTEXT");
            bus = BusFactory.newInstance(BusFactory.DEFAULT_BUS_FACTORY).createBus();
        }
        ResourceManager resourceManager = (ResourceManager)bus.getExtension(org.apache.cxf.resource.ResourceManager.class);
        resourceManager.addResourceResolver(new ServletContextResourceResolver(servletConfig.getServletContext()));
        replaceDestinationFactory();
        controller = createServletController(servletConfig);
    }


}
