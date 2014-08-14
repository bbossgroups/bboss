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

package org.frameworkset.spi.security;

import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.assemble.Pro;
import org.junit.Test;

/**
 * <p>Title: SecurityTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-4 下午05:00:32
 * @author biaoping.yin
 * @version 1.0
 */
public class SecurityTest
{
    @Test
    public void testLoginModule()
    {
        Pro pro = BaseSPIManager.getMapProperty("rpc.security").getPro("rpc.login.module");
        LoginModule loginModule = (LoginModule) pro.getBean();
        boolean enable=pro.getBooleanExtendAttribute("enable");
        System.out.println("testLoginModule enable:" + enable);
        loginModule.checkUser(null);
    }
    
    @Test
    public void testAuthorityModule()
    {
        Pro pro = BaseSPIManager.getMapProperty("rpc.security").getPro("rpc.authority.module");
        
        AuthorityModule authorityModule = (AuthorityModule) pro.getBean();
        boolean enable=pro.getBooleanExtendAttribute("enable");
        System.out.println("testAuthorityModule enable:" + enable);
        authorityModule.checkPermission(null);
    }
    
    @Test
    public void testEncryptModule()
    {
        Pro pro = BaseSPIManager.getMapProperty("rpc.security").getPro("data.encrypt.module");
        boolean enable=pro.getBooleanExtendAttribute("enable");
        EncryptModule encryptModule = (EncryptModule) pro.getBean();
        System.out.println("testEncryptModule enable:" + enable);
        byte[] origine = "return encryptor.encrypt(value);".getBytes();
        byte[] envalue = encryptModule.encode(origine);
        System.out.println(envalue[0]);
        envalue = encryptModule.encode(origine);
        System.out.println(envalue[0]);
        byte[] temp = encryptModule.decode(envalue);
        String t = new String(temp);
        
        System.out.print("equals:"+t.equals("return encryptor.encrypt(value);"));
        
    }
    
    @Test
    public void testSecurityBean()
    {
        BussinessBeanInf beaninf = (BussinessBeanInf)BaseSPIManager.getBeanObject("test.security.bean");
        System.out.println("beaninf.getCount():"+beaninf.getCount());
        System.out.println("beaninf.printMessage(message):"+beaninf.printMessage("test.security.bean"));
    }
    
    @Test
    public void testMinaSecurityBean()
    {
        BussinessBeanInf beaninf = ClientProxyContext.getApplicationClientBean("(mina::172.16.17.216:12347)/test.security.bean?USERACCOUNT=admin&PASSWORD=123456",BussinessBeanInf.class);
        System.out.println("testMinaSecurityBean beaninf.getCount():"+beaninf.getCount());
        System.out.println("testMinaSecurityBean beaninf.printMessage(message):"+beaninf.printMessage("test.security.bean"));
    }
    
    @Test
    public void testJmsSecurityBean()
    {
        BussinessBeanInf beaninf = ClientProxyContext.getApplicationClientBean("(jms::yinbiaoping-jms)/test.security.bean?USERACCOUNT=admin&PASSWORD=123456",BussinessBeanInf.class);
        System.out.println("testJmsSecurityBean beaninf.getCount():"+beaninf.getCount());
        System.out.println("testJmsSecurityBean beaninf.printMessage(message):"+beaninf.printMessage("test.security.bean"));
    }
    
    @Test
    public void testJGroupSecurityBean()
    {
        BussinessBeanInf beaninf = ClientProxyContext.getApplicationClientBean("(jgroup::172.16.17.216:1186)/test.security.bean?USERACCOUNT=admin&PASSWORD=123456",BussinessBeanInf.class);
        System.out.println("testJGroupSecurityBean beaninf.getCount():"+beaninf.getCount());
        System.out.println("testJGroupSecurityBean beaninf.printMessage(message):"+beaninf.printMessage("test.security.bean"));
    }
    
    @Test
    public void testWebServiceSecurityBean()
    {
        BussinessBeanInf beaninf = ClientProxyContext.getApplicationClientBean("(webservice::http://172.16.17.216:8080/WebRoot/cxfservices)/test.security.bean?USERACCOUNT=admin&PASSWORD=123456",BussinessBeanInf.class);
        System.out.println("testJGroupSecurityBean beaninf.getCount():"+beaninf.getCount());
        System.out.println("testJGroupSecurityBean beaninf.printMessage(message):"+beaninf.printMessage("test.security.bean"));
    }
    
    
    @Test
    public void testMuticallSecurityBean()
    {
        BussinessBeanInf beaninf = ClientProxyContext.getApplicationClientBean("(mina::172.16.17.216:12346)/test.security.bean?USERACCOUNT=admin&PASSWORD=123456",BussinessBeanInf.class);
        System.out.println("testMuticallSecurityBean beaninf.getCount():"+beaninf.getCount());
        System.out.println("testMuticallSecurityBean beaninf.printMessage(message):"+beaninf.printMessage("test.security.bean"));
    }
}
