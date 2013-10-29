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

package org.frameworkset.camel;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;
import static org.frameworkset.tlq.TLQComponent.tlqComponent;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

/**
 * <p>Title: TestTLQComponent.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-14 下午09:53:30
 * @author biaoping.yin
 * @version 1.0
 */
public class TestTLQComponent {
	@Test
	public void test() throws Exception
	{
		CamelContext context = createCamelContext() ;
		context.addRoutes(createRouteBuilder());
//		createRouteBuilder()
		context.start();
	}
	protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = new DefaultCamelContext();

        // START SNIPPET: example
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));
        camelContext.addComponent("tlq", tlqComponent("tlkq://172.16.17.216:10241/","system","manager"));
        // END SNIPPET: example

        return camelContext;
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("tlq").to("activemq:queue:test.b");
                from("activemq:queue:test.b").to("mock:result");

                
            }
        };
    }
    
    public static void main(String[] args) throws Exception
    {
    	TestTLQComponent test = new TestTLQComponent();
    	test.test();
    }
}
