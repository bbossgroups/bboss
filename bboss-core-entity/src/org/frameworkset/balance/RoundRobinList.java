package org.frameworkset.balance;


import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Copyright 2014 Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RoundRobinList<T extends Node> {

	private final List<T> elements;

	private Iterator<T> iterator;
	private String message;


	public RoundRobinList(List<T> elements) {
		this.elements = elements;
		message = "All server "+elements.toString()+" can't been connected.";
		iterator = this.elements.iterator();
	}

//	public synchronized T get() {
//		T address = null;
//		while (iterator.hasNext()) {
//			address = iterator.next();
//			if (address.ok())
//				break;
//		}
//		if (address != null) {
//			return address;
//
//		}
//		else {
//			iterator = elements.iterator();
//			while (iterator.hasNext()) {
//				address = iterator.next();
//				if (address.ok())
//					break;
//			}
//			return address;
//		}
//
//	}
	public void addAddresses(List<T> address){
		lock.lock();
		try{

			this.elements.addAll(address);
			message = new StringBuilder().append("All server ").append(elements.toString()).append(" can't been connected.").toString();
			this.iterator = elements.iterator();
		}
		finally {
			lock.unlock();
		}
	}
	private final Lock lock = new ReentrantLock();
	private T _get(){
		lock.lock();
		try {

            T address = null;
            T temp = null;
			while (iterator.hasNext()) {
				address = iterator.next();
				if (address.ok()){
					temp = address;
					break;
				}
			}
			if (temp != null) {
				return temp;

			} else {
				iterator = elements.iterator();
				while (iterator.hasNext()) {
					address = iterator.next();
					if (address.ok()){
						temp = address;
						break;
					}
				}
//				if(temp == null)
//					throw new NoServerElasticSearchException(message);
				return temp;
			}
		}
		finally {
			lock.unlock();
		}
	}

	private T _getOkOrFailed(){
		lock.lock();
		try {

            T address = null;
            T temp = null;
			while (iterator.hasNext()) {
				address = iterator.next();
				if (address.okOrFailed()){
					temp = address;
					break;
				}
			}
			if (temp != null) {
				return temp;

			} else {
				iterator = elements.iterator();
				while (iterator.hasNext()) {
					address = iterator.next();
					if (address.okOrFailed()){
						temp = address;
						break;
					}
				}

				return temp;
			}
		}
		finally {
			lock.unlock();
		}
	}
	public T get(boolean failAllContinue){
        T esAddress = _get();
		if(esAddress != null){
			return esAddress;
		}
		if(!failAllContinue){
			throw new NoServerException(message);
		}
//		if(esAddress != null || !failAllContinue)
//			return esAddress;


		esAddress = _getOkOrFailed();
		if(esAddress != null){
			return esAddress;
		}
		else{
			throw new NoServerException(message);
		}
	}

	public int size() {
		return elements.size();
	}
}
