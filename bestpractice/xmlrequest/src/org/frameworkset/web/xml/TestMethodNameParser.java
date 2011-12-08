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
package org.frameworkset.web.xml;

import java.lang.reflect.Method;

import org.frameworkset.spi.support.LocalVariableTableParameterNameDiscoverer;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.junit.Test;

/**
 * <p>Title: TestMethodNameParser.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-12-8
 * @author biaoping.yin
 * @version 1.0
 */
public class TestMethodNameParser {
	@Test
	public void testClassInfo() throws SecurityException, NoSuchMethodException
	{
//		Perl5Compiler s;
//		s.compile(arg0);
		LocalVariableTableParameterNameDiscoverer r = new LocalVariableTableParameterNameDiscoverer();
		Method method = ClassInfo.class.getMethod("getDeclaredMethod", String.class);
		String[] names = r.getParameterNames(method);
		System.out.println(names);
	}

}
