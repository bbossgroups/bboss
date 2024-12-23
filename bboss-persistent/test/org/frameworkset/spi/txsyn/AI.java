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
package org.frameworkset.spi.txsyn;

public interface AI {
	
	public void testTXSynSuccess() throws Exception;
	public void testTXSynFailed() throws Exception;
	public void testTXNoSyn() throws Exception;
	public void testNoTXSyn() throws Exception;
	public void testNoTXNoSyn() throws Exception;
	public void testWithSpecialException(int type) throws Exception;

}
