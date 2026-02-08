package org.frameworkset.http;
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

import org.frameworkset.util.annotations.HttpMethod;

/**
 * @author biaoping.yin
 * @Date 2026/2/8
 */
public class HttpMethodsContainer {
    /**
     * 基于配置或者全局允许的httpmethod
     */
    protected HttpMethod[] httpMethods;
    protected String[] httpMethodNames;
    
    public boolean containMethods(){
        return httpMethods != null && httpMethods.length > 0;
    }

    public HttpMethod[] getHttpMethods() {
        return httpMethods;
    }
    public void setHttpMethods(HttpMethod[] httpMethods) {
        this.httpMethods = httpMethods;
        if(httpMethods == null || httpMethods.length == 0)
            return;
        this.httpMethodNames = new String[httpMethods.length];
        for(int i = 0; i < httpMethods.length; i ++){
            this.httpMethodNames[i] = httpMethods[i].name();
        }
    }

    public void setHttpMethods(HttpMethodsContainer httpMethodsContainer) {
        this.httpMethods = httpMethodsContainer.getHttpMethods();         
        this.httpMethodNames = httpMethodsContainer.getHttpMethodNames();
         
    }
    public String[] getHttpMethodNames() {
        return httpMethodNames;
    }
    public boolean contains(String method) {
        if(httpMethods == null || httpMethods.length == 0){
            return true;
        }
        HttpMethod httpMethod = HttpMethod.resolve(method);
        if(httpMethod == null){
            return true;
        }
        for(HttpMethod m:httpMethods){
            if(m.name().equals(method)){
                return true;
            }
        }
        return false;
        
    }
}
