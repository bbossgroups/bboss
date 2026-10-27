package org.frameworkset.util;
/**
 * Copyright 2026 bboss
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

import org.slf4j.Logger;

/**
 * @author biaoping.yin
 * @Date 2026/6/3
 */
public class RetryUtil {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(RetryUtil.class);
    public static <T> T retry(int retry, long retryInterval, RetryCallback<T> retryCallback) throws RetryException {
        
        return executeWithRetry( retry,   retryInterval,  retryCallback);
    }

    public static <T> T executeWithRetry(int retry, long retryInterval, RetryCallback<T> retryCallback) throws RetryException {

        return executeWithRetry( null, retry,   retryInterval,  retryCallback);
    }

    public static <T> T executeWithRetry(String label,int retry, long retryInterval, RetryCallback<T> retryCallback) throws RetryException {
		Throwable exception = null;
		Throwable firstException = null;
        T ret = null;
        int i = 0;
        int max = retry;
		RetryContext retryContext = retryCallback.getRetryContext();
		if(retryContext == null) 
			retryContext =	 new RetryContext();
        
        do {
            try{
                if(exception != null){
					
                    i ++;
                    if(label == null) {
                        logger.warn("Retry times:{}/{} ,retryInterval:{}ms",i,max,retryInterval,exception);
                    }
                    else{
                        logger.warn("{} retry times:{}/{} ,retryInterval:{}ms",label,i,max,retryInterval,exception);
                    }
                    if(retryInterval > 0L)
                        Thread.sleep(retryInterval);
                }
                ret = retryCallback.doRetry(exception,i,retryContext);
				if(retryContext.isTerminateRetry()){
					break;
				}
                exception = null;
				firstException = null;
                break;
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                if(label == null) {
                    logger.warn("Retry times:{}/{} ,retryInterval:{}ms",i,max,retryInterval,ex);
                }
                else{
                    logger.warn("{} retry times:{}/{} ,retryInterval:{}ms",label,i,max,retryInterval,ex);
                }
                if(firstException== null){
					firstException = ex;
				}
                break;
            }
            catch (Exception ex) {
                exception = ex;
				if(firstException== null){
					firstException = ex;
				}
				if(!retryCallback.assertRetryException(exception)){
					break;
				}
				retry --;
            }
			catch (Throwable ex) {
				exception = ex;
				if(firstException== null){
					firstException = ex;
				}
				if(!retryCallback.assertRetryException(exception)){
					break;
				}
				retry --;
			}

        }while (retry >= 0);
        if(firstException != null) {
            if(firstException instanceof RuntimeException){
                throw (RuntimeException)firstException;
            }
            else {
                throw new RetryException(firstException);
            }
        }
        return ret;
    }
}
