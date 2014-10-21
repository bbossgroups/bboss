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



import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.common.classloader.ClassLoaderUtils.ClassLoaderHolder;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.resource.ResourceManager;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.servicelist.ServiceListGeneratorServlet;
import org.frameworkset.spi.remote.webservice.WSLoader;
/**
 * 
 * <p>Title: RPCCXFServlet.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-4 下午04:56:02
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCCXFServlet extends AbstractHTTPServlet {
	private static final Logger LOG = LogUtils.getL7dLogger(RPCCXFServlet.class);
    private static final long serialVersionUID = -2437897227486327166L;
    private DestinationRegistry destinationRegistry;
    private boolean globalRegistry;
    private Bus bus;
    private ServletController controller;
    private ClassLoader loader;
    private boolean loadBus = true;
    
    public RPCCXFServlet() {
    }

    public RPCCXFServlet(DestinationRegistry destinationRegistry) {
        this(destinationRegistry, true);
    }
    public RPCCXFServlet(DestinationRegistry destinationRegistry,
                               boolean loadBus) {
        this.destinationRegistry = destinationRegistry;
        this.globalRegistry = destinationRegistry != null;
        this.loadBus = loadBus;
    }
    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
        if (this.bus == null && loadBus) {
            loadBus(sc);
        }
        if (this.bus != null) {
            loader = bus.getExtension(ClassLoader.class);
            ResourceManager resourceManager = bus.getExtension(ResourceManager.class);
            resourceManager.addResourceResolver(new ServletContextResourceResolver(sc.getServletContext()));
            if (destinationRegistry == null) {
                this.destinationRegistry = getDestinationRegistryFromBus(this.bus);
            }
        }

        this.controller = createServletController(sc);
        finalizeServletInit(sc);
        
//        ResourceManager resourceManager = (ResourceManager)bus.getExtension(org.apache.cxf.resource.ResourceManager.class);
//      resourceManager.addResourceResolver(new ServletContextResourceResolver(servletConfig.getServletContext()));
//      replaceDestinationFactory();
//      controller = createServletController(servletConfig);
    }

    private static DestinationRegistry getDestinationRegistryFromBus(Bus bus) {
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
        try {
            DestinationFactory df = dfm
                .getDestinationFactory("http://cxf.apache.org/transports/http/configuration");
            if (df instanceof HTTPTransportFactory) {
                HTTPTransportFactory transportFactory = (HTTPTransportFactory)df;
                return transportFactory.getRegistry();
            }
        } catch (BusException e) {
            // why are we throwing a busexception if the DF isn't found?
        }
        return null;
    }

//    protected void loadBus(ServletConfig sc) {
//        this.bus = BusFactory.newInstance().createBus();
//    }
    
    private ServletController createServletController(ServletConfig servletConfig) {
        HttpServlet serviceListGeneratorServlet = 
            new ServiceListGeneratorServlet(destinationRegistry, bus);
        ServletController newController =
            new ServletController(destinationRegistry,
                                  servletConfig,
                                  serviceListGeneratorServlet);        
        return newController;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        ClassLoaderHolder origLoader = null;
        Bus origBus = null;
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            try {
                if (loader != null) {
                    origLoader = ClassLoaderUtils.setThreadContextClassloader(loader);
                }
                if (bus != null) {
                    origBus = BusFactory.getAndSetThreadDefaultBus(bus);
                }
                if (controller.filter((HttpServletRequest)request, (HttpServletResponse)response)) {
                    return;
                }
            } finally {
                if (origBus != bus) {
                    BusFactory.setThreadDefaultBus(origBus);
                }
                if (origLoader != null) {
                    origLoader.reset();
                }
            }
        }
        chain.doFilter(request, response);
    }
    @Override
    protected void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ClassLoaderHolder origLoader = null;
        Bus origBus = null;
        try {
            if (loader != null) {
                origLoader = ClassLoaderUtils.setThreadContextClassloader(loader);
            }
            if (bus != null) {
                origBus = BusFactory.getAndSetThreadDefaultBus(bus);
            }
            controller.invoke(request, response);
        } finally {
            if (origBus != bus) {
                BusFactory.setThreadDefaultBus(null);
            }
            if (origLoader != null) {
                origLoader.reset();
            }
        }
    }

    public void destroy() {
        if (!globalRegistry) {
            for (String path : destinationRegistry.getDestinationsPaths()) {
                // clean up the destination in case the destination itself can 
                // no longer access the registry later
                AbstractHTTPDestination dest = destinationRegistry.getDestinationForPath(path);
                synchronized (dest) {
                    destinationRegistry.removeDestination(path);
                    dest.releaseRegistry();
                }
            }
            destinationRegistry = null;
        }
        destroyBus();
    }
    
    public void destroyBus() {
        if (bus != null) {
            bus.shutdown(true);
            bus = null;
        }
    }
	
    
    protected void loadBus(ServletConfig servletConfig) {
    	boolean webservice_enable =  WSLoader.webservice_enable();
    	if(webservice_enable)
        {        	
    //        super.loadBus(servletConfig);        
        	loadBusNoConfig(servletConfig);
            // You could add the endpoint publish codes here
//        	org.apache.cxf.transport.servlet.AbstractCXFServlet.LOG.info("LOAD_BUS_WITHOUT_APPLICATION_CONTEXT");
    //        Bus bus = getBus();
    //        BusFactory.setDefaultBus(bus); 
        	ClassLoader classLoader = this.getClass().getClassLoader();
        	WSLoader.loadAllWebService( classLoader);
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
        
    {
        if(bus == null)
        {
        	LOG.info("LOAD_BUS_WITHOUT_APPLICATION_CONTEXT");
        	
        	bus = WSLoader.loadBusNoConfig(servletConfig);
        }
//        ResourceManager resourceManager = (ResourceManager)bus.getExtension(org.apache.cxf.resource.ResourceManager.class);
//        resourceManager.addResourceResolver(new ServletContextResourceResolver(servletConfig.getServletContext()));
//        replaceDestinationFactory();
//        controller = createServletController(servletConfig);
    }


}
