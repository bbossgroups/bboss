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

import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProMap;

/**
 * <p>Title: SecurityManagerImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-5 下午04:33:54
 * @author biaoping.yin
 * @version 1.0
 */
public class SecurityManagerImpl implements SecurityManager
{

    private EncryptModule encrypt;
    private AuthorityModule authority;
    private LoginModule loginModule;
    private boolean enableEncrypt = false;
    private boolean enableAuthority = false;
    private boolean enableAuthenticate = false;
    public SecurityManagerImpl()
    {
        
    }
    public SecurityManagerImpl(EncryptModule encrypt,AuthorityModule authority,LoginModule loginModule)
    {
        this.encrypt = encrypt;
        this.authority = authority; 
        this.loginModule = loginModule;
        if(this.encrypt != null)
            enableEncrypt = true;
        if(this.authority != null)
            enableAuthority = true;
        if(this.loginModule != null)
            enableAuthenticate = true;
    }
    public SecurityManagerImpl(ProMap proMap)
    {
        Pro pro = proMap.getPro("rpc.authority.module");
        if(pro != null)
        {
            if(pro.getBooleanExtendAttribute("enable"))
            {
                
                enableAuthority = true;
            }
            authority = (AuthorityModule) pro.getBean();
        }
        
        pro = proMap.getPro("rpc.login.module");
        if(pro != null)
        {
            if(pro.getBooleanExtendAttribute("enable"))
            {
                
                enableAuthenticate = true;
            }
            loginModule = (LoginModule) pro.getBean();
        }
        
        pro = proMap.getPro("data.encrypt.module");
        if(pro != null)
        {
            if(pro.getBooleanExtendAttribute("enable"))
            {
                
                enableEncrypt = true;
            }
            encrypt = (EncryptModule) pro.getBean();
        }
        
    }
    public boolean checkPermission(SecurityContext context) throws Exception
    {
        if(enableAuthority)
		{
			try
			{
			    if(context == null)
	                throw new SecurityException("认证失败：没有指定用户凭证信息，SecurityContext is null."  );
				return this.authority.checkPermission(context);
			}
			catch (SecurityException e)
			{
				throw e;
			}
			catch(Exception e)
			{
				throw e;
			}
			
		}
		else
            return true;
    }

    public byte[] decode(byte[] value) throws Exception
    {
//        if(this.enableEncrypt)
            
        try
		{
        	return encrypt.decode(value);
		}
		catch (SecurityException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw e;
		}
//        return value;
    }

    public byte[] encode(byte[] value) throws Exception
    {
        
//        if(this.enableEncrypt)
           
        try
		{
        	 return encrypt.encode(value);
		}
		catch (SecurityException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw e;
		}
//        return value;
    }

    public boolean checkUser(SecurityContext context) throws Exception
    {   
        if(enableAuthenticate)
        {
            if(context == null)
                throw new SecurityException("认证失败：没有指定用户凭证信息，SecurityContext is null."  );
	        try
			{
	            
	        	return loginModule.checkUser(context);
			}
			catch (SecurityException e)
			{
				throw e;
			}
			catch(Exception e)
			{
				throw e;
			}
        }
        else
            return true;
    }
    public boolean enableAuthenticate()
    {

        return this.enableAuthenticate;
    }
    public boolean enableAuthority()
    {

        return this.enableAuthority;
    }
    public boolean enableEncrypt()
    {

        return this.enableEncrypt;
    }

}
