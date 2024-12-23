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

package com.frameworkset.commons.pool2.impl;

import java.io.PrintWriter;

import com.frameworkset.commons.pool2.TrackedUse;
import com.frameworkset.commons.pool2.UsageTracking;

/**
 * Configuration settings for abandoned object removal.
 *
 * @version $Revision:$
 *
 * @since 2.0
 */
public class AbandonedConfig {

    /**
     * Whether or not borrowObject performs abandoned object removal.
     */
    private boolean removeAbandonedOnBorrow = false;

    /**
     * <p>Flag to remove abandoned objects if they exceed the
     * removeAbandonedTimeout when borrowObject is invoked.</p>
     *
     * <p>The default value is false.</p>
     *
     * <p>If set to true, abandoned objects are removed by borrowObject if
     * there are fewer than 2 idle objects available in the pool and
     * <code>getNumActive() &gt; getMaxTotal() - 3</code></p>
     *
     * @return true if abandoned objects are to be removed by borrowObject
     */
    public boolean getRemoveAbandonedOnBorrow() {
        return this.removeAbandonedOnBorrow;
    }

    /**
     * <p>Flag to remove abandoned objects if they exceed the
     * removeAbandonedTimeout when borrowObject is invoked.</p>
     *
     * @param removeAbandonedOnBorrow true means abandoned objects will be
     *   removed by borrowObject
     * @see #getRemoveAbandonedOnBorrow()
     */
    public void setRemoveAbandonedOnBorrow(boolean removeAbandonedOnBorrow) {
        this.removeAbandonedOnBorrow = removeAbandonedOnBorrow;
    }

    /**
     * Whether or not pool maintenance (evictor) performs abandoned object
     * removal.
     */
    private boolean removeAbandonedOnMaintenance = false;

    /**
     * <p>Flag to remove abandoned objects if they exceed the
     * removeAbandonedTimeout when pool maintenance (the "evictor")
     * runs.</p>
     *
     * <p>The default value is false.</p>
     *
     * <p>If set to true, abandoned objects are removed by the pool
     * maintenance thread when it runs.  This setting has no effect
     * unless maintenance is enabled by setting
     *{@link GenericObjectPool#getTimeBetweenEvictionRunsMillis() timeBetweenEvictionRunsMillis}
     * to a positive number.</p>
     *
     * @return true if abandoned objects are to be removed by the evictor
     */
    public boolean getRemoveAbandonedOnMaintenance() {
        return this.removeAbandonedOnMaintenance;
    }

    /**
     * <p>Flag to remove abandoned objects if they exceed the
     * removeAbandonedTimeout when pool maintenance runs.</p>
     *
     * @param removeAbandonedOnMaintenance true means abandoned objects will be
     *   removed by pool maintenance
     * @see #getRemoveAbandonedOnMaintenance
     */
    public void setRemoveAbandonedOnMaintenance(boolean removeAbandonedOnMaintenance) {
        this.removeAbandonedOnMaintenance = removeAbandonedOnMaintenance;
    }

    /**
     * Timeout in seconds before an abandoned object can be removed.
     */
    private int removeAbandonedTimeout = 300;

    /**
     * <p>Timeout in seconds before an abandoned object can be removed.</p>
     *
     * <p>The time of most recent use of an object is the maximum (latest) of
     * {@link TrackedUse#getLastUsed()} (if this class of the object implements
     * TrackedUse) and the time when the object was borrowed from the pool.</p>
     *
     * <p>The default value is 300 seconds.</p>
     *
     * @return the abandoned object timeout in seconds
     */
    public int getRemoveAbandonedTimeout() {
        return this.removeAbandonedTimeout;
    }

    /**
     * <p>Sets the timeout in seconds before an abandoned object can be
     * removed</p>
     *
     * <p>Setting this property has no effect if
     * {@link #getRemoveAbandonedOnBorrow() removeAbandonedOnBorrow} and
     * {@link #getRemoveAbandonedOnMaintenance() removeAbandonedOnMaintenance}
     * are both false.</p>
     *
     * @param removeAbandonedTimeout new abandoned timeout in seconds
     * @see #getRemoveAbandonedTimeout()
     */
    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    /**
     * Determines whether or not to log stack traces for application code
     * which abandoned an object.
     */
    private boolean logAbandoned = false;

    /**
     * Flag to log stack traces for application code which abandoned
     * an object.
     *
     * Defaults to false.
     * Logging of abandoned objects adds overhead for every object created
     * because a stack trace has to be generated.
     *
     * @return boolean true if stack trace logging is turned on for abandoned
     * objects
     *
     */
    public boolean getLogAbandoned() {
        return this.logAbandoned;
    }

    /**
     * Sets the flag to log stack traces for application code which abandoned
     * an object.
     *
     * @param logAbandoned true turns on abandoned stack trace logging
     * @see #getLogAbandoned()
     *
     */
    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    /**
     * PrintWriter to use to log information on abandoned objects.
     * Use of default system encoding is deliberate.
     */
    private PrintWriter logWriter = new PrintWriter(System.out);

    /**
     * Returns the log writer being used by this configuration to log
     * information on abandoned objects. If not set, a PrintWriter based on
     * System.out with the system default encoding is used.
     *
     * @return log writer in use
     */
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    /**
     * Sets the log writer to be used by this configuration to log
     * information on abandoned objects.
     *
     * @param logWriter The new log writer
     */
    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    /**
     * If the pool implements {@link UsageTracking}, should the pool record a
     * stack trace every time a method is called on a pooled object and retain
     * the most recent stack trace to aid debugging of abandoned objects?
     */
    private boolean useUsageTracking = false;

    /**
     * If the pool implements {@link UsageTracking}, should the pool record a
     * stack trace every time a method is called on a pooled object and retain
     * the most recent stack trace to aid debugging of abandoned objects?
     *
     * @return <code>true</code> if usage tracking is enabled
     */
    public boolean getUseUsageTracking() {
        return useUsageTracking;
    }

    /**
     * If the pool implements {@link UsageTracking}, configure whether the pool
     * should record a stack trace every time a method is called on a pooled
     * object and retain the most recent stack trace to aid debugging of
     * abandoned objects.
     *
     * @param   useUsageTracking    A value of <code>true</code> will enable
     *                              the recording of a stack trace on every use
     *                              of a pooled object
     */
    public void setUseUsageTracking(boolean useUsageTracking) {
        this.useUsageTracking = useUsageTracking;
    }
}