/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frameworkset.commons.dbcp;

/**
 * Configuration settings for handling abandoned db connections.
 *                                                            
 * @author Glenn L. Nielsen           
 * @version $Revision: 482015 $ $Date: 2006-12-03 19:22:09 -0700 (Sun, 03 Dec 2006) $
 * @deprecated This will be removed in a future version of DBCP.
 */
public class AbandonedConfig {

    /**
     * Whether or not a connection is considered abandoned and eligible
     * for removal if it has been idle longer than the removeAbandonedTimeout
     */
    private boolean removeAbandoned = false;

    /**
     * Flag to remove abandoned connections if they exceed the
     * removeAbandonedTimeout.
     *
     * Set to true or false, default false.
     * If set to true a connection is considered abandoned and eligible
     * for removal if it has been idle longer than the removeAbandonedTimeout.
     * Setting this to true can recover db connections from poorly written    
     * applications which fail to close a connection.
     *
     * @return true if abandoned connections are to be removed
     */
    public boolean getRemoveAbandoned() {
        return (this.removeAbandoned);
    }

    /**
     * Flag to remove abandoned connections if they exceed the
     * removeAbandonedTimeout.
     *
     * Set to true or false, default false.
     * If set to true a connection is considered abandoned and eligible   
     * for removal if it has been idle longer than the removeAbandonedTimeout.
     * Setting this to true can recover db connections from poorly written
     * applications which fail to close a connection.
     *
     * @param removeAbandoned true means abandoned connections will be
     *   removed
     */
    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    /**
     * Timeout in seconds before an abandoned connection can be removed
     */
    private int removeAbandonedTimeout = 300;

    /**
     * Timeout in seconds before an abandoned connection can be removed.
     *
     * Defaults to 300 seconds.
     *
     * @return abandoned timeout in seconds
     */
    public int getRemoveAbandonedTimeout() {
        return (this.removeAbandonedTimeout);
    }

    /**
     * Timeout in seconds before an abandoned connection can be removed.
     *
     * Defaults to 300 seconds.
     *
     * @param removeAbandonedTimeout abandoned timeout in seconds
     */
    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    /**
     * Determines whether or not to log stack traces for application code
     * which abandoned a Statement or Connection.
     */
    private boolean logAbandoned = false;

    /**
     * Flag to log stack traces for application code which abandoned
     * a Statement or Connection.
     *
     * Defaults to false.
     * Logging of abandoned Statements and Connections adds overhead
     * for every Connection open or new Statement because a stack
     * trace has to be generated.
     * 
     * @return boolean true if stack trace logging is turned on for abandoned
     *  Statements or Connections
     *
     */
    public boolean getLogAbandoned() {
        return (this.logAbandoned);
    }

    /**
     * Flag to log stack traces for application code which abandoned
     * a Statement or Connection.
     *
     * Defaults to false.
     * Logging of abandoned Statements and Connections adds overhead
     * for every Connection open or new Statement because a stack
     * trace has to be generated.
     * @param logAbandoned true turns on abandoned stack trace logging
     *
     */
    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

}
