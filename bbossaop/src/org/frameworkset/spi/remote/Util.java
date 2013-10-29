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

package org.frameworkset.spi.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.remote.serializable.Decoder;
import org.frameworkset.spi.remote.serializable.Encoder;
import org.frameworkset.spi.remote.serializable.SOADecoder;
import org.frameworkset.spi.remote.serializable.SOAEncoder;
import org.frameworkset.spi.serviceidentity.TargetImpl;





/**
 * <p>Title: Util.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-8 下午11:20:00
 * @author biaoping.yin
 * @version 1.0
 */
public class Util
{



  
    static boolean      JGROUPS_COMPAT=false;
    
    /**
     * Serializes/Streams an object into a byte buffer.
     * The object has to implement interface Serializable or Externalizable
     * or Streamable.  Only Streamable objects are interoperable w/ jgroups-me
     */
    public static byte[] oldObjectToByteBuffer(Object obj) throws Exception {
        byte[] result=null;
        ObjectOutputStream out= null;
        ByteArrayOutputStream out_stream = null;
        try
        {
        	out_stream=new ByteArrayOutputStream();
        
            out=new ObjectOutputStream(out_stream);
            out.writeObject(obj);
            
            result=out_stream.toByteArray();
            return result;
        }
        catch(Exception e)
        {
        	throw e;
        }
        finally
        {
        	try
			{
				if (out_stream != null)
					out_stream.close();
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
			try
			{
				if (out != null)
					out.close();
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
        	
        }
        
    }
    
//    public static void writeGenericStreamable(Streamable obj, DataOutputStream out) throws IOException {
//        short magic_number;
//        String classname;
//
//        if(obj == null) {
//            out.write(0);
//            return;
//        }
//
//        out.write(1);
//        magic_number=ClassConfigurator.getMagicNumber(obj.getClass());
//        // write the magic number or the class name
//        if(magic_number == -1) {
//            out.writeBoolean(false);
//            classname=obj.getClass().getName();
//            out.writeUTF(classname);
//        }
//        else {
//            out.writeBoolean(true);
//            out.writeShort(magic_number);
//        }
//
//        // write the contents
//        obj.writeTo(out);
//    }
	/**
	 * 
     * Serializes/Streams an object into a byte buffer.
     * The object has to implement interface Serializable or Externalizable
     * or Streamable.  Only Streamable objects are interoperable w/ jgroups-me
     */
    public static byte[] objectToByteBuffer(Object obj) throws Exception {
         return oldObjectToByteBuffer(obj);
    }
    
    public static void close(OutputStream out) {
        if(out != null) {
            try {out.close();} catch(IOException e) {}
        }
    }
    public static void close(InputStream inp) {
        if(inp != null)
            try {inp.close();} catch(IOException e) {}
    }
    public static Object objectFromByteBuffer(byte[] buffer, int offset, int length) throws Exception {
        if(buffer == null) return null;
        
        return oldObjectFromByteBuffer(buffer, offset, length);
        
    }
    
    public static Object oldObjectFromByteBuffer(byte[] buffer, int offset, int length) throws Exception {
        if(buffer == null) return null;
        Object retval=null;

        try {  // to read the object as an Externalizable
            ByteArrayInputStream in_stream=new ByteArrayInputStream(buffer, offset, length);
            ObjectInputStream in=new ObjectInputStream(in_stream); // changed Nov 29 2004 (bela)
            retval=in.readObject();
            in.close();
        }
        catch(StreamCorruptedException sce) {
//            try {  // is it Streamable?
//                ByteArrayInputStream in_stream=new ByteArrayInputStream(buffer, offset, length);
//                DataInputStream in=new DataInputStream(in_stream);
//                retval=readGenericStreamable(in);
//                in.close();
//            }
//            catch(Exception ee) {
                IOException tmp=new IOException("unmarshalling failed");
                tmp.initCause(sce);
                throw tmp;
//            }
        }

        if(retval == null)
            return null;
        return retval;
    }
    
//    public static Streamable readGenericStreamable(DataInputStream in) throws IOException {
//        Streamable retval=null;
//        int b=in.read();
//        if(b == 0)
//            return null;
//
//        boolean use_magic_number=in.readBoolean();
//        String classname;
//        Class clazz;
//
//        try {
//            if(use_magic_number) {
//                short magic_number=in.readShort();
//                clazz=ClassConfigurator.get(magic_number);
//                if (clazz==null) {
//                   throw new ClassNotFoundException("Class for magic number "+magic_number+" cannot be found.");
//                }
//            }
//            else {
//                classname=in.readUTF();
//                clazz=ClassConfigurator.get(classname);
//                if (clazz==null) {
//                   throw new ClassNotFoundException(classname);
//                }
//            }
//
//            retval=(Streamable)clazz.newInstance();
//            retval.readFrom(in);
//            return retval;
//        }
//        catch(Exception ex) {
//            throw new IOException("failed reading object: " + ex.toString());
//        }
//    }
    /**
     * Tries to load the class from the current thread's context class loader. If
     * not successful, tries to load the class from the current instance.
     * @param classname Desired class.
     * @param clazz Class object used to obtain a class loader
     * 				if no context class loader is available.
     * @return Class, or null on failure.
     */
    public static Class loadClass(String classname, Class clazz) throws ClassNotFoundException {
        ClassLoader loader;

        try {
            loader=Thread.currentThread().getContextClassLoader();
            if(loader != null) {
                return loader.loadClass(classname);
            }
        }
        catch(Throwable t) {
        }

        if(clazz != null) {
            try {
                loader=clazz.getClassLoader();
                if(loader != null) {
                    return loader.loadClass(classname);
                }
            }
            catch(Throwable t) {
            }
        }

        try {
            loader=ClassLoader.getSystemClassLoader();
            if(loader != null) {
                return loader.loadClass(classname);
            }
        }
        catch(Throwable t) {
        }

        throw new ClassNotFoundException(classname);
    }

    
    /**
     * Returns a value associated wither with one or more system properties, or found in the props map
     * @param system_props
     * @param props List of properties read from the configuration file
     * @param prop_name The name of the property, will be removed from props if found
     * @param ignore_sysprops If true, system properties are not used and the values will only be retrieved from
     * props (not system_props)
     * @param default_value Used to return a default value if the properties or system properties didn't have the value
     * @return The value, or null if not found
     */
    public static String getProperty(String[] system_props, Properties props, String prop_name,
                                     boolean ignore_sysprops, String default_value) {
        String retval=null;
        if(props != null && prop_name != null) {
            retval=props.getProperty(prop_name);
            props.remove(prop_name);
        }

        if(!ignore_sysprops) {
            String tmp, prop;
            if(system_props != null) {
                for(int i=0; i < system_props.length; i++) {
                    prop=system_props[i];
                    if(prop != null) {
                        try {
                            tmp=System.getProperty(prop);
                            if(tmp != null)
                                return tmp; // system properties override config file definitions
                        }
                        catch(SecurityException ex) {}
                    }
                }
            }
        }
        if(retval == null)
            return default_value;
        return retval;
    }
    
    
   
   
   
   public static InputStream getResourceAsStream(String name, Class clazz) {
       ClassLoader loader;
       InputStream retval=null;

       try {
           loader=Thread.currentThread().getContextClassLoader();
           if(loader != null) {
               retval=loader.getResourceAsStream(name);
               if(retval != null)
                   return retval;
           }
       }
       catch(Throwable t) {
       }

       if(clazz != null) {
           try {
               loader=clazz.getClassLoader();
               if(loader != null) {
                   retval=loader.getResourceAsStream(name);
                   if(retval != null)
                       return retval;
               }
           }
           catch(Throwable t) {
           }
       }
       
       try {
           loader=ClassLoader.getSystemClassLoader();
           if(loader != null) {
               return loader.getResourceAsStream(name);
           }
       }
       catch(Throwable t) {
       }

       return retval;
   }
//   private static Map<String,RPCIOHandler> handlers = new HashMap<String,RPCIOHandler>();   
   
   
   private static String RPCCallServicePort = "RPCCallServicePort";
   
   public static String rpc_mina_RPCServerIoHandler = "rpc.mina.RPCServerIoHandler";
   public static String rpc_netty_RPCServerIoHandler = "rpc.netty.RPCServerIoHandler";
   public static String rpc_webservice_RPCCall = "rpc.webservice.RPCCall";
   public static String rpc_webservice_RPCServerIoHandler = "rpc.webservice.RPCServerIoHandler";
   public static String rpc_jms_RPCServerIoHandler = "rpc.jms.RPCServerIoHandler";
   public static String rpc_rmi_RPCServerIoHandler = "rpc.rmi.RPCServerIoHandler";
   public static String rpc_http_RPCServerIoHandler = "rpc.http.RPCServerIoHandler";
   
   
   
   
   public static String getRPCCallServicePort()
   {
       return RPCCallServicePort;
   }
  
   
//   public static RPCIOHandler minaIPHandler = (RPCIOHandler)BaseSPIManager.getBeanObject(Util.rpc_mina_RPCServerIoHandler);
//   public static RPCIOHandler webserviceIPHandler = (RPCIOHandler)BaseSPIManager.getBeanObject(Util.rpc_webservice_RPCServerIoHandler);
//   public static RPCIOHandler jmsIPHandler = (RPCIOHandler)BaseSPIManager.getBeanObject(Util.rpc_jms_RPCServerIoHandler);
   
   public static RPCIOHandler getRPCIOHandler(String protocol)
   {
       
	   if(Target.BROADCAST_TYPE_MINA.equals(protocol))
		   return (RPCIOHandler)defaultContext.getBeanObject(Util.rpc_mina_RPCServerIoHandler);
	   if(Target.BROADCAST_TYPE_NETTY.equals(protocol))
           return (RPCIOHandler)defaultContext.getBeanObject(Util.rpc_netty_RPCServerIoHandler);
	   else if(Target.BROADCAST_TYPE_WEBSERVICE.equals(protocol))
		   return (RPCIOHandler)defaultContext.getBeanObject(Util.rpc_webservice_RPCServerIoHandler);
	   else if(Target.BROADCAST_TYPE_JMS.equals(protocol))
		   return (RPCIOHandler)defaultContext.getBeanObject(Util.rpc_jms_RPCServerIoHandler);
	   else if(Target.BROADCAST_TYPE_RMI.equals(protocol))
		   return (RPCIOHandler)defaultContext.getBeanObject(Util.rpc_rmi_RPCServerIoHandler);
	   else if(Target.BROADCAST_TYPE_HTTP.equals(protocol))
		   return (RPCIOHandler)defaultContext.getBeanObject(Util.rpc_http_RPCServerIoHandler);
	   else
		   throw new java.lang.UnsupportedOperationException("protocol[" + protocol+"] not implmented.");
		   
   }
   

   private static Map<String ,List<RPCAddress>> allAddress = new HashMap<String ,List<RPCAddress>>();
//   static
//   {
//	   allAddress = buildAllAddress();
//   }
   
   private static List<RPCAddress> buildAllAddress(String protocol)
	{
		String temps = rpc_all_servers;
		if(temps != null && !temps.equals(""))
		{
			return java.util.Collections.unmodifiableList(TargetImpl.buildAllTargets(temps,protocol));
		}
		return null;
	}
   
   /**
    * 需要进行特殊处理
    * @param protocol
    * @return
    */
   public static List<RPCAddress> getAllAddress(String protocol)
	{
	   List<RPCAddress> temp = allAddress.get(protocol);
	   if(temp != null)
		   return temp;
	   synchronized(allAddress)
	   {
		   temp = allAddress.get(protocol);
		   if(temp != null)
			   return temp;
		   temp = buildAllAddress(protocol);
		   allAddress.put(protocol, temp);
	   }
	   return temp;
	}
   public static BaseApplicationContext defaultContext = null;
   public static String rpc_all_servers ;
   
   public static String rpc_startup_mode ;
   
   public static String default_protocol ;
   public static String rpc_startup_protocols ;
   
   public static String clusterName ;
   public static String jgroupprotocol ;
   
   static
   {
	   init();
   }
   private static void init()
   {
	   defaultContext = ApplicationContext.getApplicationContext();
	   rpc_all_servers = defaultContext.getProperty("rpc.all.servers");
	   
	   rpc_startup_mode = defaultContext.getProperty("rpc.startup.mode","mannual");
	   
	   default_protocol = defaultContext.getProperty("rpc.default.protocol",Target.BROADCAST_TYPE_MINA);
	   rpc_startup_protocols = defaultContext.getProperty("rpc.startup.protocols");
	   
	   clusterName = defaultContext.getProperty("cluster_name", "Cluster");
	   jgroupprotocol = defaultContext.getProperty("cluster_protocol", "udp");
   }
  
   public static String getProtocolConfigFile()
   {
       String key = "cluster_protocol." + jgroupprotocol + ".configfile" ;
       String conf = defaultContext.getProperty(key, "etc/META-INF/replSync-service-aop.xml");
       return conf;
       
   }
   
   private static Encoder encoder = new SOAEncoder();
   public static Encoder getEncoder()
   {
	   return encoder;
   }
   
   
   private static Decoder decoder = new SOADecoder();
   public static Decoder getDecoder()
   {
	   return decoder;
   }
   
  
   
//   public static final String SYN_MODE = "syn";
//   public static final String ASYN_MODE = "asyn";
//   public static String rpc_response_mode = ApplicationContext.getApplicationContext().getProperty("rpc.response.mode",SYN_MODE);
//   public static String rpc_request_mode = ApplicationContext.getApplicationContext().getProperty("rpc.request.mode",ASYN_MODE);
//   
//   public static boolean asyn_request = Util.rpc_request_mode.equals(Util.ASYN_MODE);
//   public static boolean asyn_response = Util.rpc_response_mode.equals(Util.ASYN_MODE);
   
   

}
