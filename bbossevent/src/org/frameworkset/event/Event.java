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

/**
 * 事件，比如：
 * 用户/组信息的修改、角色信息的修改、
 * 资源信息的修改、许可信息的修改、
 * 用户/组角色关系的修改等等
 * @author biaoping.yin
 * @version 1.0
 * 
 */
public interface Event<T> extends java.io.Serializable{

	public T getSource();

	public EventType getType();
	
	/**
	 * 是否异步传播，集群环境下事件以异步的方式广播，返回值为false，
	 * 同步方式广播时，返回true
	 * @return
	 */
	public boolean isSynchronized();
	
	public void setSynchronized(boolean issynchronized);	
	public boolean isRemote();	
	public boolean isRemoteLocal();
	public boolean isLocal();
	public EventTarget getEventTarget();
	
	/**
	 * 消息传播类型：
	 * 本地传播
	 * 远程传播
	 * 本地远程传播 
	 */
	
	/**
	 * 本地传播
	 */
	public static int LOCAL = 0;
	/**
	 * 远程传播
	 */
	public static int REMOTE = 1;
	/**
	 * 本地远程传播
	 */
	public static int REMOTELOCAL = 2;
	
	



}
