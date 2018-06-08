package org.frameworkset.spi.i18n;/*
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

import org.frameworkset.spi.support.HotDeployResourceBundleMessageSource;

import java.util.Locale;

public  class MessageUtil {
	private  static  HotDeployResourceBundleMessageSource messagesource ;
	private static void init(){
		if(messagesource == null){
			messagesource = new HotDeployResourceBundleMessageSource( );
			messagesource.setBasenames(new String[] {"hint"});
			messagesource.setUseCodeAsDefaultMessage(true);
			messagesource.setChangemonitor(true);
		}
	}
	public static String getMessage(String code ,Object ... args){
		init();
		return messagesource.getMessage(code,args,Locale.CHINA);
	}
	public static String getMessage(String code){
		init();
		return messagesource.getMessage(code,Locale.CHINA);
	}

	public static void main(String[] args){
		String msg = MessageUtil.getMessage("message.crmBackError");
		System.out.println(msg);
	}
}
