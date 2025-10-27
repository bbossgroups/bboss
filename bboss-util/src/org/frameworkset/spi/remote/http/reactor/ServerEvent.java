package org.frameworkset.spi.remote.http.reactor;
/**
 * Copyright 2025 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 封装流式调用数据报文：
 * type 字段表示数据报文类型，0表示数据报文，1表示错误报文
 * data 字段包含数据内容，当type为0时，data字段包含数据内容，当type为1时，data字段包含错误信息
 * @author biaoping.yin
 * @Date 2025/10/19
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerEvent {
    public static final int DATA = 0;
    public static final int ERROR = 1;
    /**
     * 字段包含数据内容，当type为0时，data字段包含数据内容，当type为1时，data字段包含错误信息
     */
    private String data;
    /**
     * 扩展数据
     */
    private Map<String,Object> extendDatas ;
    /**
     * 字段表示数据报文类型，0表示数据报文，1表示错误报文,默认值为0
     */
    private int type = DATA;
    
    /**
     * 标记数据获取是否完成
     */
    private boolean done;



    /**
     * 是否是第一个数据报文
     */
    private boolean first;

    /**
     * 获取数据报文类型，0表示数据报文，1表示错误报文
     */
    public int getType() {
        return type;
    }

    /**
     * 设置数据报文类型，0表示数据报文，1表示错误报文,默认值为0
     */
    public void setType(int type) {
        this.type = type;
    }
    /**
     * 获取数据内容，当type为0时，data字段包含数据内容，当type为1时，data字段包含错误信息
     */
    public String getData() {
        return data;
    }
    /**
     * 设置数据内容，当type为0时，data字段包含数据内容，当type为1时，data字段包含错误信息
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * 设置扩展数据
     * @param extendDatas
     */
    public void setExtendDatas(Map<String, Object> extendDatas) {
        this.extendDatas = extendDatas;
    }
    
    /**
     * 添加扩展数据
     * @param name
     * @param value
     * @return
     */
    public ServerEvent addExtendData(String name,Object value){
        if(extendDatas == null){
            extendDatas = new LinkedHashMap<>();            
        }
        extendDatas.put(name,value);
        return this;
    }

    /**
     * 添加扩展数据
     * @param extendDatas
     * @return
     */
    public ServerEvent addExtendDatas(Map<String, Object> extendDatas){
        if(extendDatas == null){
            extendDatas = new LinkedHashMap<>();
        }
        extendDatas.putAll(extendDatas);
        return this;
    }

    /**
     * 获取扩展数据
     * @return
     */
    public Map<String, Object> getExtendDatas() {
        return extendDatas;
    }
    
    public boolean isDone() {
		return done;
	}

    public void setDone(boolean done) {
        this.done = done;
    }
    /**
     * 是否是第一个数据报文
     * @return
     */
    public boolean isFirst() {
        return first;
    }
    /**
     * 设置是否是第一个数据报文
     * @param first
     */
    public void setFirst(boolean first) {
        this.first = first;
    }
    
}
