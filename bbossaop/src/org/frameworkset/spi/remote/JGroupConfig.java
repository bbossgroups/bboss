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

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
/**
 * 
 * <p>Title: JGroupConfig.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Apr 24, 2009 10:49:35 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class JGroupConfig {
	private static final Logger log = Logger.getLogger(JGroupConfig.class);
	protected String cluster_props = null;
	
	/**
	    * Convert a list of elements to the JG property string
	    */
	   public void setClusterConfig(Element config)
	   {
		   
	      StringBuffer buffer = new StringBuffer();
	      NodeList stack = config.getChildNodes();
	      int length = stack.getLength();

	      for (int s = 0; s < length; s++)
	      {
	         org.w3c.dom.Node node = stack.item(s);
	         if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
	            continue;

	         Element tag = (Element) node;
	         String protocol = tag.getTagName();
	         buffer.append(protocol);
	         NamedNodeMap attrs = tag.getAttributes();
	         int attrLength = attrs.getLength();
	         if (attrLength > 0)
	            buffer.append('(');
	         for (int a = 0; a < attrLength; a++)
	         {
	            Attr attr = (Attr) attrs.item(a);
	            String name = attr.getName();
	            String value = attr.getValue();
	            buffer.append(name);
	            buffer.append('=');
	            buffer.append(value);
	            if (a < attrLength - 1)
	               buffer.append(';');
	         }
	         if (attrLength > 0)
	            buffer.append(')');
	         buffer.append(':');
	      }
	      // Remove the trailing ':'
	      buffer.setLength(buffer.length() - 1);
	      this.cluster_props = buffer.toString();
	      log.info("setting cluster properties from xml to: " + cluster_props);
	   }

	public String getClusterProperties() {
		return cluster_props;
	}



}
