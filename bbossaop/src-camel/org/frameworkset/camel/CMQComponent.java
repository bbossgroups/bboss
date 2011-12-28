package org.frameworkset.camel;

import javax.jms.ConnectionFactory;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.CamelContext;
import org.apache.camel.util.ObjectHelper;

public class CMQComponent extends ActiveMQComponent {
	private CMQEndpointLoader endpointLoader;

	public CMQComponent(ActiveMQConfiguration configuration) {
		super(configuration);
		// TODO Auto-generated constructor stub
	}

	public CMQComponent(CamelContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// private CamelEndpointLoader endpointLoader;
	public CMQComponent() {
		super();
	}

	public static CMQComponent cmqComponent() {
		return new CMQComponent();
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
	public static CMQComponent cmqComponent(ConnectionFactory connectionFactory) {
		CMQComponent answer = new CMQComponent();
		if (answer.getConfiguration() instanceof ActiveMQConfiguration) {
			((ActiveMQConfiguration) answer.getConfiguration())
					.setConnectionFactory(connectionFactory);
		}
		return answer;
	}

	@Override
	protected void doStart() throws Exception {
		ObjectHelper.notNull(getCamelContext(), "camelContext");
		if (isExposeAllQueues()) {
			endpointLoader = new CMQEndpointLoader(getCamelContext());
			endpointLoader.afterPropertiesSet();
		}
	}

	@Override
	protected void doStop() throws Exception {
		if (endpointLoader != null) {
			endpointLoader.destroy();
			endpointLoader = null;
		}
		super.doStop();
	}

}
