package org.frameworkset.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;

/**
 * 
 * <p>
 * Title: JMSProperties.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-8-10 ÉÏÎç11:09:49
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSProperties
{
    private Map<String, HeaderProperty> headers;
    

    private List<HeaderProperty> listheaders;

    private int pro_id;

    public JMSProperties()
    {
        listheaders = new ArrayList<HeaderProperty>();
        headers = new HashMap<String, HeaderProperty>();
    }

    public int getPro_id()
    {

        return pro_id;
    }

    public void setPro_id(int pro_id)
    {

        this.pro_id = pro_id;
    }

    public List<HeaderProperty> getListheaders()
    {
        return listheaders;
    }

    public void setListheaders(List<HeaderProperty> listheaders)
    {
        if (listheaders != null)
            this.listheaders = listheaders;

    }
    
    public String getJmsProvider()
    {
    	HeaderProperty JMSProvider = headers.get(JMSConnectionFactory.MQProvider_name);
    	return JMSProvider == null ? JMSConnectionFactory.JMSProvider_ACTIVEMQ : JMSProvider.getVALUE_string();
    }

    public boolean isCompress()
    {
        HeaderProperty iszip = headers.get("isZip");
        return iszip == null ? false : iszip.isVALUE_BOOLEAN();
    }

    public boolean isEncrypt()
    {
        HeaderProperty isEncrypt = headers.get("isEncrypt");
        return isEncrypt == null ? false : isEncrypt.isVALUE_BOOLEAN();

    }

    public String getJMSCorrelationID()
    {
        HeaderProperty JMSCorrelationID = headers.get("JMSCorrelationID");
        return JMSCorrelationID == null ? null : JMSCorrelationID.getVALUE_string();
        // return JMSCorrelationID;
    }

    public String getJMSMessageID()
    {
        HeaderProperty JMSMessageID = headers.get("JMSMessageID");
        return JMSMessageID == null ? null : JMSMessageID.getVALUE_string();
    }

    public Destination getJMSDestination()
    {

        HeaderProperty JMSDestination = headers.get("JMSDestination");
        return JMSDestination == null ? null : (Destination) JMSDestination.getVALUE_OBJECT();
    }

    public Destination getJMSReplyTo()
    {

        HeaderProperty JMSReplyTo = headers.get("JMSReplyTo");
        return JMSReplyTo == null ? null : (Destination)JMSReplyTo.getVALUE_OBJECT();
    }

    public String getJMSType()
    {

        HeaderProperty JMSType = headers.get("JMSType");
        return JMSType == null ? null : JMSType.getVALUE_string();
    }

    public void addHeader(HeaderProperty p)
    {
        this.listheaders.add(p);
        this.headers.put(p.getPRO_NAME(), p);
    }

    // private String JMSCorrelationID;

    public Map<String, HeaderProperty> getHeaders()
    {
        return this.headers;
    }
    
    public HeaderProperty getHeadProperty(String name)
    {
        return this.getHeaders().get(name);
    }

    public void setHeaders(Map<String, HeaderProperty> headers)
    {
        if (headers != null)
            this.headers = headers;
    }

    public void addProperty(String name, Object value)
    {
        if (value != null)
        {
            HeaderProperty p = new HeaderProperty(name, value);
            this.addHeader(p);
        }
    }
    
    public short getShortProperty(String arg0) throws JMSException
    {
        
        return getHeaders().get(arg0).getVALUE_short();
        
    }

    public String getStringProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).getVALUE_string();
        
       
    }
    
    public long getLongProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).getVALUE_long();
        
        
    }

    public Object getObjectProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).getObject();
        
        
    }
    
    public boolean getBooleanProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).isVALUE_BOOLEAN();
        
        
    }

    public byte getByteProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).getVALUE_byte();
        
        
    }

    public double getDoubleProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).getVALUE_double();
        
        
    }

    public float getFloatProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).getVALUE_float();
        
        
    }

    public int getIntProperty(String arg0) throws JMSException
    {
        return getHeaders().get(arg0).getVALUE_int();
        
        
    }
}
