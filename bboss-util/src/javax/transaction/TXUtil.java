package javax.transaction;/*
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

public class TXUtil {
	public static Class<?> rollbackExceptionClass = null;
	static {
		try {
			rollbackExceptionClass = Class.forName("javax.transaction.RollbackException");

		} catch (ClassNotFoundException e) {

		}
	}
	public static boolean isRollbackException(Class exceptionClass){
		if(rollbackExceptionClass == null ){
			if( exceptionClass != null){
				if(exceptionClass.getName().equals("javax.transaction.RollbackException")){
					return true;
				}
				else
					return false;
			}
			return false;
		}
		if( exceptionClass == null){
			return false;
		}
		return rollbackExceptionClass.isAssignableFrom(exceptionClass);





	}
}
