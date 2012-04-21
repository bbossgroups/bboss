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
package com.frameworkset.common.poolman.security;

public class DESDBUserAndPasswordEncrypt extends BaseDBInfoEncrypt{
	@Override
	public String encryptDBPassword(String password){
		// TODO Auto-generated method stub
		return encrypt(password);
	}

	@Override
	public String decryptDBPassword(String password){
		// TODO Auto-generated method stub
		return decrypt(password);
	}

	@Override
	public String encryptDBUser(String user) {
		// TODO Auto-generated method stub
		return encrypt(user);
	}

	@Override
	public String decryptDBUser(String user) {
		// TODO Auto-generated method stub
		return decrypt(user);
	}
}
