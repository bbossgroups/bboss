/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.app.Velocity;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.velocity.BBossVelocityUtil;


/**
 * This class is a simple demonstration of how the Velocity Template Engine
 * can be used in a standalone application.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: Example.java 463298 2006-10-12 16:10:32Z henning $
 */

public class Example
{
	private static final Logger log = Logger.getLogger(Example.class);
    public Example(String templateFile)
    {
        try
        {
//            /*
//             * setup
//             */
//
//            Velocity.init("./velocity.properties");

            /*
             *  Make a context object and populate with the data.  This
             *  is where the Velocity engine gets the data to resolve the
             *  references (ex. $list) in the template
             */
        	init(null);
            VelocityContext context = new VelocityContext();
            context.put("list", getNames());

            /*
             *  get the Template object.  This is the parsed version of your
             *  template input file.  Note that getTemplate() can throw
             *   ResourceNotFoundException : if it doesn't find the template
             *   ParseErrorException : if there is something wrong with the VTL
             *   Exception : if something else goes wrong (this is generally
             *        indicative of as serious problem...)
             */

            Template template =  null;

            try
            {
                template = Velocity.getTemplate(templateFile);
            }
            catch( ResourceNotFoundException rnfe )
            {
                System.out.println("Example : error : cannot find template " + templateFile );
            }
            catch( ParseErrorException pee )
            {
                System.out.println("Example : Syntax error in template " + templateFile + ":" + pee );
            }

            /*
             *  Now have the template engine process your template using the
             *  data placed into the context.  Think of it as a  'merge'
             *  of the template and the data to produce the output stream.
             */

            BufferedWriter writer = writer = new BufferedWriter(
                new OutputStreamWriter(System.out));

            if ( template != null)
                template.merge(context, writer);
            template = Velocity.getTemplate("example1.vm");
            template.merge(context, writer);
            
            /*
             *  flush and cleanup
             */

            writer.flush();
            writer.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public static  void init(String approot)
    {
    	
    	 {
    		 
	    	 try {
	    		
	    	        
	    	       java.util.Properties pros =SimpleStringUtil.getProperties("/bboss-velocity.properties", BBossVelocityUtil.class);

	             
	
	             //初始化velocity
	             try { 
	
	            	 Velocity.init(pros);
	                
	
	             } catch (Exception ex) {
	             	try {
	             		ex.printStackTrace();
	 					Velocity.init("bboss-velocity.properties");
	 					
	 					log.error("Init velocity failed velocity.properties："+ ex.getMessage(),ex) ;
	 				} catch (Exception e) {
	 				
	 					log.error("Init velocity failed velocity.properties "+ ex.getMessage(),e);
	 				}
	                
	             }
	            
	         } catch (Exception e) {
	        	
	             log.error("Init velocity failed:velocity.properties :"+ e.getMessage(),e);
	         }
    	 }
    	 
    	 
    }

    public ArrayList getNames()
    {
        ArrayList list = new ArrayList();

        list.add("ArrayList element 1");
        list.add("ArrayList element 2");
        list.add("ArrayList element 3");
        list.add("ArrayList element 4");

        return list;
    }

    public static void main(String[] args)
    {
        Example t = new Example("example.vm");
    }
}
