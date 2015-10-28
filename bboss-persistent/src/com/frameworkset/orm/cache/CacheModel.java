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
package com.frameworkset.orm.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;



public class CacheModel {
	
	private static final Logger log = Logger.getLogger(CacheModel.class);

	  private static final int MAX_OBJECT_LOG_SIZE = 200;

	  /**
	   * This is used to represent null objects that are returned from the cache so
	   * that they can be cached, too.
	   */
	  public static final Object NULL_OBJECT = new String("SERIALIZABLE_NULL_OBJECT");
	  private int requests = 0;
	  private int hits = 0;

	  /**
	   * Constant to turn off periodic cache flushes
	   */
	  private static final long NO_FLUSH_INTERVAL = -99999;

	  private String id;

	  private boolean readOnly;
	  private boolean serialize;

	  private long lastFlush;
	  private long flushInterval;
	  private long flushIntervalSeconds;
	  private Set flushTriggerStatements;

	  private CacheControl controller;

	  private String resource;

	  /**
	   * Default constructor
	   */
	  public CacheModel() {
	    this.flushInterval = NO_FLUSH_INTERVAL;
	    this.flushIntervalSeconds = NO_FLUSH_INTERVAL;
	    this.lastFlush = System.currentTimeMillis();
	    this.flushTriggerStatements = new HashSet();
	  }

	  /**
	   * Getter for the cache model's id
	   *
	   * @return the id
	   */
	  public String getId() {
	    return id;
	  }

	  /**
	   * Setter for the cache model's id
	   *
	   * @param id - the new id
	   */
	  public void setId(String id) {
	    this.id = id;
	  }

	  /**
	   * Getter for read-only property
	   *
	   * @return true if a read-only model
	   */
	  public boolean isReadOnly() {
	    return readOnly;
	  }

	  /**
	   * Setter for read-only property
	   *
	   * @param readOnly - the new setting
	   */
	  public void setReadOnly(boolean readOnly) {
	    this.readOnly = readOnly;
	  }

	  /**
	   * Getter to tell if the cache serializes
	   *
	   * @return true if the cache model serializes objects
	   */
	  public boolean isSerialize() {
	    return serialize;
	  }

	  /**
	   * Setter to tell the cache to serialize objects
	   *
	   * @param serialize - if the cache model is to serialize objects
	   */
	  public void setSerialize(boolean serialize) {
	    this.serialize = serialize;
	  }

	  /**
	   * Getter for resource property
	   *
	   * @return the value of the resource property
	   */
	  public String getResource() {
	    return resource;
	  }

	  /**
	   * Setter for resource property
	   *
	   * @param resource - the new value
	   */
	  public void setResource(String resource) {
	    this.resource = resource;
	  }

