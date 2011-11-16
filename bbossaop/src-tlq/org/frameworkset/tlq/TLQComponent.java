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

package org.frameworkset.tlq;

import java.util.Map;

import javax.jms.ConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsEndpoint;
import org.frameworkset.tlq.converter.TLQJmsBinding;

/**
 * <p>Title: TLQComponent.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-13 ÉÏÎç11:04:28
 * @author biaoping.yin
 * @version 1.0
 */
public class TLQComponent extends JmsComponent {
	private TLQJmsBinding jmsBinding = new TLQJmsBinding(); 
	public TLQComponent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TLQComponent(CamelContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TLQComponent(JmsConfiguration configuration) {
		super(configuration);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Creates an <a
	 * href="http://activemq.apache.org/camel/activemq.html">ActiveMQ
	 * Component</a> connecting to the given <a
	 * href="http://activemq.apache.org/configuring-transports.html">broker
	 * URL</a>
	 * 
	 * @param brokerURL
	 *            the URL to connect to
	 * @return the created component
	 */
	public static TLQComponent tlqComponent(ConnectionFactory connectionFactory) {
		TLQComponent answer = new TLQComponent();
		answer.getConfiguration().setConnectionFactory(connectionFactory);
		
		return answer;
	}
	
	public static TLQComponent tlqComponent(String connectURI, String username, String password) throws Exception {
		TLQComponent answer = new TLQComponent();
		TLQConnectionFactory factory = new TLQConnectionFactory(connectURI, username, password);
		answer.getConfiguration().setConnectionFactory(factory.buildConnectionFactory());
		
		return answer;
	}
	
//	public static ?
	/**
	 * String connectURI, String username, String password
	 */

	/* (non-Javadoc)
	 * @see org.apache.camel.component.jms.JmsComponent#createEndpoint(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	protected Endpoint createEndpoint(String uri,
			String remaining, Map parameters) throws Exception {
		// TODO Auto-generated method stub
		JmsEndpoint endpoint = (JmsEndpoint)super.createEndpoint(uri, remaining, parameters);
		endpoint.setBinding(jmsBinding);
		return endpoint;
	}

}
