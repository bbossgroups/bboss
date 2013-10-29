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

package org.frameworkset.tlq.converter;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsBinding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Title: TLQJmsBinding.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-13 上午11:12:10
 * @author biaoping.yin
 * @version 1.0
 */
public class TLQJmsBinding extends JmsBinding {
	private static final transient Log LOG = LogFactory.getLog(TLQJmsBinding.class);

	/* (non-Javadoc)
	 * @see org.apache.camel.component.jms.JmsBinding#appendJmsProperty(javax.jms.Message, org.apache.camel.Exchange, org.apache.camel.Message, java.lang.String, java.lang.Object)
	 */
	@Override
	public void appendJmsProperty(Message jmsMessage, Exchange exchange,
			org.apache.camel.Message in, String headerName, Object headerValue)
			throws JMSException {
//		if(headerName.equals("JMS_TLQ_Encrypted") || headerName.equals("JMS_TLQ_CheckQue") || headerName.equals(" JMS_TLQ_Compressed") )
//        {
//        	 String key = super.encodeToSafeJmsHeaderName(headerName);
//        	Object value = getValidJMSHeaderValue(headerName, headerValue);
//            if (value != null) {
//                jmsMessage.setObjectProperty(key, value);
//            } else if (LOG.isDebugEnabled()) {
//                // okay the value is not a primitive or string so we can not sent it over the wire
//                LOG.debug("Ignoring non primitive header: " + headerName + " of class: "
//                    + headerValue.getClass().getName() + " with value: " + headerValue);
//            }
//        }
		super.appendJmsProperty( jmsMessage,  exchange,
			 in,  headerName,  headerValue);
	}
	
	
	

}
