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

package org.frameworkset.spi.remote.health;

import org.junit.Test;

/**
 * <p>Title: ValidatorTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-5-16 08:42:05
 * @author biaoping.yin
 * @version 1.0
 */
public class ValidatorTest {
	
	@Test
	public void validateAuth()
	{
		RPCValidator.validator("netty","10.25.192.142","12347","admin","123456");
	}
	
	@Test
	public void validate()
	{
		RPCValidator.validator("netty","10.25.192.142","12347");
	}

}
