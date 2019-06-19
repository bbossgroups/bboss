package org.frameworkset.spi.remote.http.proxy;


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

public class RoundRobinList {

	private final List<HttpAddress> elements;

	private Iterator<HttpAddress> iterator;
	private String message;


	public RoundRobinList(List<HttpAddress> elements) {
		this.elements = elements;
		message = "All Http Server "+elements.toString()+" can't been connected.";
		iterator = this.elements.iterator();
	}


	public void addAddresses(List<HttpAddress> address){
		try{
			lock.lock();
			this.elements.addAll(address);
			message = new StringBuilder().append("All Http Server ").append(elements.toString()).append(" can't been connected.").toString();
			this.iterator = elements.iterator();
		}
		finally {
			lock.unlock();
		}
	}
	private Lock lock = new ReentrantLock();
	public HttpAddress get(){
		try {
			lock.lock();
			HttpAddress address = null;
			HttpAddress temp = null;
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
				if(temp == null)
					throw new NoHttpServerException(message);
				return temp;
			}
		}
		finally {
			lock.unlock();
		}
	}

	public int size() {
		return elements.size();
	}
}
