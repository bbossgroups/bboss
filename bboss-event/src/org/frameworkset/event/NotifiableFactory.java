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
 * 工厂类，用来获取相应类型的事件源
 *
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class NotifiableFactory implements java.io.Serializable {
    public NotifiableFactory() {
        
    }

//    protected  NotifiableInfo notifiableInfo;
    public static Notifiable getNotifiable()
    {
    	return EventHandle.getInstance();
    }
    public static void addListener(Listener listener,EventType eventtypes)
    {
    	getNotifiable().addListener(  listener,  eventtypes);
    }
    public static void addListener(Listener listener )
    {
    	getNotifiable().addListener(  listener );
    }
	
	/**
	 * Description 注册监听器方法
	 * @param listener 需要注册的监听器
	 * @param List<ResourceChangeEventType> 监听器需要监听的消息类型
	 * void
	 */
	public static void addListener(Listener listener,List eventtypes)
	{
		getNotifiable().addListener(  listener,  eventtypes);
	}
	
	
    

//    public NotifiableInfo getNotifiableInfo() {
//        return notifiableInfo;
//    }
//
//    public void setNotifiableInfo(NotifiableInfo notifiableInfo) {
//        this.notifiableInfo = notifiableInfo;
//    }
}
