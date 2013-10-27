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
package org.frameworkset.event;

import java.util.List;


/**
 * 事件发送者
 *
 * @author biaoping.yin
 * @version 1.0
 */
public interface Notifiable {
	/**
	 * Description 注册监听器方法
	 * @param listener 需要注册的监听器
	 * void
	 */
	public void addListener(Listener listener);
	
	/**
	 * Description 注册监听器方法
	 * @param listener 需要注册的监听器
	 * @remote 监听本地事件和远程事件标识，为true时监听远程事件
	 *         如果系统中没有启动集群或多实例功能时
	 * void
	 * @deprecated
	 * @see public void addListener(Listener listener,int listenerType);
	 */
	public void addListener(Listener listener,boolean remote);
	
	
	/**
	 * Description 注册监听器方法
	 * @param listener 需要注册的监听器
	 * @param List<ResourceChangeEventType> 监听器需要监听的消息类型
	 * void
	 */
	public void addListener(Listener listener,List eventtypes);
	/**
	 * Description 注册监听器方法
	 * @param listener 需要注册的监听器
	 * @param List<ResourceChangeEventType> 监听器需要监听的消息类型
	 * @parema boolean remote 区分监听器是否监听远程事件，
	 * 					true-监听本地和远程事件
	 * 					false-不监听，只监听本地事件
	 * void
	 * @deprecated 
	 * @see public void addListener(Listener listener,List eventtypes,int listenerType)
	 */
	public void addListener(Listener listener,List eventtypes,boolean remote);
	
	
	
	/**
	 * 
	 * @param listener 需要注册的监听器
	 * @param eventtypes List<ResourceChangeEventType> 监听器需要监听的消息类型
	 * @param listenerType 事件监听器类型
	 * 
	 */
	public void addListener(Listener listener,List eventtypes,int listenerType);
	
	/**
	 * 
	 * @param listener 需要注册的监听器，监听所有类型的事件
	 * 
	 * @param listenerType 事件监听器类型
	 * 
	 */
	public void addListener(Listener listener,int listenerType);

	/**
	 * Description 当事件发生时，调用该方法通知所有的监听器，系统根据synchronizable
         *             参数控制消息同步和异步处理消息机制
	 * @param source 事件源
         * @param synchronizable 控制消息同步和异步处理消息机制，false异步处理，true同步方式处理
	 * void
	 */
	public void change(Event source,boolean synchronizable);

    /**
     * Description 当事件发生时，调用该方法通知所有的监听器
     *             ,系统将以同步的方式处理事件消息
     * @param source 事件源
     * void
     */
    public void change(Event source);
}
