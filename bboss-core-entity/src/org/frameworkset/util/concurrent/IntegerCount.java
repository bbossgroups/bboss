package org.frameworkset.util.concurrent;
/**
 * Copyright 2020 bboss
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

/**
 * <p>Description: 多线程安全计数器</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/1/10 10:17
 * @author biaoping.yin
 * @version 1.0
 */
public class IntegerCount {
	private volatile int count;
	public synchronized int increament(){
		count ++;
		return count;
	}
	public synchronized int increament(int incr){
		count = count + incr;
		return count;
	}
	public synchronized int getCount(){
		return count;
	}
	public int getCountUnSynchronized(){
		return count;
	}

	public int increamentUnSynchronized(){
		count ++;
		return count;
	}
	public int increamentUnSynchronized(int incr){
		count = count + incr;
		return count;
	}

}
