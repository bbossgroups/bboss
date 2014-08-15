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

package org.frameworkset.spi;

import java.util.HashMap;

/**
 * <p>Title: UtilMap.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-5-5 下午04:00:45
 * @author biaoping.yin
 * @version 1.0
 */
public class UtilMap extends HashMap
{
    public int getInt(String key)
    {            
        Object value = this.get(key);
        if(value == null)
            return 0;
        int value_ = Integer.parseInt(value.toString());
        return value_;
    }
    
    public long getLong(String key)
    {            
        Object value = this.get(key);
        if(value == null)
            return 0;
        long value_ = Integer.parseInt(value.toString());
        return value_;
//        return value.getLong();
    }
    
    public int getInt(String key,int defaultValue)
    {
        Object value = this.get(key);
        if(value == null)
            return defaultValue;
        int value_ = Integer.parseInt(value.toString());
        return value_;
    }
    
    
    public long getLong(String key,long defaultValue)
    {
        Object value = this.get(key);
        if(value == null)
            return defaultValue;
        long value_ = Long.parseLong(value.toString());
        return value_;
    }
    
    
    
    public boolean getBoolean(String key)
    {
        Object value = this.get(key);
        if(value == null)
            return false;
        boolean value_ = Boolean.parseBoolean(value.toString());
        return value_;
    }
    public boolean getBoolean(String key,boolean defaultValue)
    {
        Object value = this.get(key);
        if(value == null)
            return defaultValue;
        boolean value_ =  Boolean.parseBoolean(value.toString());
        return value_;
    }
    
    public String getString(String key)
    {
        Object value = this.get(key);
        if(value == null)
            return null;
        
        return value.toString();
    }
    public String getString(String key,String defaultValue)
    {
        Object value = this.get(key);
        
        if(value == null)
            return defaultValue;
        
        return value.toString();
    }
    
    
    
    
    
    
    public Object getObject(String key)
    {
        Object value = this.get(key);
        return value;
    }
   
    
    public Object getObject(String key,Object defaultValue)
    {
        Object value = this.get(key);
        
        if(value == null)
            return defaultValue;
        
        return value;
    }
    
    
}
