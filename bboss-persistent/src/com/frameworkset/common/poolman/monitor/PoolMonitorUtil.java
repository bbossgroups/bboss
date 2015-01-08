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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.commons.dbcp.AbandonedTrace;

public class PoolMonitorUtil {

	public static List<AbandonedTraceExt> converAbandonedTrace(List objects){
		List<AbandonedTraceExt> list = new ArrayList<AbandonedTraceExt>();
		java.io.StringWriter writer = null;
		java.io.PrintWriter pwriter = null;
		AbandonedTrace trace = null;
		Exception stack = null;
		try {
			writer = new StringWriter();
			pwriter = new PrintWriter(writer);
			if (objects != null) {
				for (int i = 0; i < objects.size(); i++) {
					Object obj = objects.get(i);

					if (obj instanceof AbandonedTrace) {
						trace = (AbandonedTrace) obj;
						
						
						AbandonedTraceExt item = new AbandonedTraceExt(String
								.valueOf(i));
						item.setDburl(trace.getDbURLinfo());
						item.setLabel("Connection-" + i);
						item.setCreateTime(trace.getCreateTime());
						item.setParent(null);
						item.setLastUsed(trace.getLastUsed());
						
						 
						
						stack = trace.getCreateBy();
						if (stack != null) {
							trace.getCreateBy().printStackTrace(pwriter);
							item.setStackInfo(writer.getBuffer().toString());
							writer.getBuffer().setLength(0);
						}
						list.add(item);
						List statms = trace.getTraces();
						for (int j = 0; j < statms.size(); j++) {
							obj = statms.get(j);

							if (obj instanceof AbandonedTrace) {
								trace = (AbandonedTrace) obj;
								AbandonedTraceExt statmTrace = new AbandonedTraceExt(
										item.getStackid() + "-" + j);
								statmTrace.setLabel("Statement-" + j);
								statmTrace.setCreateTime(trace.getCreateTime());
								statmTrace.setParent(item);
								statmTrace.setLastUsed(trace.getLastUsed());
								stack = trace.getCreateBy();
								if (stack != null) {
									trace.getCreateBy()
											.printStackTrace(pwriter);
									statmTrace.setStackInfo(writer.getBuffer()
											.toString());
									writer.getBuffer().setLength(0);
								}
								item.addTrace(statmTrace);
								List results = trace.getTraces();
								for (int k = 0; k < results.size(); k++) {
									obj = results.get(k);

									if (obj instanceof AbandonedTrace) {
										trace = (AbandonedTrace) obj;
										AbandonedTraceExt resultTrace = new AbandonedTraceExt(
												statmTrace.getStackid() + "-"
														+ k);
										resultTrace.setCreateTime(trace
												.getCreateTime());
										resultTrace.setLastUsed(trace.getLastUsed());
										resultTrace.setLabel("ResultSet-" + k);
										resultTrace.setParent(statmTrace);
										stack = trace.getCreateBy();
										if (stack != null) {
											trace.getCreateBy()
													.printStackTrace(pwriter);
											resultTrace.setStackInfo(writer
													.getBuffer().toString());
											writer.getBuffer().setLength(0);
										}
										statmTrace.addTrace(resultTrace);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
		finally
		{
			if(writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(pwriter != null)
				try {
					pwriter.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
		}
		return list;
	}
}
