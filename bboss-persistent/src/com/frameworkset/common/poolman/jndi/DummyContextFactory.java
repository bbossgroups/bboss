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
 *  
 *  http://blog.csdn.net/yin_bp
 *  http://yin-bp.javaeye.com/
 */
package com.frameworkset.common.poolman.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * 
 * <p>Title: DummyContextFactory.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2009-4-2 下午03:57:12
 * @author biaoping.yin
 * @version 1.0
 */
public class DummyContextFactory implements InitialContextFactory {

   static Context instance=null;

   public DummyContextFactory() {
      ;
   }

   /**
    * Creates an Initial Context for beginning name resolution.
    * Special requirements of this context are supplied
    * using <code>environment</code>.
    * <p/>
    * The environment parameter is owned by the caller.
    * The implementation will not modify the object or keep a reference
    * to it, although it may keep a reference to a clone or copy.
    *
    * @param environment The possibly null environment
    *                    specifying information to be used in the creation
    *                    of the initial context.
    * @return A non-null initial context object that implements the Context
    *         interface.
    * @throws NamingException If cannot create an initial context.
    */
   public Context getInitialContext(Hashtable environment) throws NamingException {
      if(instance == null)
         instance=new DummyContext();
      return instance;
   }
}
