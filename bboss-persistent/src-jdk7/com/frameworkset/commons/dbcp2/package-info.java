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

/**
 * <p>
 *    Database Connection Pool API.
 * </p>
 *
 * <b>Overview in Dialog Form</b>
 * <p>
 *    Q: How do I use the DBCP package?
 * </p><p>
 *    A: There are two primary ways to access the DBCP pool, as a
 *    {@link java.sql.Driver Driver}, or as a {@link javax.sql.DataSource DataSource}.
 *    You'll want to create an instance of {@link com.frameworkset.commons.dbcp2.PoolingDriver} or
 *    {@link com.frameworkset.commons.dbcp2.PoolingDataSource}.  When using one of these
 *    interfaces, you can just use your JDBC objects the way you normally would.
 *    Closing a {@link java.sql.Connection} will simply return it to its pool.
 * </p>
 * <p>
 *    Q: But {@link com.frameworkset.commons.dbcp2.PoolingDriver PoolingDriver} and
 *    {@link com.frameworkset.commons.dbcp2.PoolingDataSource PoolingDataSource} both expect an
 *    {@link com.frameworkset.commons.pool2.ObjectPool ObjectPool} as an input.  Where do I
 *    get one of those?
 * </p><p>
 *    A: The {@link com.frameworkset.commons.pool2.ObjectPool ObjectPool} interface is defined
 *    in Commons Pool. You can use one of the provided implementations such as
 *    {@link com.frameworkset.commons.pool2.impl.GenericObjectPool GenericObjectPool},
 *    {@link com.frameworkset.commons.pool2.proxy.ProxiedObjectPool ProxiedObjectPool} or
 *    {@link com.frameworkset.commons.pool2.impl.SoftReferenceObjectPool SoftReferenceObjectPool}
 *    or you can create your own.
 * </p>
 * <p>
 *    Q: Ok, I've found an {@link com.frameworkset.commons.pool2.ObjectPool ObjectPool}
 *    implementation that I think suits my connection pooling needs.  But it wants
 *    a {@link com.frameworkset.commons.pool2.PooledObjectFactory PooledObjectFactory}.
 *    What should I use for that?
 * </p><p>
 *    A: The DBCP package provides a class for this purpose. It's called
 *    {@link com.frameworkset.commons.dbcp2.PoolableConnectionFactory}.
 *    It implements the factory and lifecycle methods of
 *    {@link com.frameworkset.commons.pool2.PooledObjectFactory}
 *    for {@link java.sql.Connection}s.  But it doesn't create the actual database
 *    {@link java.sql.Connection}s itself, it uses a
 *    {@link com.frameworkset.commons.dbcp2.ConnectionFactory} for that.
 *    The {@link com.frameworkset.commons.dbcp2.PoolableConnectionFactory} will take
 *    {@link java.sql.Connection}s created by the {@link com.frameworkset.commons.dbcp2.ConnectionFactory}
 *    and wrap them with classes that implement the pooling behaviour.
 * </p><p>
 *    Several implementations of {@link com.frameworkset.commons.dbcp2.ConnectionFactory} are
 *    provided--one that uses {@link java.sql.DriverManager} to create connections
 *    ({@link com.frameworkset.commons.dbcp2.DriverManagerConnectionFactory}),
 *    one that uses a {@link java.sql.Driver} to create connections
 *    ({@link com.frameworkset.commons.dbcp2.DriverConnectionFactory}),
 *    one that uses a {@link javax.sql.DataSource} to create connections
 *    ({@link com.frameworkset.commons.dbcp2.DataSourceConnectionFactory}).
 * </p>
 * <p>
 *    Q: I think I'm starting to get it, but can you walk me though it again?
 * </p><p>
 *    A: Sure.  Let's assume you want to create a {@link javax.sql.DataSource}
 *    that pools {@link java.sql.Connection}s.  Let's also assume that
 *    those pooled {@link java.sql.Connection}s should be obtained from
 *    the {@link java.sql.DriverManager}.
 *    You'll want to create a {@link com.frameworkset.commons.dbcp2.PoolingDataSource}.
 * </p><p>
 *    The {@link com.frameworkset.commons.dbcp2.PoolingDataSource} uses an underlying
 *    {@link com.frameworkset.commons.pool2.ObjectPool} to create and store its
 *    {@link java.sql.Connection}.
 * </p><p>
 *    To create a {@link com.frameworkset.commons.pool2.ObjectPool}, you'll need
 *    a {@link com.frameworkset.commons.pool2.PooledObjectFactory} that creates
 *    the actual {@link java.sql.Connection}s.  That's what
 *    {@link com.frameworkset.commons.dbcp2.PoolableConnectionFactory} is for.
 * </p><p>
 *    To create the {@link com.frameworkset.commons.dbcp2.PoolableConnectionFactory},
 *    you'll need at least two things:</p>
 *    <ol>
 *     <li>
 * A {@link com.frameworkset.commons.dbcp2.ConnectionFactory} from which
 * the actual database {@link java.sql.Connection}s will be obtained.
 *     </li>
 *     <li>
 * An empty and factory-less {@link com.frameworkset.commons.pool2.ObjectPool}
 * in which the {@link java.sql.Connection}s will be stored.
 * <br>
 * When you pass an {@link com.frameworkset.commons.pool2.ObjectPool} into the
 * {@link com.frameworkset.commons.dbcp2.PoolableConnectionFactory}, it will
 * automatically register itself as the {@link com.frameworkset.commons.pool2.PooledObjectFactory}
 * for that pool.
 *     </li>
 *    </ol>
 * <p>
 *    In code, that might look like this:
 * </p>
 * <pre>GenericObjectPool connectionPool = new GenericObjectPool(null);
 * ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:some:connect:string", "username", "password");
 * PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
 * PoolingDataSource dataSource = new PoolingDataSource(connectionPool);</pre>
 * <p>
 *    To create a {@link com.frameworkset.commons.dbcp2.PoolingDriver}, we do the same thing,
 *    except that instead of creating a {@link javax.sql.DataSource} on the last line,
 *    we create a {@link com.frameworkset.commons.dbcp2.PoolingDriver}, and register the
 *    {@code connectionPool} with it.  E.g.,:</p>
 * <pre>GenericObjectPool connectionPool = new GenericObjectPool(null);
 * ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:some:connect:string", "username", "password");
 * PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
 * PoolingDriver driver = new PoolingDriver();
 * driver.registerPool("example",connectionPool);</pre>
 * <p>
 *    Since the {@link com.frameworkset.commons.dbcp2.PoolingDriver} registers itself
 *    with the {@link java.sql.DriverManager} when it is created, now you can just
 *    go to the {@link java.sql.DriverManager} to create your {@link java.sql.Connection}s,
 *    like you normally would:</p>
 * <pre>Connection conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:example");</pre>
 */
package com.frameworkset.commons.dbcp2;