	  /**
	   * Sets up the controller for the cache model
	   *
	   * @throws ClassNotFoundException - if the class cannot be found
	   * @throws InstantiationException - if the class cannot be instantiated
	   * @throws IllegalAccessException - if the classes constructor is not accessible
	   */
	  public void setCacheController(CacheControl controller)
	      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	    this.controller = controller;
	  }

	  /**
	   * Getter for flushInterval property
	   *
	   * @return The flushInterval (in milliseconds)
	   */
	  public long getFlushInterval() {
	    return flushInterval;
	  }

	  /**
	   * Getter for flushInterval property
	   *
	   * @return The flushInterval (in milliseconds)
	   */
	  public long getFlushIntervalSeconds() {
	    return flushIntervalSeconds;
	  }

	  /**
	   * Setter for flushInterval property
	   *
	   * @param flushInterval The new flushInterval (in milliseconds)
	   */
	  public void setFlushInterval(long flushInterval) {
	    this.flushInterval = flushInterval;
	    this.flushIntervalSeconds = flushInterval / 1000;
	  }

	  /**
	   * Adds a flushTriggerStatment. When a flushTriggerStatment is executed, the
	   * cache is flushed (cleared).
	   *
	   * @param statementName The statement to add.
	   */
	  public void addFlushTriggerStatement(String statementName) {
	    flushTriggerStatements.add(statementName);
	  }

	  /**
	   * Gets an Iterator containing all flushTriggerStatment objects for this cache.
	   *
	   * @return The Iterator
	   */
	  public Iterator getFlushTriggerStatementNames() {
	    return flushTriggerStatements.iterator();
	  }

	  /**
	   * ExecuteListener event.  This will be called by a MappedStatement
	   * for which this cache is registered as a ExecuteListener.  It will
	   * be called each time an executeXXXXXX method is called.  In the
	   * case of the Cache class, it is registered in order to flush the
	   * cache whenever a certain statement is executed.
	   * (i.e. the flushOnExecute cache policy)
	   *
	   * @param statement The statement to execute
	   */
	  public void onExecuteStatement(MappedStatement statement) {
	    flush();
	  }


	  /**
	   * Returns statistical information about the cache.
	   *
	   * @return the number of cache hits divided by the total requests
	   */
	  public double getHitRatio() {
	    return (double) hits / (double) requests;
	  }

	  /**
	   * Configures the cache
	   *
	   * @param props
	   */
	  public void configure(Properties props) {
	    controller.setProperties(props);
	  }

	  /**
	   * Clears the cache
	   */
	  public void flush() {
	  	synchronized (this)  {
	      controller.flush(this);
	      lastFlush = System.currentTimeMillis();
	      if ( log.isDebugEnabled() )  {
	        log("flushed", false, null);
	      }
	    }
	  }

	  /**
	   * Get an object out of the cache.
	   * A side effect of this method is that is may clear the cache if it has not been
	   * cleared in the flushInterval.
	   *
	   * @param key The key of the object to be returned
	   * @return The cached object (or null)
	   */
	  public Object getObject(CacheKey key) {
	  	Object value = null;
	    synchronized (this) {
	      if (flushInterval != NO_FLUSH_INTERVAL
	          && System.currentTimeMillis() - lastFlush > flushInterval) {
	        flush();
	      }

	      value = controller.getObject(this, key);
	      if (serialize && !readOnly &&
	       	    (value != NULL_OBJECT && value != null)) {
	        try {
	          ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) value);
	          ObjectInputStream ois = new ObjectInputStream(bis);
	          value = ois.readObject();
	          ois.close();
	        } catch (Exception e) {
	          throw new RuntimeException("Error caching serializable object.  Be sure you're not attempting to use " +
	                                           "a serialized cache for an object that may be taking advantage of lazy loading.  Cause: " + e, e);
	        }
	      }
	      requests++;
	      if (value != null) {
	        hits++;
	      }
	      if ( log.isDebugEnabled() )  {
	      	if ( value != null )  {
	            log("retrieved object", true, value);
	      	}
	      	else  {
	      		log("cache miss", false, null);
	      	}
	      }
	    }
	    return value;
	  }

	  /**
	   * Add an object to the cache
	   *
	   * @param key   The key of the object to be cached
	   * @param value The object to be cached
	   */
	  public void putObject(CacheKey key, Object value) {
	  	if (null == value) value = NULL_OBJECT;
	  	synchronized ( this )  {
	      if (serialize && !readOnly && value != NULL_OBJECT) {
	        try {
	          ByteArrayOutputStream bos = new ByteArrayOutputStream();
	          ObjectOutputStream oos = new ObjectOutputStream(bos);
	          oos.writeObject(value);
	          oos.flush();
	          oos.close();
	          value = bos.toByteArray();
	        } catch (IOException e) {
	          throw new RuntimeException("Error caching serializable object.  Cause: " + e, e);
	        }
	      }
	      controller.putObject(this, key, value);
	      if ( log.isDebugEnabled() )  {
	        log("stored object", true, value);
	      }
	    }
	  }

	  /**
	   * Get the maximum size of an object in the log output.
	   *
	   * @return Maximum size of a logged object in the output
	   */
	  protected int getMaxObjectLogSize()  {
	      return MAX_OBJECT_LOG_SIZE;
	  }

	  /**
	   * Log a cache action. Since this method is pretty heavy
	   * weight, it's best to enclose it with a log.isDebugEnabled()
	   * when called.
	   *
	   * @param action String to output
	   * @param addValue Add the value being cached to the log
	   * @param cacheValue The value being logged
	   */
	  protected void log(String action, boolean addValue,
	  		             Object cacheValue)  {
	    StringBuffer output = new StringBuffer("Cache '");
	    output.append(getId());
	    output.append("': ");
	    output.append(action);
	    if ( addValue )  {
	      String cacheObjectStr = (cacheValue == null ? "null" : cacheValue.toString());
	      output.append(" '");
	      if ( cacheObjectStr.length() < getMaxObjectLogSize() )  {
	      	output.append(cacheObjectStr.toString());
	      }
	      else  {
	      	output.append(cacheObjectStr.substring(1,
	      			                 getMaxObjectLogSize()));
	      	output.append("...");
	      }
	      output.append("'");
	    }
	    log.debug(output.toString());
	  }

	  public void setControllerProperties(Properties cacheProps) {
	    controller.setProperties(cacheProps);
	  }

}
