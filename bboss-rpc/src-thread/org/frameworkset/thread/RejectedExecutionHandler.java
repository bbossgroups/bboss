/*
 * @(#)RejectedExecutionHandler.java	1.3 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.frameworkset.thread;

import java.util.concurrent.RejectedExecutionException;

/**
 * A handler for tasks that cannot be executed by a {@link
 * ThreadPoolExecutor}.
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface RejectedExecutionHandler {

    /**
     * Method that may be invoked by a {@link ThreadPoolExecutor} when
     * <tt>execute</tt> cannot accept a task. This may occur when no
     * more threads or queue slots are available because their bounds
     * would be exceeded, or upon shutdown of the Executor.
     *
     * In the absence other alternatives, the method may throw an
     * unchecked {@link RejectedExecutionException}, which will be
     * propagated to the caller of <tt>execute</tt>.
     *
     * @param r the runnable task requested to be executed
     * @param executor the executor attempting to execute this task
     * @throws RejectedExecutionException if there is no remedy
     */
    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}
