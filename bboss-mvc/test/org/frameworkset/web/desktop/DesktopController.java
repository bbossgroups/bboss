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
package org.frameworkset.web.desktop;

import java.io.IOException;

import org.frameworkset.util.annotations.ResponseBody;

/**
 * <p>Title: DesktopController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-2
 * @author biaoping.yin
 * @version 1.0
 */
public class DesktopController {
	public String desktop() 
	{
		return "path:desktop";
	}
	
	public @ResponseBody String getdata() throws IOException
	{
//		response.setContentType("text/html; charset=UTF-8");
		String ret = "[" + 
    "['3m Co多多',71.72,0.02,0.03,'9/1 12:00am']," + 
    "['Alcoa Inc',29.01,0.42,1.47,'9/1 12:00am']," + 
    " ['American Express Company',52.55,0.01,0.02,'9/1 12:00am']," + 
    "['American International Group, Inc.',64.13,0.31,0.49,'9/1 12:00am']," + 
    "['AT&T Inc.',31.61,-0.48,-1.54,'9/1 12:00am']," + 
    "['Caterpillar Inc.',67.27,0.92,1.39,'9/1 12:00am']," + 
    "['Citigroup, Inc.',49.37,0.02,0.04,'9/1 12:00am']," + 
    "['Exxon Mobil Corp',68.1,-0.43,-0.64,'9/1 12:00am']," + 
    "['General Electric Company',34.14,-0.08,-0.23,'9/1 12:00am']," + 
    "['General Motors Corporation',30.27,1.09,3.74,'9/1 12:00am']," + 
    "['Hewlett-Packard Co.',36.53,-0.03,-0.08,'9/1 12:00am']," + 
    "['Honeywell Intl Inc',38.77,0.05,0.13,'9/1 12:00am']," + 
    "['Intel Corporation',19.88,0.31,1.58,'9/1 12:00am']," + 
    "['Johnson & Johnson',64.72,0.06,0.09,'9/1 12:00am']," + 
    "['Merck & Co., Inc.',40.96,0.41,1.01,'9/1 12:00am']," + 
    "['Microsoft Corporation',25.84,0.14,0.54,'9/1 12:00am']," + 
    "['The Coca-Cola Company',45.07,0.26,0.58,'9/1 12:00am']," + 
    "['The Procter & Gamble Company',61.91,0.01,0.02,'9/1 12:00am']," + 
    "['Wal-Mart Stores, Inc.',45.45,0.73,1.63,'9/1 12:00am']," + 
    "['Walt Disney Company (The) (Holding Company)',29.89,0.24,0.81,'9/1 12:00am']" + 
"]";
	return ret;	
	}
	

}
