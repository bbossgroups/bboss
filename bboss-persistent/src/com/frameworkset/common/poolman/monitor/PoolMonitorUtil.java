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
package com.frameworkset.common.poolman.monitor;

import com.frameworkset.commons.pool2.impl.DefaultPooledObjectInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PoolMonitorUtil {

	public static List<AbandonedTraceExt> converAbandonedTrace(Set<DefaultPooledObjectInfo> set){
		List<AbandonedTraceExt> list = new ArrayList<AbandonedTraceExt>();
	 
		 
		try {
			 
			 
				 
				Iterator<DefaultPooledObjectInfo> iterator = set.iterator();
				for (int i = 0; iterator.hasNext(); i++) {
					DefaultPooledObjectInfo obj = iterator.next();				
						
						AbandonedTraceExt item = new AbandonedTraceExt(String
								.valueOf(i));
						item.setDburl(obj.getPooledObjectToString());
						item.setLabel("Connection-" + i);
						item.setCreateTime(obj.getCreateTime());
						 
						item.setLastUsed( obj.getLastUsedTime());
						item.setLastBorrowTime(obj.getLastBorrowTime());
						item.setLastReturnTime(obj.getLastReturnTime());
						item.setBorrowedCount(obj.getBorrowedCount());
						
						 String trace = obj.getLastBorrowTrace();
						if (trace != null && !trace.equals("")) {
							
							item.setStackInfo(trace);
							
						}
						list.add(item);
						 
					 
				}
			
		} catch (Exception e) {
			
		}
		 
		return list;
	}
	 
}
