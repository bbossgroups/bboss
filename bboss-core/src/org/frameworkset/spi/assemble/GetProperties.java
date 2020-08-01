package org.frameworkset.spi.assemble;/*
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

import java.util.Map;

public interface GetProperties {
	public String getExternalProperty(String property);
	public String getSystemEnvProperty(String property);
	public String getExternalProperty(String property,String defaultValue);
	public Object getExternalObjectProperty(String property);
	public Object getExternalObjectProperty(String property,Object defaultValue);
	public boolean getExternalBooleanProperty(String property,boolean defaultValue);
//	public String getProperty(String property);
	public String getExternalPropertyWithNS(String namespace,String property);
	public String getExternalPropertyWithNS(String namespace,String property,String defaultValue);
	public Object getExternalObjectPropertyWithNS(String namespace,String property);
	public Object getExternalObjectPropertyWithNS(String namespace,String property,Object defaultValue);
//	public boolean getBooleanProperty(String property,boolean defaultValue);
//	public Map getAllProperties();
    public Map getAllExternalProperties();
}
