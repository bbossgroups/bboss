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
package com.frameworkset.commons.dbcp2;


/**
 * Constants for use with JMX
 * @version $Id: Constants.java 1649430 2015-01-04 21:29:32Z tn $
 * @since 2.0
 */
public class Constants {
    public static final String JMX_CONNECTION_POOL_BASE_EXT = ",connectionpool=";
    public static final String JMX_CONNECTION_POOL_PREFIX = "connections";

    public static final String JMX_CONNECTION_BASE_EXT =
            JMX_CONNECTION_POOL_BASE_EXT +
            JMX_CONNECTION_POOL_PREFIX +
            ",connection=";

    public static final String JMX_STATEMENT_POOL_BASE_EXT =
            JMX_CONNECTION_BASE_EXT;
    public static final String JMX_STATEMENT_POOL_PREFIX = ",statementpool=statements";
}
