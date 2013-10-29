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

package org.frameworkset.spi.support;

import java.util.Locale;

import org.junit.Test;

/**
 * <p>Title: TestHotDeployResourceBundleMessageSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-5-6 下午10:35:22
 * @author biaoping.yin
 * @version 1.0
 */
public class TestHotDeployResourceBundleMessageSource {
	@Test
	public void testHotDeployResourceBundleMessageSource()
	{
		HotDeployResourceBundleMessageSource messagesource = new HotDeployResourceBundleMessageSource( );
		messagesource.setBasenames(new String[] {"org/frameworkset/spi/support/messages"});
		messagesource.setUseCodeAsDefaultMessage(true);
		System.out.println(messagesource.getMessage("probe.jsp.generic.abbreviations",  Locale.US));
		System.out.println(messagesource.getMessage("probe.jsp.generic.abbreviations",  Locale.US));
	}
	@Test
	public void test()
	{
		HotDeployResourceBundleMessageSource messageSource = (HotDeployResourceBundleMessageSource)MessageSourceUtil.
				getMessageSource("org/frameworkset/spi/support/messages");
		System.out.println(messageSource.getMessage("sany.pdp.module.personcenter",  Locale.US));
		try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(messageSource.getMessage("sany.pdp.module.personcenter",  Locale.US));
	}

}
