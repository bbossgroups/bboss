/**
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
package org.frameworkset.remote;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;

/**
 * 
 * <p>Title: EventRemoteServiceImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-4-17 下午02:02:31
 * @author biaoping.yin
 * @version 1.0
 */
public class EventRemoteServiceImpl implements EventRemoteService {
    
    /**
     * 处理远程事件接口
     * @param eventfqn
     * @return
     * @throws Exception
     */
    public Object remoteEventChange(String eventfqn,Event event) throws Exception {            
            return EventHandle.remotechange(eventfqn,event);
    }

}
