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
package org.frameworkset.spi.syn;

public interface AI {
	
	public void testSynInvoke(String msg)
	;
	
	public void testSynInvoke() ;
	public void testNoSynInvoke();
	
	public String testSynInvokeWithReturn();
	
	public String testSynInvokeWithException() throws Exception;
	public void testSameName();
	public void testSameName(String msg);
	
	public void testSameName1();
	public void testSameName1(String msg);
	
	public int testInt(int i);
	public int testIntNoSyn(int i);
	
	

}
