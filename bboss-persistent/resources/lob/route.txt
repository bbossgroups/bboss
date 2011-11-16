1. MySQL Connector/J
     ________________________________________________________

   MySQL provides connectivity for client applications developed
   in the Java programming language via a JDBC driver, which is
   called MySQL Connector/J.

   MySQL Connector/J is a JDBC-3.0 Type 4 driver, which means
   that is pure Java, implements version 3.0 of the JDBC
   specification, and communicates directly with the MySQL
   server using the MySQL protocol.

   Although JDBC is useful by itself, we would hope that if you
   are not familiar with JDBC that after reading the first few
   sections of this manual, that you would avoid using naked
   JDBC for all but the most trivial problems and consider using
   one of the popular persistence frameworks such as Hibernate
   (http://www.hibernate.org/), Spring's JDBC templates
   (http://www.springframework.org/) or Ibatis SQL Maps
   (http://ibatis.apache.org/) to do the majority of repetitive
   work and heavier lifting that is sometimes required with
   JDBC.

   This section is not designed to be a complete JDBC tutorial.
   If you need more information about using JDBC you might be
   interested in the following online tutorials that are more
   in-depth than the information presented here:
     * JDBC Basics
       (http://java.sun.com/docs/books/tutorial/jdbc/basics/inde
       x.html) --- A tutorial from Sun covering beginner topics
       in JDBC
     * JDBC Short Course
       (http://java.sun.com/developer/onlineTraining/Database/JD
       BCShortCourse/index.html) --- A more in-depth tutorial
       from Sun and JGuru

   Key topics:
     * For help with connection strings, connection options
       setting up your connection through JDBC, see Section
       Section, "Driver/Datasource Class Names, URL Syntax and
       Configuration Properties for Connector/J."
     * For tips on using Connector/J and JDBC with generic J2EE
       toolkits, see Section Section, "Using Connector/J with
       J2EE and Other Java Frameworks."
     * Developers using the Tomcat server platform, see Section
       Section, "Using Connector/J with Tomcat."
     * Developers using JBoss, see Section Section, "Using
       Connector/J with JBoss."
     * Developers using Spring, see Section Section, "Using
       Connector/J with Spring."

   MySQL Enterprise MySQL Enterprise subscribers will find more
   information about using JDBC with MySQL in the Knowledge Base
   articles about JDBC
   (https://kb.mysql.com/search.php?cat=search&category=10).
   Access to the MySQL Knowledge Base collection of articles is
   one of the advantages of subscribing to MySQL Enterprise. For
   more information see
   http://www.mysql.com/products/enterprise/advisors.html.

1.1. Connector/J Versions

   There are currently four versions of MySQL Connector/J
   available:
     * Connector/J 5.1 is current in alpha status. It provides
       compatibility with all the functionality of MySQL,
       including 4.1, 5.0, 5.1 and the 6.0 alpha release
       featuring the new Falcon storage engine. Connector/J 5.1
       provides ease of development features, including
       auto-registration with the Driver Manager, standardized
       validity checks, categorized SQLExceptions, support for
       the JDBC-4.0 XML processing, per connection client
       information, NCHAR, NVARCHAR and NCLOB types. This
       release also includes all bug fixes up to and including
       Connector/J 5.0.6.
     * Connector/J 5.0 provides support for all the
       functionality offered by Connector/J 3.1 and includes
       distributed transaction (XA) support.
     * Connector/J 3.1 was designed for connectivity to MySQL
       4.1 and MySQL 5.0 servers and provides support for all
       the functionality in MySQL 5.0 except distributed
       transaction (XA) support.
     * Connector/J 3.0 provides core functionality and was
       designed with connectivity to MySQL 3.x or MySQL 4.1
       servers, although it will provide basic compatibility
       with later versions of MySQL. Connector/J 3.0 does not
       support server-side prepared statements, and does not
       support any of the features in versions of MySQL later
       than 4.1.

   The current recommended version for Connector/J is 5.0. This
   guide covers all three connector versions, with specific
   notes given where a setting applies to a specific option.

1.1.1. Java Versions Supported

   MySQL Connector/J supports Java-2 JVMs, including:
     * JDK 1.2.x (only for Connector/J 3.1.x or earlier)
     * JDK 1.3.x
     * JDK 1.4.x
     * JDK 1.5.x

   If you are building Connector/J from source using the source
   distribution (see Section Section, "Installing from the
   Development Source Tree") then you must use JDK 1.4.x or
   newer to compiler the Connector package.

   MySQL Connector/J does not support JDK-1.1.x or JDK-1.0.x.

   Because of the implementation of java.sql.Savepoint,
   Connector/J 3.1.0 and newer will not run on JDKs older than
   1.4 unless the class verifier is turned off (by setting the
   -Xverify:none option to the Java runtime). This is because
   the class verifier will try to load the class definition for
   java.sql.Savepoint even though it is not accessed by the
   driver unless you actually use savepoint functionality.

   Caching functionality provided by Connector/J 3.1.0 or newer
   is also not available on JVMs older than 1.4.x, as it relies
   on java.util.LinkedHashMap which was first available in
   JDK-1.4.0.

1.2. Connector/J Installation

   You can install the Connector/J package using two methods,
   using either the binary or source distribution. The binary
   distribution provides the easiest methods for installation;
   the source distribution enables you to customize your
   installation further. With either solution, you must manually
   add the Connector/J location to your Java CLASSPATH.

1.2.1. Installing Connector/J from a Binary Distribution

   The easiest method of installation is to use the binary
   distribution of the Connector/J package. The binary
   distribution is available either as a Tar/Gzip or Zip file
   which you must extract to a suitable location and then
   optionally make the information about the package available
   by changing your CLASSPATH (see Section Section, "Installing
   the Driver and Configuring the CLASSPATH").

   MySQL Connector/J is distributed as a .zip or .tar.gz archive
   containing the sources, the class files, and the JAR archive
   named mysql-connector-java-[version]-bin.jar, and starting
   with Connector/J 3.1.8 a debug build of the driver in a file
   named mysql-connector-java-[version]-bin-g.jar.

   Starting with Connector/J 3.1.9, the .class files that
   constitute the JAR files are only included as part of the
   driver JAR file.

   You should not use the debug build of the driver unless
   instructed to do so when reporting a problem or a bug to
   MySQL AB, as it is not designed to be run in production
   environments, and will have adverse performance impact when
   used. The debug binary also depends on the Aspect/J runtime
   library, which is located in the src/lib/aspectjrt.jar file
   that comes with the Connector/J distribution.

   You will need to use the appropriate graphical or
   command-line utility to extract the distribution (for
   example, WinZip for the .zip archive, and tar for the .tar.gz
   archive). Because there are potentially long filenames in the
   distribution, we use the GNU tar archive format. You will
   need to use GNU tar (or an application that understands the
   GNU tar archive format) to unpack the .tar.gz variant of the
   distribution.

1.2.2. Installing the Driver and Configuring the CLASSPATH

   Once you have extracted the distribution archive, you can
   install the driver by placing
   mysql-connector-java-[version]-bin.jar in your classpath,
   either by adding the full path to it to your CLASSPATH
   environment variable, or by directly specifying it with the
   command line switch -cp when starting your JVM.

   If you are going to use the driver with the JDBC
   DriverManager, you would use com.mysql.jdbc.Driver as the
   class that implements java.sql.Driver.

   You can set the CLASSPATH environment variable under UNIX,
   Linux or Mac OS X either locally for a user within their
   .profile, .login or other login file. You can also set it
   globally by editing the global /etc/profile file.

   For example, under a C shell (csh, tcsh) you would add the
   Connector/J driver to your CLASSPATH using the following:
shell> setenv CLASSPATH /path/mysql-connector-java-[ver]-bin.jar:$CLAS
SPATH

   Or with a Bourne-compatible shell (sh, ksh, bash):
export set CLASSPATH=/path/mysql-connector-java-[ver]-bin.jar:$CLASSPA
TH

   Within Windows 2000, Windows XP and Windows Server 2003, you
   must set the environment variable through the System control
   panel.

   If you want to use MySQL Connector/J with an application
   server such as Tomcat or JBoss, you will have to read your
   vendor's documentation for more information on how to
   configure third-party class libraries, as most application
   servers ignore the CLASSPATH environment variable. For
   configuration examples for some J2EE application servers, see
   Section Section, "Using Connector/J with J2EE and Other Java
   Frameworks." However, the authoritative source for JDBC
   connection pool configuration information for your particular
   application server is the documentation for that application
   server.

   If you are developing servlets or JSPs, and your application
   server is J2EE-compliant, you can put the driver's .jar file
   in the WEB-INF/lib subdirectory of your webapp, as this is a
   standard location for third party class libraries in J2EE web
   applications.

   You can also use the MysqlDataSource or
   MysqlConnectionPoolDataSource classes in the
   com.mysql.jdbc.jdbc2.optional package, if your J2EE
   application server supports or requires them. Starting with
   Connector/J 5.0.0, the javax.sql.XADataSource interface is
   implemented via the
   com.mysql.jdbc.jdbc2.optional.MysqlXADataSource class, which
   supports XA distributed transactions when used in combination
   with MySQL server version 5.0.

   The various MysqlDataSource classes support the following
   parameters (through standard set mutators):
     * user
     * password
     * serverName (see the previous section about fail-over
       hosts)
     * databaseName
     * port

1.2.3. Upgrading from an Older Version

   MySQL AB tries to keep the upgrade process as easy as
   possible, however as is the case with any software, sometimes
   changes need to be made in new versions to support new
   features, improve existing functionality, or comply with new
   standards.

   This section has information about what users who are
   upgrading from one version of Connector/J to another (or to a
   new version of the MySQL server, with respect to JDBC
   functionality) should be aware of.

1.2.3.1. Upgrading from MySQL Connector/J 3.0 to 3.1

   Connector/J 3.1 is designed to be backward-compatible with
   Connector/J 3.0 as much as possible. Major changes are
   isolated to new functionality exposed in MySQL-4.1 and newer,
   which includes Unicode character sets, server-side prepared
   statements, SQLState codes returned in error messages by the
   server and various performance enhancements that can be
   enabled or disabled via configuration properties.
     * Unicode Character Sets --- See the next section, as well
       as Character Set Support
       (http://dev.mysql.com/doc/refman/5.0/en/charset.html),
       for information on this new feature of MySQL. If you have
       something misconfigured, it will usually show up as an
       error with a message similar to Illegal mix of
       collations.
     * Server-side Prepared Statements --- Connector/J 3.1 will
       automatically detect and use server-side prepared
       statements when they are available (MySQL server version
       4.1.0 and newer).
       Starting with version 3.1.7, the driver scans SQL you are
       preparing via all variants of
       Connection.prepareStatement() to determine if it is a
       supported type of statement to prepare on the server
       side, and if it is not supported by the server, it
       instead prepares it as a client-side emulated prepared
       statement. You can disable this feature by passing
       emulateUnsupportedPstmts=false in your JDBC URL.
       If your application encounters issues with server-side
       prepared statements, you can revert to the older
       client-side emulated prepared statement code that is
       still presently used for MySQL servers older than 4.1.0
       with the connection property useServerPrepStmts=false
     * Datetimes with all-zero components (0000-00-00 ...) ---
       These values can not be represented reliably in Java.
       Connector/J 3.0.x always converted them to NULL when
       being read from a ResultSet.
       Connector/J 3.1 throws an exception by default when these
       values are encountered as this is the most correct
       behavior according to the JDBC and SQL standards. This
       behavior can be modified using the zeroDateTimeBehavior
       configuration property. The allowable values are:
          + exception (the default), which throws an
            SQLException with an SQLState of S1009.
          + convertToNull, which returns NULL instead of the
            date.
          + round, which rounds the date to the nearest closest
            value which is 0001-01-01.
       Starting with Connector/J 3.1.7, ResultSet.getString()
       can be decoupled from this behavior via
       noDatetimeStringSync=true (the default value is false) so
       that you can get retrieve the unaltered all-zero value as
       a String. It should be noted that this also precludes
       using any time zone conversions, therefore the driver
       will not allow you to enable noDatetimeStringSync and
       useTimezone at the same time.
     * New SQLState Codes --- Connector/J 3.1 uses SQL:1999
       SQLState codes returned by the MySQL server (if
       supported), which are different from the legacy X/Open
       state codes that Connector/J 3.0 uses. If connected to a
       MySQL server older than MySQL-4.1.0 (the oldest version
       to return SQLStates as part of the error code), the
       driver will use a built-in mapping. You can revert to the
       old mapping by using the configuration property
       useSqlStateCodes=false.
     * ResultSet.getString() --- Calling ResultSet.getString()
       on a BLOB column will now return the address of the
       byte[] array that represents it, instead of a String
       representation of the BLOB. BLOBs have no character set,
       so they can't be converted to java.lang.Strings without
       data loss or corruption.
       To store strings in MySQL with LOB behavior, use one of
       the TEXT types, which the driver will treat as a
       java.sql.Clob.
     * Debug builds --- Starting with Connector/J 3.1.8 a debug
       build of the driver in a file named
       mysql-connector-java-[version]-bin-g.jar is shipped
       alongside the normal binary jar file that is named
       mysql-connector-java-[version]-bin.jar.
       Starting with Connector/J 3.1.9, we don't ship the .class
       files unbundled, they are only available in the JAR
       archives that ship with the driver.
       You should not use the debug build of the driver unless
       instructed to do so when reporting a problem or bug to
       MySQL AB, as it is not designed to be run in production
       environments, and will have adverse performance impact
       when used. The debug binary also depends on the Aspect/J
       runtime library, which is located in the
       src/lib/aspectjrt.jar file that comes with the
       Connector/J distribution.

1.2.3.2. JDBC-Specific Issues When Upgrading to MySQL Server 4.1 or
Newer

     * Using the UTF-8 Character Encoding - Prior to MySQL
       server version 4.1, the UTF-8 character encoding was not
       supported by the server, however the JDBC driver could
       use it, allowing storage of multiple character sets in
       latin1 tables on the server.
       Starting with MySQL-4.1, this functionality is
       deprecated. If you have applications that rely on this
       functionality, and can not upgrade them to use the
       official Unicode character support in MySQL server
       version 4.1 or newer, you should add the following
       property to your connection URL:
       useOldUTF8Behavior=true
     * Server-side Prepared Statements - Connector/J 3.1 will
       automatically detect and use server-side prepared
       statements when they are available (MySQL server version
       4.1.0 and newer). If your application encounters issues
       with server-side prepared statements, you can revert to
       the older client-side emulated prepared statement code
       that is still presently used for MySQL servers older than
       4.1.0 with the following connection property:
       useServerPrepStmts=false

1.2.4. Installing from the Development Source Tree

Caution

   You should read this section only if you are interested in
   helping us test our new code. If you just want to get MySQL
   Connector/J up and running on your system, you should use a
   standard release distribution.

   To install MySQL Connector/J from the development source
   tree, make sure that you have the following prerequisites:
     * Subversion, to check out the sources from our repository
       (available from http://subversion.tigris.org/).
     * Apache Ant version 1.6 or newer (available from
       http://ant.apache.org/).
     * JDK-1.4.2 or later. Although MySQL Connector/J can be
       installed on older JDKs, to compile it from source you
       must have at least JDK-1.4.2.

   The Subversion source code repository for MySQL Connector/J
   is located at http://svn.mysql.com/svnpublic/connector-j. In
   general, you should not check out the entire repository
   because it contains every branch and tag for MySQL
   Connector/J and is quite large.

   To check out and compile a specific branch of MySQL
   Connector/J, follow these steps:
    1. At the time of this writing, there are three active
       branches of Connector/J: branch_3_0, branch_3_1 and
       branch_5_0. Check out the latest code from the branch
       that you want with the following command (replacing
       [major] and [minor] with appropriate version numbers):
shell> svn co �
http://svn.mysql.com/svnpublic/connector-j/branches/branch_[major]_[mi
nor]/connector-j
       This creates a connector-j subdirectory in the current
       directory that contains the latest sources for the
       requested branch.
    2. Change location to the connector-j directory to make it
       your current working directory:
shell> cd connector-j
    3. Issue the following command to compile the driver and
       create a .jar file suitable for installation:
shell> ant dist
       This creates a build directory in the current directory,
       where all build output will go. A directory is created in
       the build directory that includes the version number of
       the sources you are building from. This directory
       contains the sources, compiled .class files, and a .jar
       file suitable for deployment. For other possible targets,
       including ones that will create a fully packaged
       distribution, issue the following command:
shell> ant --projecthelp
    4. A newly created .jar file containing the JDBC driver will
       be placed in the directory
       build/mysql-connector-java-[version].
       Install the newly created JDBC driver as you would a
       binary .jar file that you download from MySQL by
       following the instructions in Section Section,
       "Installing the Driver and Configuring the CLASSPATH."

1.3. Connector/J Examples

   Examples of using Connector/J are located throughout this
   document, this section provides a summary and links to these
   examples.
     * Example Section, "Obtaining a connection from the
       DriverManager"
     * Example Section, "Using java.sql.Statement to execute a
       SELECT query"
     * Example Section, "Stored Procedures"
     * Example Section, "Using Connection.prepareCall()"
     * Example Section, "Registering output parameters"
     * Example Section, "Setting CallableStatement input
       parameters"
     * Example Section, "Retrieving results and output parameter
       values"
     * Example Section, "Retrieving AUTO_INCREMENT column values
       using Statement.getGeneratedKeys()"
     * Example Section, "Retrieving AUTO_INCREMENT column values
       using SELECT LAST_INSERT_ID()"
     * Example Section, "Retrieving AUTO_INCREMENT column values
       in Updatable ResultSets"
     * Example Section, "Using a connection pool with a J2EE
       application server"
     * Example Section, "Example of transaction with retry
       logic"

1.4. Connector/J (JDBC) Reference

   This section of the manual contains reference material for
   MySQL Connector/J, some of which is automatically generated
   during the Connector/J build process.

1.4.1. Driver/Datasource Class Names, URL Syntax and Configuration
Properties for Connector/J

   The name of the class that implements java.sql.Driver in
   MySQL Connector/J is com.mysql.jdbc.Driver. The
   org.gjt.mm.mysql.Driver class name is also usable to remain
   backward-compatible with MM.MySQL. You should use this class
   name when registering the driver, or when otherwise
   configuring software to use MySQL Connector/J.

   The JDBC URL format for MySQL Connector/J is as follows, with
   items in square brackets ([, ]) being optional:
jdbc:mysql://[host][,failoverhost...][:port]/[database] �
[?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]...

   If the hostname is not specified, it defaults to 127.0.0.1.
   If the port is not specified, it defaults to 3306, the
   default port number for MySQL servers.
jdbc:mysql://[host:port],[host:port].../[database] �
[?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]...

   If the database is not specified, the connection will be made
   with no default database. In this case, you will need to
   either call the setCatalog() method on the Connection
   instance or fully-specify table names using the database name
   (i.e. SELECT dbname.tablename.colname FROM
   dbname.tablename...) in your SQL. Not specifying the database
   to use upon connection is generally only useful when building
   tools that work with multiple databases, such as GUI database
   managers.

   MySQL Connector/J has fail-over support. This allows the
   driver to fail-over to any number of slave hosts and still
   perform read-only queries. Fail-over only happens when the
   connection is in an autoCommit(true) state, because fail-over
   can not happen reliably when a transaction is in progress.
   Most application servers and connection pools set autoCommit
   to true at the end of every transaction/connection use.

   The fail-over functionality has the following behavior:
     * If the URL property autoReconnect is false: Failover only
       happens at connection initialization, and failback occurs
       when the driver determines that the first host has become
       available again.
     * If the URL property autoReconnect is true: Failover
       happens when the driver determines that the connection
       has failed (before every query), and falls back to the
       first host when it determines that the host has become
       available again (after queriesBeforeRetryMaster queries
       have been issued).

   In either case, whenever you are connected to a "failed-over"
   server, the connection will be set to read-only state, so
   queries that would modify data will have exceptions thrown
   (the query will never be processed by the MySQL server).

   Configuration properties define how Connector/J will make a
   connection to a MySQL server. Unless otherwise noted,
   properties can be set for a DataSource object or for a
   Connection object.

   Configuration Properties can be set in one of the following
   ways:
     * Using the set*() methods on MySQL implementations of
       java.sql.DataSource (which is the preferred method when
       using implementations of java.sql.DataSource):
          + com.mysql.jdbc.jdbc2.optional.MysqlDataSource
          + com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDat
            aSource
     * As a key/value pair in the java.util.Properties instance
       passed to DriverManager.getConnection() or
       Driver.connect()
     * As a JDBC URL parameter in the URL given to
       java.sql.DriverManager.getConnection(),
       java.sql.Driver.connect() or the MySQL implementations of
       the javax.sql.DataSource setURL() method.

Note
       If the mechanism you use to configure a JDBC URL is
       XML-based, you will need to use the XML character literal
       &amp; to separate configuration parameters, as the
       ampersand is a reserved character for XML.

   The properties are listed in the following tables.

   Connection/Authentication.
   Property Name Definition Default Value Since Version
   user The user to connect as   all versions
   password The password to use when connecting   all versions
   socketFactory The name of the class that the driver should
   use for creating socket connections to the server. This class
   must implement the interface 'com.mysql.jdbc.SocketFactory'
   and have public no-args constructor.
   com.mysql.jdbc.StandardSocketFactory 3.0.3
   connectTimeout Timeout for socket connect (in milliseconds),
   with 0 being no timeout. Only works on JDK-1.4 or newer.
   Defaults to '0'. 0 3.0.1
   socketTimeout Timeout on network socket operations (0, the
   default means no timeout). 0 3.0.1
   connectionLifecycleInterceptors A comma-delimited list of
   classes that implement
   "com.mysql.jdbc.ConnectionLifecycleInterceptor" that should
   notified of connection lifecycle events (creation,
   destruction, commit, rollback, setCatalog and setAutoCommit)
   and potentially alter the execution of these commands.
   ConnectionLifecycleInterceptors are "stackable", more than
   one interceptor may be specified via the configuration
   property as a comma-delimited list, with the interceptors
   executed in order from left to right.   5.1.4
   useConfigs Load the comma-delimited list of configuration
   properties before parsing the URL or applying user-specified
   properties. These configurations are explained in the
   'Configurations' of the documentation.   3.1.5
   interactiveClient Set the CLIENT_INTERACTIVE flag, which
   tells MySQL to timeout connections based on
   INTERACTIVE_TIMEOUT instead of WAIT_TIMEOUT false 3.1.0
   localSocketAddress Hostname or IP address given to explicitly
   configure the interface that the driver will bind the client
   side of the TCP/IP connection to when connecting.   5.0.5
   propertiesTransform An implementation of
   com.mysql.jdbc.ConnectionPropertiesTransform that the driver
   will use to modify URL properties passed to the driver before
   attempting a connection   3.1.4
   useCompression Use zlib compression when communicating with
   the server (true/false)? Defaults to 'false'. false 3.0.17

   Networking.
   Property Name Definition Default Value Since Version
   tcpKeepAlive If connecting using TCP/IP, should the driver
   set SO_KEEPALIVE? true 5.0.7
   tcpNoDelay If connecting using TCP/IP, should the driver set
   SO_TCP_NODELAY (disabling the Nagle Algorithm)? true 5.0.7
   tcpRcvBuf If connecting using TCP/IP, should the driver set
   SO_RCV_BUF to the given value? The default value of '0',
   means use the platform default value for this property) 0
   5.0.7
   tcpSndBuf If connecting using TCP/IP, shuold the driver set
   SO_SND_BUF to the given value? The default value of '0',
   means use the platform default value for this property) 0
   5.0.7
   tcpTrafficClass If connecting using TCP/IP, should the driver
   set traffic class or type-of-service fields ?See the
   documentation for java.net.Socket.setTrafficClass() for more
   information. 0 5.0.7

   High Availability and Clustering.
   Property Name Definition Default Value Since Version
   autoReconnect Should the driver try to re-establish stale
   and/or dead connections? If enabled the driver will throw an
   exception for a queries issued on a stale or dead connection,
   which belong to the current transaction, but will attempt
   reconnect before the next query issued on the connection in a
   new transaction. The use of this feature is not recommended,
   because it has side effects related to session state and data
   consistency when applications don't handle SQLExceptions
   properly, and is only designed to be used when you are unable
   to configure your application to handle SQLExceptions
   resulting from dead and stale connections properly.
   Alternatively, investigate setting the MySQL server variable
   "wait_timeout" to some high value rather than the default of
   8 hours. false 1.1
   autoReconnectForPools Use a reconnection strategy appropriate
   for connection pools (defaults to 'false') false 3.1.3
   failOverReadOnly When failing over in autoReconnect mode,
   should the connection be set to 'read-only'? true 3.0.12
   maxReconnects Maximum number of reconnects to attempt if
   autoReconnect is true, default is '3'. 3 1.1
   reconnectAtTxEnd If autoReconnect is set to true, should the
   driver attempt reconnections at the end of every transaction?
   false 3.0.10
   initialTimeout If autoReconnect is enabled, the initial time
   to wait between re-connect attempts (in seconds, defaults to
   '2'). 2 1.1
   roundRobinLoadBalance When autoReconnect is enabled, and
   failoverReadonly is false, should we pick hosts to connect to
   on a round-robin basis? false 3.1.2
   queriesBeforeRetryMaster Number of queries to issue before
   falling back to master when failed over (when using
   multi-host failover). Whichever condition is met first,
   'queriesBeforeRetryMaster' or 'secondsBeforeRetryMaster' will
   cause an attempt to be made to reconnect to the master.
   Defaults to 50. 50 3.0.2
   secondsBeforeRetryMaster How long should the driver wait,
   when failed over, before attempting 30 3.0.2
   resourceId A globally unique name that identifies the
   resource that this datasource or connection is connected to,
   used for XAResource.isSameRM() when the driver can't
   determine this value based on hostnames used in the URL
   5.0.1

   Security.
   Property Name Definition Default Value Since Version
   allowMultiQueries Allow the use of ';' to delimit multiple
   queries during one statement (true/false), defaults to
   'false' false 3.1.1
   useSSL Use SSL when communicating with the server
   (true/false), defaults to 'false' false 3.0.2
   requireSSL Require SSL connection if useSSL=true? (defaults
   to 'false'). false 3.1.0
   allowLoadLocalInfile Should the driver allow use of 'LOAD
   DATA LOCAL INFILE...' (defaults to 'true'). true 3.0.3
   allowUrlInLocalInfile Should the driver allow URLs in 'LOAD
   DATA LOCAL INFILE' statements? false 3.1.4
   clientCertificateKeyStorePassword Password for the client
   certificates KeyStore   5.1.0
   clientCertificateKeyStoreType KeyStore type for client
   certificates (NULL or empty means use default, standard
   keystore types supported by the JVM are "JKS" and "PKCS12",
   your environment may have more available depending on what
   security products are installed and available to the JVM.
   5.1.0
   clientCertificateKeyStoreUrl URL to the client certificate
   KeyStore (if not specified, use defaults)   5.1.0
   trustCertificateKeyStorePassword Password for the trusted
   root certificates KeyStore   5.1.0
   trustCertificateKeyStoreType KeyStore type for trusted root
   certificates (NULL or empty means use default, standard
   keystore types supported by the JVM are "JKS" and "PKCS12",
   your environment may have more available depending on what
   security products are installed and available to the JVM.
   5.1.0
   trustCertificateKeyStoreUrl URL to the trusted root
   certificate KeyStore (if not specified, use defaults)   5.1.0
   paranoid Take measures to prevent exposure sensitive
   information in error messages and clear data structures
   holding sensitive data when possible? (defaults to 'false')
   false 3.0.1

   Performance Extensions.
   Property Name Definition Default Value Since Version
   callableStmtCacheSize If 'cacheCallableStmts' is enabled, how
   many callable statements should be cached? 100 3.1.2
   metadataCacheSize The number of queries to cache
   ResultSetMetadata for if cacheResultSetMetaData is set to
   'true' (default 50) 50 3.1.1
   prepStmtCacheSize If prepared statement caching is enabled,
   how many prepared statements should be cached? 25 3.0.10
   prepStmtCacheSqlLimit If prepared statement caching is
   enabled, what's the largest SQL the driver will cache the
   parsing for? 256 3.0.10
   alwaysSendSetIsolation Should the driver always communicate
   with the database when Connection.setTransactionIsolation()
   is called? If set to false, the driver will only communicate
   with the database when the requested transaction isolation is
   different than the whichever is newer, the last value that
   was set via Connection.setTransactionIsolation(), or the
   value that was read from the server when the connection was
   established. true 3.1.7
   maintainTimeStats Should the driver maintain various internal
   timers to enable idle time calculations as well as more
   verbose error messages when the connection to the server
   fails? Setting this property to false removes at least two
   calls to System.getCurrentTimeMillis() per query. true 3.1.9
   useCursorFetch If connected to MySQL > 5.0.2, and
   setFetchSize() > 0 on a statement, should that statement use
   cursor-based fetching to retrieve rows? false 5.0.0
   blobSendChunkSize Chunk to use when sending BLOB/CLOBs via
   ServerPreparedStatements 1048576 3.1.9
   cacheCallableStmts Should the driver cache the parsing stage
   of CallableStatements false 3.1.2
   cachePrepStmts Should the driver cache the parsing stage of
   PreparedStatements of client-side prepared statements, the
   "check" for suitability of server-side prepared and
   server-side prepared statements themselves? false 3.0.10
   cacheResultSetMetadata Should the driver cache
   ResultSetMetaData for Statements and PreparedStatements?
   (Req. JDK-1.4+, true/false, default 'false') false 3.1.1
   cacheServerConfiguration Should the driver cache the results
   of 'SHOW VARIABLES' and 'SHOW COLLATION' on a per-URL basis?
   false 3.1.5
   defaultFetchSize The driver will call setFetchSize(n) with
   this value on all newly-created Statements 0 3.1.9
   dontTrackOpenResources The JDBC specification requires the
   driver to automatically track and close resources, however if
   your application doesn't do a good job of explicitly calling
   close() on statements or result sets, this can cause memory
   leakage. Setting this property to true relaxes this
   constraint, and can be more memory efficient for some
   applications. false 3.1.7
   dynamicCalendars Should the driver retrieve the default
   calendar when required, or cache it per connection/session?
   false 3.1.5
   elideSetAutoCommits If using MySQL-4.1 or newer, should the
   driver only issue 'set autocommit=n' queries when the
   server's state doesn't match the requested state by
   Connection.setAutoCommit(boolean)? false 3.1.3
   enableQueryTimeouts When enabled, query timeouts set via
   Statement.setQueryTimeout() use a shared java.util.Timer
   instance for scheduling. Even if the timeout doesn't expire
   before the query is processed, there will be memory used by
   the TimerTask for the given timeout which won't be reclaimed
   until the time the timeout would have expired if it hadn't
   been cancelled by the driver. High-load environments might
   want to consider disabling this functionality. true 5.0.6
   holdResultsOpenOverStatementClose Should the driver close
   result sets on Statement.close() as required by the JDBC
   specification? false 3.1.7
   largeRowSizeThreshold What size result set row should the
   JDBC driver consider "large", and thus use a more
   memory-efficient way of representing the row internally? 2048
   5.1.1
   loadBalanceStrategy If using a load-balanced connection to
   connect to SQL nodes in a MySQL Cluster/NDB configuration (by
   using the URL prefix "jdbc:mysql:loadbalance://"), which load
   balancing algorithm should the driver use: (1) "random" - the
   driver will pick a random host for each request. This tends
   to work better than round-robin, as the randomness will
   somewhat account for spreading loads where requests vary in
   response time, while round-robin can sometimes lead to
   overloaded nodes if there are variations in response times
   across the workload. (2) "bestResponseTime" - the driver will
   route the request to the host that had the best response time
   for the previous transaction. random 5.0.6
   locatorFetchBufferSize If 'emulateLocators' is configured to
   'true', what size buffer should be used when fetching BLOB
   data for getBinaryInputStream? 1048576 3.2.1
   rewriteBatchedStatements Should the driver use multiqueries
   (irregardless of the setting of "allowMultiQueries") as well
   as rewriting of prepared statements for INSERT into
   multi-value inserts when executeBatch() is called? Notice
   that this has the potential for SQL injection if using plain
   java.sql.Statements and your code doesn't sanitize input
   correctly. Notice that for prepared statements, server-side
   prepared statements can not currently take advantage of this
   rewrite option, and that if you don't specify stream lengths
   when using PreparedStatement.set*Stream(), the driver won't
   be able to determine the optimum number of parameters per
   batch and you might receive an error from the driver that the
   resultant packet is too large. Statement.getGeneratedKeys()
   for these rewritten statements only works when the entire
   batch includes INSERT statements. false 3.1.13
   useDirectRowUnpack Use newer result set row unpacking code
   that skips a copy from network buffers to a MySQL packet
   instance and instead reads directly into the result set row
   data buffers. true 5.1.1
   useDynamicCharsetInfo Should the driver use a per-connection
   cache of character set information queried from the server
   when necessary, or use a built-in static mapping that is more
   efficient, but isn't aware of custom character sets or
   character sets implemented after the release of the JDBC
   driver? true 5.0.6
   useFastDateParsing Use internal String->Date/Time/Timestamp
   conversion routines to avoid excessive object creation? true
   5.0.5
   useFastIntParsing Use internal String->Integer conversion
   routines to avoid excessive object creation? true 3.1.4
   useJvmCharsetConverters Always use the character encoding
   routines built into the JVM, rather than using lookup tables
   for single-byte character sets? false 5.0.1
   useLocalSessionState Should the driver refer to the internal
   values of autocommit and transaction isolation that are set
   by Connection.setAutoCommit() and
   Connection.setTransactionIsolation() and transaction state as
   maintained by the protocol, rather than querying the database
   or blindly sending commands to the database for commit() or
   rollback() method calls? false 3.1.7
   useReadAheadInput Use newer, optimized non-blocking, buffered
   input stream when reading from the server? true 3.1.5

   Debugging/Profiling.
   Property Name Definition Default Value Since Version
   logger The name of a class that implements
   "com.mysql.jdbc.log.Log" that will be used to log messages
   to. (default is "com.mysql.jdbc.log.StandardLogger", which
   logs to STDERR) com.mysql.jdbc.log.StandardLogger 3.1.1
   gatherPerfMetrics Should the driver gather performance
   metrics, and report them via the configured logger every
   'reportMetricsIntervalMillis' milliseconds? false 3.1.2
   profileSQL Trace queries and their execution/fetch times to
   the configured logger (true/false) defaults to 'false' false
   3.1.0
   profileSql Deprecated, use 'profileSQL' instead. Trace
   queries and their execution/fetch times on STDERR
   (true/false) defaults to 'false'   2.0.14
   reportMetricsIntervalMillis If 'gatherPerfMetrics' is
   enabled, how often should they be logged (in ms)? 30000 3.1.2
   maxQuerySizeToLog Controls the maximum length/size of a query
   that will get logged when profiling or tracing 2048 3.1.3
   packetDebugBufferSize The maximum number of packets to retain
   when 'enablePacketDebug' is true 20 3.1.3
   slowQueryThresholdMillis If 'logSlowQueries' is enabled, how
   long should a query (in ms) before it is logged as 'slow'?
   2000 3.1.2
   slowQueryThresholdNanos If 'useNanosForElapsedTime' is set to
   true, and this property is set to a non-zero value, the
   driver will use this threshold (in nanosecond units) to
   determine if a query was slow. 0 5.0.7
   useUsageAdvisor Should the driver issue 'usage' warnings
   advising proper and efficient usage of JDBC and MySQL
   Connector/J to the log (true/false, defaults to 'false')?
   false 3.1.1
   autoGenerateTestcaseScript Should the driver dump the SQL it
   is executing, including server-side prepared statements to
   STDERR? false 3.1.9
   autoSlowLog Instead of using slowQueryThreshold* to determine
   if a query is slow enough to be logged, maintain statistics
   that allow the driver to determine queries that are outside
   the 99th percentile? true 5.1.4
   clientInfoProvider The name of a class that implements the
   com.mysql.jdbc.JDBC4ClientInfoProvider interface in order to
   support JDBC-4.0's Connection.get/setClientInfo() methods
   com.mysql.jdbc.JDBC4CommentClientInfoProvider 5.1.0
   dumpMetadataOnColumnNotFound Should the driver dump the
   field-level metadata of a result set into the exception
   message when ResultSet.findColumn() fails? false 3.1.13
   dumpQueriesOnException Should the driver dump the contents of
   the query sent to the server in the message for
   SQLExceptions? false 3.1.3
   enablePacketDebug When enabled, a ring-buffer of
   'packetDebugBufferSize' packets will be kept, and dumped when
   exceptions are thrown in key areas in the driver's code false
   3.1.3
   explainSlowQueries If 'logSlowQueries' is enabled, should the
   driver automatically issue an 'EXPLAIN' on the server and
   send the results to the configured log at a WARN level? false
   3.1.2
   includeInnodbStatusInDeadlockExceptions Include the output of
   "SHOW ENGINE INNODB STATUS" in exception messages when
   deadlock exceptions are detected? false 5.0.7
   logSlowQueries Should queries that take longer than
   'slowQueryThresholdMillis' be logged? false 3.1.2
   logXaCommands Should the driver log XA commands sent by
   MysqlXaConnection to the server, at the DEBUG level of
   logging? false 5.0.5
   resultSetSizeThreshold If the usage advisor is enabled, how
   many rows should a result set contain before the driver warns
   that it is suspiciously large? 100 5.0.5
   traceProtocol Should trace-level network protocol be logged?
   false 3.1.2
   useNanosForElapsedTime For profiling/debugging functionality
   that measures elapsed time, should the driver try to use
   nanoseconds resolution if available (JDK >= 1.5)? false 5.0.7

   Miscellaneous.
   Property Name Definition Default Value Since Version
   useUnicode Should the driver use Unicode character encodings
   when handling strings? Should only be used when the driver
   can't determine the character set mapping, or you are trying
   to 'force' the driver to use a character set that MySQL
   either doesn't natively support (such as UTF-8), true/false,
   defaults to 'true' true 1.1g
   characterEncoding If 'useUnicode' is set to true, what
   character encoding should the driver use when dealing with
   strings? (defaults is to 'autodetect')   1.1g
   characterSetResults Character set to tell the server to
   return results as.   3.0.13
   connectionCollation If set, tells the server to use this
   collation via 'set collation_connection'   3.0.13
   useBlobToStoreUTF8OutsideBMP Tells the driver to treat
   [MEDIUM/LONG]BLOB columns as [LONG]VARCHAR columns holding
   text encoded in UTF-8 that has characters outside the BMP
   (4-byte encodings), which MySQL server can't handle natively.
   false 5.1.3
   utf8OutsideBmpExcludedColumnNamePattern When
   "useBlobToStoreUTF8OutsideBMP" is set to "true", column names
   matching the given regex will still be treated as BLOBs
   unless they match the regex specified for
   "utf8OutsideBmpIncludedColumnNamePattern". The regex must
   follow the patterns used for the java.util.regex package.
   5.1.3
   utf8OutsideBmpIncludedColumnNamePattern Used to specify
   exclusion rules to "utf8OutsideBmpExcludedColumnNamePattern".
   The regex must follow the patterns used for the
   java.util.regex package.   5.1.3
   sessionVariables A comma-separated list of name/value pairs
   to be sent as SET SESSION ... to the server when the driver
   connects.   3.1.8
   allowNanAndInf Should the driver allow NaN or +/- INF values
   in PreparedStatement.setDouble()? false 3.1.5
   autoClosePStmtStreams Should the driver automatically call
   .close() on streams/readers passed as arguments via set*()
   methods? false 3.1.12
   autoDeserialize Should the driver automatically detect and
   de-serialize objects stored in BLOB fields? false 3.1.5
   blobsAreStrings Should the driver always treat BLOBs as
   Strings - specifically to work around dubious metadata
   returned by the server for GROUP BY clauses? false 5.0.8
   capitalizeTypeNames Capitalize type names in
   DatabaseMetaData? (usually only useful when using WebObjects,
   true/false, defaults to 'false') true 2.0.7
   clobCharacterEncoding The character encoding to use for
   sending and retrieving TEXT, MEDIUMTEXT and LONGTEXT values
   instead of the configured connection characterEncoding
   5.0.0
   clobberStreamingResults This will cause a 'streaming'
   ResultSet to be automatically closed, and any outstanding
   data still streaming from the server to be discarded if
   another query is executed before all the data has been read
   from the server. false 3.0.9
   continueBatchOnError Should the driver continue processing
   batch commands if one statement fails. The JDBC spec allows
   either way (defaults to 'true'). true 3.0.3
   createDatabaseIfNotExist Creates the database given in the
   URL if it doesn't yet exist. Assumes the configured user has
   permissions to create databases. false 3.1.9
   emptyStringsConvertToZero Should the driver allow conversions
   from empty string fields to numeric values of '0'? true 3.1.8
   emulateLocators Should the driver emulate java.sql.Blobs with
   locators? With this feature enabled, the driver will delay
   loading the actual Blob data until the one of the retrieval
   methods (getInputStream(), getBytes(), and so forth) on the
   blob data stream has been accessed. For this to work, you
   must use a column alias with the value of the column to the
   actual name of the Blob. The feature also has the following
   restrictions: The SELECT that created the result set must
   reference only one table, the table must have a primary key;
   the SELECT must alias the original blob column name,
   specified as a string, to an alternate name; the SELECT must
   cover all columns that make up the primary key. false 3.1.0
   emulateUnsupportedPstmts Should the driver detect prepared
   statements that are not supported by the server, and replace
   them with client-side emulated versions? true 3.1.7
   functionsNeverReturnBlobs Should the driver always treat data
   from functions returning BLOBs as Strings - specifically to
   work around dubious metadata returned by the server for GROUP
   BY clauses? false 5.0.8
   generateSimpleParameterMetadata Should the driver generate
   simplified parameter metadata for PreparedStatements when no
   metadata is available either because the server couldn't
   support preparing the statement, or server-side prepared
   statements are disabled? false 5.0.5
   ignoreNonTxTables Ignore non-transactional table warning for
   rollback? (defaults to 'false'). false 3.0.9
   jdbcCompliantTruncation Should the driver throw
   java.sql.DataTruncation exceptions when data is truncated as
   is required by the JDBC specification when connected to a
   server that supports warnings (MySQL 4.1.0 and newer)? This
   property has no effect if the server sql-mode includes
   STRICT_TRANS_TABLES. true 3.1.2
   maxRows The maximum number of rows to return (0, the default
   means return all rows). -1 all versions
   netTimeoutForStreamingResults What value should the driver
   automatically set the server setting 'net_write_timeout' to
   when the streaming result sets feature is in use? (value has
   unit of seconds, the value '0' means the driver will not try
   and adjust this value) 600 5.1.0
   noAccessToProcedureBodies When determining procedure
   parameter types for CallableStatements, and the connected
   user can't access procedure bodies through "SHOW CREATE
   PROCEDURE" or select on mysql.proc should the driver instead
   create basic metadata (all parameters reported as IN
   VARCHARs, but allowing registerOutParameter() to be called on
   them anyway) instead of throwing an exception? false 5.0.3
   noDatetimeStringSync Don't ensure that
   ResultSet.getDatetimeType().toString().equals(ResultSet.getSt
   ring()) false 3.1.7
   noTimezoneConversionForTimeType Don't convert TIME values
   using the server timezone if 'useTimezone'='true' false 5.0.0
   nullCatalogMeansCurrent When DatabaseMetadataMethods ask for
   a 'catalog' parameter, does the value null mean use the
   current catalog? (this is not JDBC-compliant, but follows
   legacy behavior from earlier versions of the driver) true
   3.1.8
   nullNamePatternMatchesAll Should DatabaseMetaData methods
   that accept *pattern parameters treat null the same as '%'
   (this is not JDBC-compliant, however older versions of the
   driver accepted this departure from the specification) true
   3.1.8
   overrideSupportsIntegrityEnhancementFacility Should the
   driver return "true" for
   DatabaseMetaData.supportsIntegrityEnhancementFacility() even
   if the database doesn't support it to workaround applications
   that require this method to return "true" to signal support
   of foreign keys, even though the SQL specification states
   that this facility contains much more than just foreign key
   support (one such application being OpenOffice)? false 3.1.12
   padCharsWithSpace If a result set column has the CHAR type
   and the value does not fill the amount of characters
   specified in the DDL for the column, should the driver pad
   the remaining characters with space (for ANSI compliance)?
   false 5.0.6
   pedantic Follow the JDBC spec to the letter. false 3.0.0
   pinGlobalTxToPhysicalConnection When using XAConnections,
   should the driver ensure that operations on a given XID are
   always routed to the same physical connection? This allows
   the XAConnection to support "XA START ... JOIN" after "XA
   END" has been called false 5.0.1
   populateInsertRowWithDefaultValues When using ResultSets that
   are CONCUR_UPDATABLE, should the driver pre-populate the
   "insert" row with default values from the DDL for the table
   used in the query so those values are immediately available
   for ResultSet accessors? This functionality requires a call
   to the database for metadata each time a result set of this
   type is created. If disabled (the default), the default
   values will be populated by the an internal call to
   refreshRow() which pulls back default values and/or values
   changed by triggers. false 5.0.5
   processEscapeCodesForPrepStmts Should the driver process
   escape codes in queries that are prepared? true 3.1.12
   relaxAutoCommit If the version of MySQL the driver connects
   to does not support transactions, still allow calls to
   commit(), rollback() and setAutoCommit() (true/false,
   defaults to 'false')? false 2.0.13
   retainStatementAfterResultSetClose Should the driver retain
   the Statement reference in a ResultSet after
   ResultSet.close() has been called. This is not JDBC-compliant
   after JDBC-4.0. false 3.1.11
   rollbackOnPooledClose Should the driver issue a rollback()
   when the logical connection in a pool is closed? true 3.0.15
   runningCTS13 Enables workarounds for bugs in Sun's JDBC
   compliance testsuite version 1.3 false 3.1.7
   serverTimezone Override detection/mapping of timezone. Used
   when timezone from server doesn't map to Java timezone
   3.0.2
   statementInterceptors A comma-delimited list of classes that
   implement "com.mysql.jdbc.StatementInterceptor" that should
   be placed "in between" query execution to influence the
   results. StatementInterceptors are "chainable", the results
   returned by the "current" interceptor will be passed on to
   the next in in the chain, from left-to-right order, as
   specified in this property.   5.1.1
   strictFloatingPoint Used only in older versions of compliance
   test false 3.0.0
   strictUpdates Should the driver do strict checking (all
   primary keys selected) of updatable result sets (true, false,
   defaults to 'true')? true 3.0.4
   tinyInt1isBit Should the driver treat the datatype TINYINT(1)
   as the BIT type (because the server silently converts BIT ->
   TINYINT(1) when creating tables)? true 3.0.16
   transformedBitIsBoolean If the driver converts TINYINT(1) to
   a different type, should it use BOOLEAN instead of BIT for
   future compatibility with MySQL-5.0, as MySQL-5.0 has a BIT
   type? false 3.1.9
   treatUtilDateAsTimestamp Should the driver treat
   java.util.Date as a TIMESTAMP for the purposes of
   PreparedStatement.setObject()? true 5.0.5
   ultraDevHack Create PreparedStatements for prepareCall() when
   required, because UltraDev is broken and issues a
   prepareCall() for _all_ statements? (true/false, defaults to
   'false') false 2.0.3
   useGmtMillisForDatetimes Convert between session timezone and
   GMT before creating Date and Timestamp instances (value of
   "false" is legacy behavior, "true" leads to more
   JDBC-compliant behavior. false 3.1.12
   useHostsInPrivileges Add '@hostname' to users in
   DatabaseMetaData.getColumn/TablePrivileges() (true/false),
   defaults to 'true'. true 3.0.2
   useInformationSchema When connected to MySQL-5.0.7 or newer,
   should the driver use the INFORMATION_SCHEMA to derive
   information used by DatabaseMetaData? false 5.0.0
   useJDBCCompliantTimezoneShift Should the driver use
   JDBC-compliant rules when converting TIME/TIMESTAMP/DATETIME
   values' timezone information for those JDBC arguments which
   take a java.util.Calendar argument? (Notice that this option
   is exclusive of the "useTimezone=true" configuration option.)
   false 5.0.0
   useOldAliasMetadataBehavior Should the driver use the legacy
   behavior for "AS" clauses on columns and tables, and only
   return aliases (if any) for ResultSetMetaData.getColumnName()
   or ResultSetMetaData.getTableName() rather than the original
   column/table name? false 5.0.4
   useOldUTF8Behavior Use the UTF-8 behavior the driver did when
   communicating with 4.0 and older servers false 3.1.6
   useOnlyServerErrorMessages Don't prepend 'standard' SQLState
   error messages to error messages returned by the server. true
   3.0.15
   useSSPSCompatibleTimezoneShift If migrating from an
   environment that was using server-side prepared statements,
   and the configuration property
   "useJDBCCompliantTimeZoneShift" set to "true", use compatible
   behavior when not using server-side prepared statements when
   sending TIMESTAMP values to the MySQL server. false 5.0.5
   useServerPrepStmts Use server-side prepared statements if the
   server supports them? false 3.1.0
   useSqlStateCodes Use SQL Standard state codes instead of
   'legacy' X/Open/SQL state codes (true/false), default is
   'true' true 3.1.3
   useStreamLengthsInPrepStmts Honor stream length parameter in
   PreparedStatement/ResultSet.setXXXStream() method calls
   (true/false, defaults to 'true')? true 3.0.2
   useTimezone Convert time/date types between client and server
   timezones (true/false, defaults to 'false')? false 3.0.2
   useUnbufferedInput Don't use BufferedInputStream for reading
   data from the server true 3.0.11
   yearIsDateType Should the JDBC driver treat the MySQL type
   "YEAR" as a java.sql.Date, or as a SHORT? true 3.1.9
   zeroDateTimeBehavior What should happen when the driver
   encounters DATETIME values that are composed entirely of
   zeroes (used by MySQL to represent invalid dates)? Valid
   values are "exception", "round" and "convertToNull".
   exception 3.1.4

   Connector/J also supports access to MySQL via named pipes on
   Windows NT/2000/XP using the NamedPipeSocketFactory as a
   plugin-socket factory via the socketFactory property. If you
   don't use a namedPipePath property, the default of
   '\\.\pipe\MySQL' will be used. If you use the
   NamedPipeSocketFactory, the hostname and port number values
   in the JDBC url will be ignored. You can enable this feature
   using:
socketFactory=com.mysql.jdbc.NamedPipeSocketFactory

   Named pipes only work when connecting to a MySQL server on
   the same physical machine as the one the JDBC driver is being
   used on. In simple performance tests, it appears that named
   pipe access is between 30%-50% faster than the standard
   TCP/IP access.

   You can create your own socket factories by following the
   example code in com.mysql.jdbc.NamedPipeSocketFactory, or
   com.mysql.jdbc.StandardSocketFactory.

1.4.2. JDBC API Implementation Notes

   MySQL Connector/J passes all of the tests in the
   publicly-available version of Sun's JDBC compliance test
   suite. However, in many places the JDBC specification is
   vague about how certain functionality should be implemented,
   or the specification allows leeway in implementation.

   This section gives details on a interface-by-interface level
   about how certain implementation decisions may affect how you
   use MySQL Connector/J.
     * Blob
       Starting with Connector/J version 3.1.0, you can emulate
       Blobs with locators by adding the property
       'emulateLocators=true' to your JDBC URL. Using this
       method, the driver will delay loading the actual Blob
       data until you retrieve the other data and then use
       retrieval methods (getInputStream(), getBytes(), and so
       forth) on the blob data stream.
       For this to work, you must use a column alias with the
       value of the column to the actual name of the Blob, for
       example:
SELECT id, 'data' as blob_data from blobtable
       For this to work, you must also follow follow these
       rules:
          + The SELECT must also reference only one table, the
            table must have a primary key.
          + The SELECT must alias the original blob column name,
            specified as a string, to an alternate name.
          + The SELECT must cover all columns that make up the
            primary key.
       The Blob implementation does not allow in-place
       modification (they are copies, as reported by the
       DatabaseMetaData.locatorsUpdateCopies() method). Because
       of this, you should use the corresponding
       PreparedStatement.setBlob() or ResultSet.updateBlob() (in
       the case of updatable result sets) methods to save
       changes back to the database.
       MySQL Enterprise MySQL Enterprise subscribers will find
       more information about type conversion in the Knowledge
       Base article, Type Conversions Supported by MySQL
       Connector/J (https://kb.mysql.com/view.php?id=4929). To
       subscribe to MySQL Enterprise see
       http://www.mysql.com/products/enterprise/advisors.html.
     * CallableStatement
       Starting with Connector/J 3.1.1, stored procedures are
       supported when connecting to MySQL version 5.0 or newer
       via the CallableStatement interface. Currently, the
       getParameterMetaData() method of CallableStatement is not
       supported.
     * Clob
       The Clob implementation does not allow in-place
       modification (they are copies, as reported by the
       DatabaseMetaData.locatorsUpdateCopies() method). Because
       of this, you should use the PreparedStatement.setClob()
       method to save changes back to the database. The JDBC API
       does not have a ResultSet.updateClob() method.
     * Connection
       Unlike older versions of MM.MySQL the isClosed() method
       does not ping the server to determine if it is alive. In
       accordance with the JDBC specification, it only returns
       true if closed() has been called on the connection. If
       you need to determine if the connection is still valid,
       you should issue a simple query, such as SELECT 1. The
       driver will throw an exception if the connection is no
       longer valid.
     * DatabaseMetaData
       Foreign Key information
       (getImportedKeys()/getExportedKeys() and
       getCrossReference()) is only available from InnoDB
       tables. However, the driver uses SHOW CREATE TABLE to
       retrieve this information, so when other storage engines
       support foreign keys, the driver will transparently
       support them as well.
     * PreparedStatement
       PreparedStatements are implemented by the driver, as
       MySQL does not have a prepared statement feature. Because
       of this, the driver does not implement
       getParameterMetaData() or getMetaData() as it would
       require the driver to have a complete SQL parser in the
       client.
       Starting with version 3.1.0 MySQL Connector/J,
       server-side prepared statements and binary-encoded result
       sets are used when the server supports them.
       Take care when using a server-side prepared statement
       with large parameters that are set via setBinaryStream(),
       setAsciiStream(), setUnicodeStream(), setBlob(), or
       setClob(). If you want to re-execute the statement with
       any large parameter changed to a non-large parameter, it
       is necessary to call clearParameters() and set all
       parameters again. The reason for this is as follows:
          + During both server-side prepared statements and
            client-side emulation, large data is exchanged only
            when PreparedStatement.execute() is called.
          + Once that has been done, the stream used to read the
            data on the client side is closed (as per the JDBC
            spec), and can't be read from again.
          + If a parameter changes from large to non-large, the
            driver must reset the server-side state of the
            prepared statement to allow the parameter that is
            being changed to take the place of the prior large
            value. This removes all of the large data that has
            already been sent to the server, thus requiring the
            data to be re-sent, via the setBinaryStream(),
            setAsciiStream(), setUnicodeStream(), setBlob() or
            setClob() methods.
       Consequently, if you want to change the type of a
       parameter to a non-large one, you must call
       clearParameters() and set all parameters of the prepared
       statement again before it can be re-executed.
     * ResultSet
       By default, ResultSets are completely retrieved and
       stored in memory. In most cases this is the most
       efficient way to operate, and due to the design of the
       MySQL network protocol is easier to implement. If you are
       working with ResultSets that have a large number of rows
       or large values, and can not allocate heap space in your
       JVM for the memory required, you can tell the driver to
       stream the results back one row at a time.
       To enable this functionality, you need to create a
       Statement instance in the following manner:
stmt = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
              java.sql.ResultSet.CONCUR_READ_ONLY);
stmt.setFetchSize(Integer.MIN_VALUE);
       The combination of a forward-only, read-only result set,
       with a fetch size of Integer.MIN_VALUE serves as a signal
       to the driver to stream result sets row-by-row. After
       this any result sets created with the statement will be
       retrieved row-by-row.
       There are some caveats with this approach. You will have
       to read all of the rows in the result set (or close it)
       before you can issue any other queries on the connection,
       or an exception will be thrown.
       The earliest the locks these statements hold can be
       released (whether they be MyISAM table-level locks or
       row-level locks in some other storage engine such as
       InnoDB) is when the statement completes.
       If the statement is within scope of a transaction, then
       locks are released when the transaction completes (which
       implies that the statement needs to complete first). As
       with most other databases, statements are not complete
       until all the results pending on the statement are read
       or the active result set for the statement is closed.
       Therefore, if using streaming results, you should process
       them as quickly as possible if you want to maintain
       concurrent access to the tables referenced by the
       statement producing the result set.
     * ResultSetMetaData
       The isAutoIncrement() method only works when using MySQL
       servers 4.0 and newer.
     * Statement
       When using versions of the JDBC driver earlier than
       3.2.1, and connected to server versions earlier than
       5.0.3, the setFetchSize() method has no effect, other
       than to toggle result set streaming as described above.
       Connector/J 5.0.0 and later include support for both
       Statement.cancel() and Statement.setQueryTimeout(). Both
       require MySQL 5.0.0 or newer server, and require a
       separate connection to issue the KILL QUERY statement. In
       the case of setQueryTimeout(), the implementation creates
       an additional thread to handle the timeout functionality.

Note
       Failures to cancel the statement for setQueryTimeout()
       may manifest themselves as RuntimeException rather than
       failing silently, as there is currently no way to unblock
       the thread that is executing the query being cancelled
       due to timeout expiration and have it throw the exception
       instead.
       MySQL does not support SQL cursors, and the JDBC driver
       doesn't emulate them, so "setCursorName()" has no effect.
       Connector/J 5.1.3 and later include two additional
       methods:
          + setLocalInfileInputStream() sets an InputStream
            instance that will be used to send data to the MySQL
            server for a LOAD DATA LOCAL INFILE statement rather
            than a FileInputStream or URLInputStream that
            represents the path given as an argument to the
            statement.
            This stream will be read to completion upon
            execution of a LOAD DATA LOCAL INFILE statement, and
            will automatically be closed by the driver, so it
            needs to be reset before each call to execute*()
            that would cause the MySQL server to request data to
            fulfill the request for LOAD DATA LOCAL INFILE.
            If this value is set to NULL, the driver will revert
            to using a FileInputStream or URLInputStream as
            required.
          + getLocalInfileInputStream() returns the InputStream
            instance that will be used to send data in response
            to a LOAD DATA LOCAL INFILE statement.
            This method returns NULL if no such stream has been
            set via setLocalInfileInputStream().

1.4.3. Java, JDBC and MySQL Types

   MySQL Connector/J is flexible in the way it handles
   conversions between MySQL data types and Java data types.

   In general, any MySQL data type can be converted to a
   java.lang.String, and any numerical type can be converted to
   any of the Java numerical types, although round-off,
   overflow, or loss of precision may occur.

   Starting with Connector/J 3.1.0, the JDBC driver will issue
   warnings or throw DataTruncation exceptions as is required by
   the JDBC specification unless the connection was configured
   not to do so by using the property jdbcCompliantTruncation
   and setting it to false.

   The conversions that are always guaranteed to work are listed
   in the following table:

   Connection Properties - Miscellaneous.
   These MySQL Data Types Can always be converted to these Java
   types
   CHAR, VARCHAR, BLOB, TEXT, ENUM, and SET java.lang.String,
   java.io.InputStream, java.io.Reader, java.sql.Blob,
   java.sql.Clob
   FLOAT, REAL, DOUBLE PRECISION, NUMERIC, DECIMAL, TINYINT,
   SMALLINT, MEDIUMINT, INTEGER, BIGINT java.lang.String,
   java.lang.Short, java.lang.Integer, java.lang.Long,
   java.lang.Double, java.math.BigDecimal
   DATE, TIME, DATETIME, TIMESTAMP java.lang.String,
   java.sql.Date, java.sql.Timestamp

Note

   Round-off, overflow or loss of precision may occur if you
   choose a Java numeric data type that has less precision or
   capacity than the MySQL data type you are converting to/from.

   The ResultSet.getObject() method uses the type conversions
   between MySQL and Java types, following the JDBC
   specification where appropriate. The value returned by
   ResultSetMetaData.GetColumnClassName() is also shown below.
   For more information on the java.sql.Types classes see Java 2
   Platform Types
   (http://java.sun.com/j2se/1.4.2/docs/api/java/sql/Types.html)
   .

   MySQL Types to Java Types for ResultSet.getObject().
   MySQL Type Name Return value of GetColumnClassName Returned
   as Java Class
   BIT(1) (new in MySQL-5.0) BIT java.lang.Boolean
   BIT( > 1) (new in MySQL-5.0) BIT byte[]
   TINYINT TINYINT java.lang.Boolean if the configuration
   property tinyInt1isBit is set to true (the default) and the
   storage size is 1, or java.lang.Integer if not.
   BOOL, BOOLEAN TINYINT See TINYINT, above as these are aliases
   for TINYINT(1), currently.
   SMALLINT[(M)] [UNSIGNED] SMALLINT [UNSIGNED]
   java.lang.Integer (regardless if UNSIGNED or not)
   MEDIUMINT[(M)] [UNSIGNED] MEDIUMINT [UNSIGNED]
   java.lang.Integer, if UNSIGNED java.lang.Long
   INT,INTEGER[(M)] [UNSIGNED] INTEGER [UNSIGNED]
   java.lang.Integer, if UNSIGNED java.lang.Long
   BIGINT[(M)] [UNSIGNED] BIGINT [UNSIGNED] java.lang.Long, if
   UNSIGNED java.math.BigInteger
   FLOAT[(M,D)] FLOAT java.lang.Float
   DOUBLE[(M,B)] DOUBLE java.lang.Double
   DECIMAL[(M[,D])] DECIMAL java.math.BigDecimal
   DATE DATE java.sql.Date
   DATETIME DATETIME java.sql.Timestamp
   TIMESTAMP[(M)] TIMESTAMP java.sql.Timestamp
   TIME TIME java.sql.Time
   YEAR[(2|4)] YEAR If yearIsDateType configuration property is
   set to false, then the returned object type is
   java.sql.Short. If set to true (the default) then an object
   of type java.sql.Date (with the date set to January 1st, at
   midnight).
   CHAR(M) CHAR java.lang.String (unless the character set for
   the column is BINARY, then byte[] is returned.
   VARCHAR(M) [BINARY] VARCHAR java.lang.String (unless the
   character set for the column is BINARY, then byte[] is
   returned.
   BINARY(M) BINARY byte[]
   VARBINARY(M) VARBINARY byte[]
   TINYBLOB TINYBLOB byte[]
   TINYTEXT VARCHAR java.lang.String
   BLOB BLOB byte[]
   TEXT VARCHAR java.lang.String
   MEDIUMBLOB MEDIUMBLOB byte[]
   MEDIUMTEXT VARCHAR java.lang.String
   LONGBLOB LONGBLOB byte[]
   LONGTEXT VARCHAR java.lang.String
   ENUM('value1','value2',...) CHAR java.lang.String
   SET('value1','value2',...) CHAR java.lang.String

1.4.4. Using Character Sets and Unicode

   All strings sent from the JDBC driver to the server are
   converted automatically from native Java Unicode form to the
   client character encoding, including all queries sent via
   Statement.execute(), Statement.executeUpdate(),
   Statement.executeQuery() as well as all PreparedStatement and
   CallableStatement parameters with the exclusion of parameters
   set using setBytes(), setBinaryStream(), setAsciiStream(),
   setUnicodeStream() and setBlob() .

   Prior to MySQL Server 4.1, Connector/J supported a single
   character encoding per connection, which could either be
   automatically detected from the server configuration, or
   could be configured by the user through the useUnicode and
   characterEncoding properties.

   Starting with MySQL Server 4.1, Connector/J supports a single
   character encoding between client and server, and any number
   of character encodings for data returned by the server to the
   client in ResultSets.

   The character encoding between client and server is
   automatically detected upon connection. The encoding used by
   the driver is specified on the server via the character_set
   system variable for server versions older than 4.1.0 and
   character_set_server for server versions 4.1.0 and newer. For
   more information, see Server Character Set and Collation
   (http://dev.mysql.com/doc/refman/5.0/en/charset-server.html).

   To override the automatically-detected encoding on the client
   side, use the characterEncoding property in the URL used to
   connect to the server.

   When specifying character encodings on the client side,
   Java-style names should be used. The following table lists
   Java-style names for MySQL character sets:

   MySQL to Java Encoding Name Translations.
   MySQL Character Set Name Java-Style Character Encoding Name
   ascii US-ASCII
   big5 Big5
   gbk GBK
   sjis SJIS (or Cp932 or MS932 for MySQL Server < 4.1.11)
   cp932 Cp932 or MS932 (MySQL Server > 4.1.11)
   gb2312 EUC_CN
   ujis EUC_JP
   euckr EUC_KR
   latin1 ISO8859_1
   latin2 ISO8859_2
   greek ISO8859_7
   hebrew ISO8859_8
   cp866 Cp866
   tis620 TIS620
   cp1250 Cp1250
   cp1251 Cp1251
   cp1257 Cp1257
   macroman MacRoman
   macce MacCentralEurope
   utf8 UTF-8
   ucs2 UnicodeBig

Warning

   Do not issue the query 'set names' with Connector/J, as the
   driver will not detect that the character set has changed,
   and will continue to use the character set detected during
   the initial connection setup.

   To allow multiple character sets to be sent from the client,
   the UTF-8 encoding should be used, either by configuring utf8
   as the default server character set, or by configuring the
   JDBC driver to use UTF-8 through the characterEncoding
   property.

1.4.5. Connecting Securely Using SSL

   SSL in MySQL Connector/J encrypts all data (other than the
   initial handshake) between the JDBC driver and the server.
   The performance penalty for enabling SSL is an increase in
   query processing time between 35% and 50%, depending on the
   size of the query, and the amount of data it returns.

   For SSL Support to work, you must have the following:
     * A JDK that includes JSSE (Java Secure Sockets Extension),
       like JDK-1.4.1 or newer. SSL does not currently work with
       a JDK that you can add JSSE to, like JDK-1.2.x or
       JDK-1.3.x due to the following JSSE bug:
       http://developer.java.sun.com/developer/bugParade/bugs/42
       73544.html
     * A MySQL server that supports SSL and has been compiled
       and configured to do so, which is MySQL-4.0.4 or later,
       see Using Secure Connections
       (http://dev.mysql.com/doc/refman/5.0/en/secure-connection
       s.html), for more information.
     * A client certificate (covered later in this section)

   You will first need to import the MySQL server CA Certificate
   into a Java truststore. A sample MySQL server CA Certificate
   is located in the SSL subdirectory of the MySQL source
   distribution. This is what SSL will use to determine if you
   are communicating with a secure MySQL server.

   To use Java's keytool to create a truststore in the current
   directory , and import the server's CA certificate
   (cacert.pem), you can do the following (assuming that keytool
   is in your path. The keytool should be located in the bin
   subdirectory of your JDK or JRE):
shell> keytool -import -alias mysqlServerCACert \
                                  -file cacert.pem -keystore truststor
e

   Keytool will respond with the following information:
Enter keystore password:  *********
Owner: EMAILADDRESS=walrus@example.com, CN=Walrus,
       O=MySQL AB, L=Orenburg, ST=Some-State, C=RU
Issuer: EMAILADDRESS=walrus@example.com, CN=Walrus,
       O=MySQL AB, L=Orenburg, ST=Some-State, C=RU
Serial number: 0
Valid from:
   Fri Aug 02 16:55:53 CDT 2002 until: Sat Aug 02 16:55:53 CDT 2003
Certificate fingerprints:
    MD5:  61:91:A0:F2:03:07:61:7A:81:38:66:DA:19:C4:8D:AB
    SHA1: 25:77:41:05:D5:AD:99:8C:14:8C:CA:68:9C:2F:B8:89:C3:34:4D:6C
Trust this certificate? [no]:  yes
Certificate was added to keystore

   You will then need to generate a client certificate, so that
   the MySQL server knows that it is talking to a secure client:
 shell> keytool -genkey -keyalg rsa \
     -alias mysqlClientCertificate -keystore keystore

   Keytool will prompt you for the following information, and
   create a keystore named keystore in the current directory.

   You should respond with information that is appropriate for
   your situation:
Enter keystore password:  *********
What is your first and last name?
  [Unknown]:  Matthews
What is the name of your organizational unit?
  [Unknown]:  Software Development
What is the name of your organization?
  [Unknown]:  MySQL AB
What is the name of your City or Locality?
  [Unknown]:  Flossmoor
What is the name of your State or Province?
  [Unknown]:  IL
What is the two-letter country code for this unit?
  [Unknown]:  US
Is <CN=Matthews, OU=Software Development, O=MySQL AB,
 L=Flossmoor, ST=IL, C=US> correct?
  [no]:  y

Enter key password for <mysqlClientCertificate>
        (RETURN if same as keystore password):

   Finally, to get JSSE to use the keystore and truststore that
   you have generated, you need to set the following system
   properties when you start your JVM, replacing
   path_to_keystore_file with the full path to the keystore file
   you created, path_to_truststore_file with the path to the
   truststore file you created, and using the appropriate
   password values for each property. You can do this either on
   the command line:
-Djavax.net.ssl.keyStore=path_to_keystore_file
-Djavax.net.ssl.keyStorePassword=password
-Djavax.net.ssl.trustStore=path_to_truststore_file
-Djavax.net.ssl.trustStorePassword=password

   Or you can set the values directly within the application:
 System.setProperty("javax.net.ssl.keyStore","path_to_keystore_file");
System.setProperty("javax.net.ssl.keyStorePassword","password");
System.setProperty("javax.net.ssl.trustStore","path_to_truststore_file
");
System.setProperty("javax.net.ssl.trustStorePassword","password");

   You will also need to set useSSL to true in your connection
   parameters for MySQL Connector/J, either by adding
   useSSL=true to your URL, or by setting the property useSSL to
   true in the java.util.Properties instance you pass to
   DriverManager.getConnection().

   You can test that SSL is working by turning on JSSE debugging
   (as detailed below), and look for the following key events:
...
*** ClientHello, v3.1
RandomCookie:  GMT: 1018531834 bytes = { 199, 148, 180, 215, 74, 12, �
  54, 244, 0, 168, 55, 103, 215, 64, 16, 138, 225, 190, 132, 153, 2, �
  217, 219, 239, 202, 19, 121, 78 }
Session ID:  {}
Cipher Suites:  { 0, 5, 0, 4, 0, 9, 0, 10, 0, 18, 0, 19, 0, 3, 0, 17 }
Compression Methods:  { 0 }
***
[write] MD5 and SHA1 hashes:  len = 59
0000: 01 00 00 37 03 01 3D B6 90 FA C7 94 B4 D7 4A 0C  ...7..=.......J
.
0010: 36 F4 00 A8 37 67 D7 40 10 8A E1 BE 84 99 02 D9  6...7g.@.......
.
0020: DB EF CA 13 79 4E 00 00 10 00 05 00 04 00 09 00  ....yN.........
.
0030: 0A 00 12 00 13 00 03 00 11 01 00                 ...........
main, WRITE:  SSL v3.1 Handshake, length = 59
main, READ:  SSL v3.1 Handshake, length = 74
*** ServerHello, v3.1
RandomCookie:  GMT: 1018577560 bytes = { 116, 50, 4, 103, 25, 100, 58,
 �
   202, 79, 185, 178, 100, 215, 66, 254, 21, 83, 187, 190, 42, 170, 3,
 �
   132, 110, 82, 148, 160, 92 }
Session ID:  {163, 227, 84, 53, 81, 127, 252, 254, 178, 179, 68, 63, �
   182, 158, 30, 11, 150, 79, 170, 76, 255, 92, 15, 226, 24, 17, 177,
�
   219, 158, 177, 187, 143}
Cipher Suite:  { 0, 5 }
Compression Method: 0
***
%% Created:  [Session-1, SSL_RSA_WITH_RC4_128_SHA]
** SSL_RSA_WITH_RC4_128_SHA
[read] MD5 and SHA1 hashes:  len = 74
0000: 02 00 00 46 03 01 3D B6 43 98 74 32 04 67 19 64  ...F..=.C.t2.g.
d
0010: 3A CA 4F B9 B2 64 D7 42 FE 15 53 BB BE 2A AA 03  :.O..d.B..S..*.
.
0020: 84 6E 52 94 A0 5C 20 A3 E3 54 35 51 7F FC FE B2  .nR..\ ..T5Q...
.
0030: B3 44 3F B6 9E 1E 0B 96 4F AA 4C FF 5C 0F E2 18  .D?.....O.L.\..
.
0040: 11 B1 DB 9E B1 BB 8F 00 05 00                    ..........
main, READ:  SSL v3.1 Handshake, length = 1712
...

   JSSE provides debugging (to STDOUT) when you set the
   following system property: -Djavax.net.debug=all This will
   tell you what keystores and truststores are being used, as
   well as what is going on during the SSL handshake and
   certificate exchange. It will be helpful when trying to
   determine what is not working when trying to get an SSL
   connection to happen.

1.4.6. Using Master/Slave Replication with ReplicationConnection

   Starting with Connector/J 3.1.7, we've made available a
   variant of the driver that will automatically send queries to
   a read/write master, or a failover or round-robin
   loadbalanced set of slaves based on the state of
   Connection.getReadOnly() .

   An application signals that it wants a transaction to be
   read-only by calling Connection.setReadOnly(true), this
   replication-aware connection will use one of the slave
   connections, which are load-balanced per-vm using a
   round-robin scheme (a given connection is sticky to a slave
   unless that slave is removed from service). If you have a
   write transaction, or if you have a read that is
   time-sensitive (remember, replication in MySQL is
   asynchronous), set the connection to be not read-only, by
   calling Connection.setReadOnly(false) and the driver will
   ensure that further calls are sent to the master MySQL
   server. The driver takes care of propagating the current
   state of autocommit, isolation level, and catalog between all
   of the connections that it uses to accomplish this load
   balancing functionality.

   To enable this functionality, use the "
   com.mysql.jdbc.ReplicationDriver " class when configuring
   your application server's connection pool or when creating an
   instance of a JDBC driver for your standalone application.
   Because it accepts the same URL format as the standard MySQL
   JDBC driver, ReplicationDriver does not currently work with
   java.sql.DriverManager -based connection creation unless it
   is the only MySQL JDBC driver registered with the
   DriverManager .

   Here is a short, simple example of how ReplicationDriver
   might be used in a standalone application.
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import com.mysql.jdbc.ReplicationDriver;

public class ReplicationDriverDemo {

  public static void main(String[] args) throws Exception {
    ReplicationDriver driver = new ReplicationDriver();

    Properties props = new Properties();

    // We want this for failover on the slaves
    props.put("autoReconnect", "true");

    // We want to load balance between the slaves
    props.put("roundRobinLoadBalance", "true");

    props.put("user", "foo");
    props.put("password", "bar");

    //
    // Looks like a normal MySQL JDBC url, with a
    // comma-separated list of hosts, the first
    // being the 'master', the rest being any number
    // of slaves that the driver will load balance against
    //

    Connection conn =
        driver.connect("jdbc:mysql://master,slave1,slave2,slave3/test"
,
            props);

    //
    // Perform read/write work on the master
    // by setting the read-only flag to "false"
    //

    conn.setReadOnly(false);
    conn.setAutoCommit(false);
    conn.createStatement().executeUpdate("UPDATE some_table ....");
    conn.commit();

    //
    // Now, do a query from a slave, the driver automatically picks on
e
    // from the list
    //

    conn.setReadOnly(true);

    ResultSet rs =
      conn.createStatement().executeQuery("SELECT a,b FROM alt_table")
;

     .......
  }
}

1.4.7. Mapping MySQL Error Numbers to SQLStates

   The table below provides a mapping of the MySQL Error Numbers
   to SQL States

   Table 1. Mapping of MySQL Error Numbers to SQLStates
   MySQL Error Number MySQL Error Name Legacy (X/Open) SQLState
   SQL Standard SQLState
   1022 ER_DUP_KEY S1000 23000
   1037 ER_OUTOFMEMORY S1001 HY001
   1038 ER_OUT_OF_SORTMEMORY S1001 HY001
   1040 ER_CON_COUNT_ERROR 08004 08004
   1042 ER_BAD_HOST_ERROR 08004 08S01
   1043 ER_HANDSHAKE_ERROR 08004 08S01
   1044 ER_DBACCESS_DENIED_ERROR S1000 42000
   1045 ER_ACCESS_DENIED_ERROR 28000 28000
   1047 ER_UNKNOWN_COM_ERROR 08S01 HY000
   1050 ER_TABLE_EXISTS_ERROR S1000 42S01
   1051 ER_BAD_TABLE_ERROR 42S02 42S02
   1052 ER_NON_UNIQ_ERROR S1000 23000
   1053 ER_SERVER_SHUTDOWN S1000 08S01
   1054 ER_BAD_FIELD_ERROR S0022 42S22
   1055 ER_WRONG_FIELD_WITH_GROUP S1009 42000
   1056 ER_WRONG_GROUP_FIELD S1009 42000
   1057 ER_WRONG_SUM_SELECT S1009 42000
   1058 ER_WRONG_VALUE_COUNT 21S01 21S01
   1059 ER_TOO_LONG_IDENT S1009 42000
   1060 ER_DUP_FIELDNAME S1009 42S21
   1061 ER_DUP_KEYNAME S1009 42000
   1062 ER_DUP_ENTRY S1009 23000
   1063 ER_WRONG_FIELD_SPEC S1009 42000
   1064 ER_PARSE_ERROR 42000 42000
   1065 ER_EMPTY_QUERY 42000 42000
   1066 ER_NONUNIQ_TABLE S1009 42000
   1067 ER_INVALID_DEFAULT S1009 42000
   1068 ER_MULTIPLE_PRI_KEY S1009 42000
   1069 ER_TOO_MANY_KEYS S1009 42000
   1070 ER_TOO_MANY_KEY_PARTS S1009 42000
   1071 ER_TOO_LONG_KEY S1009 42000
   1072 ER_KEY_COLUMN_DOES_NOT_EXITS S1009 42000
   1073 ER_BLOB_USED_AS_KEY S1009 42000
   1074 ER_TOO_BIG_FIELDLENGTH S1009 42000
   1075 ER_WRONG_AUTO_KEY S1009 42000
   1080 ER_FORCING_CLOSE S1000 08S01
   1081 ER_IPSOCK_ERROR 08S01 08S01
   1082 ER_NO_SUCH_INDEX S1009 42S12
   1083 ER_WRONG_FIELD_TERMINATORS S1009 42000
   1084 ER_BLOBS_AND_NO_TERMINATED S1009 42000
   1090 ER_CANT_REMOVE_ALL_FIELDS S1000 42000
   1091 ER_CANT_DROP_FIELD_OR_KEY S1000 42000
   1101 ER_BLOB_CANT_HAVE_DEFAULT S1000 42000
   1102 ER_WRONG_DB_NAME S1000 42000
   1103 ER_WRONG_TABLE_NAME S1000 42000
   1104 ER_TOO_BIG_SELECT S1000 42000
   1106 ER_UNKNOWN_PROCEDURE S1000 42000
   1107 ER_WRONG_PARAMCOUNT_TO_PROCEDURE S1000 42000
   1109 ER_UNKNOWN_TABLE S1000 42S02
   1110 ER_FIELD_SPECIFIED_TWICE S1000 42000
   1112 ER_UNSUPPORTED_EXTENSION S1000 42000
   1113 ER_TABLE_MUST_HAVE_COLUMNS S1000 42000
   1115 ER_UNKNOWN_CHARACTER_SET S1000 42000
   1118 ER_TOO_BIG_ROWSIZE S1000 42000
   1120 ER_WRONG_OUTER_JOIN S1000 42000
   1121 ER_NULL_COLUMN_IN_INDEX S1000 42000
   1129 ER_HOST_IS_BLOCKED 08004 HY000
   1130 ER_HOST_NOT_PRIVILEGED 08004 HY000
   1131 ER_PASSWORD_ANONYMOUS_USER S1000 42000
   1132 ER_PASSWORD_NOT_ALLOWED S1000 42000
   1133 ER_PASSWORD_NO_MATCH S1000 42000
   1136 ER_WRONG_VALUE_COUNT_ON_ROW S1000 21S01
   1138 ER_INVALID_USE_OF_NULL S1000 42000
   1139 ER_REGEXP_ERROR S1000 42000
   1140 ER_MIX_OF_GROUP_FUNC_AND_FIELDS S1000 42000
   1141 ER_NONEXISTING_GRANT S1000 42000
   1142 ER_TABLEACCESS_DENIED_ERROR S1000 42000
   1143 ER_COLUMNACCESS_DENIED_ERROR S1000 42000
   1144 ER_ILLEGAL_GRANT_FOR_TABLE S1000 42000
   1145 ER_GRANT_WRONG_HOST_OR_USER S1000 42000
   1146 ER_NO_SUCH_TABLE S1000 42S02
   1147 ER_NONEXISTING_TABLE_GRANT S1000 42000
   1148 ER_NOT_ALLOWED_COMMAND S1000 42000
   1149 ER_SYNTAX_ERROR S1000 42000
   1152 ER_ABORTING_CONNECTION S1000 08S01
   1153 ER_NET_PACKET_TOO_LARGE S1000 08S01
   1154 ER_NET_READ_ERROR_FROM_PIPE S1000 08S01
   1155 ER_NET_FCNTL_ERROR S1000 08S01
   1156 ER_NET_PACKETS_OUT_OF_ORDER S1000 08S01
   1157 ER_NET_UNCOMPRESS_ERROR S1000 08S01
   1158 ER_NET_READ_ERROR S1000 08S01
   1159 ER_NET_READ_INTERRUPTED S1000 08S01
   1160 ER_NET_ERROR_ON_WRITE S1000 08S01
   1161 ER_NET_WRITE_INTERRUPTED S1000 08S01
   1162 ER_TOO_LONG_STRING S1000 42000
   1163 ER_TABLE_CANT_HANDLE_BLOB S1000 42000
   1164 ER_TABLE_CANT_HANDLE_AUTO_INCREMENT S1000 42000
   1166 ER_WRONG_COLUMN_NAME S1000 42000
   1167 ER_WRONG_KEY_COLUMN S1000 42000
   1169 ER_DUP_UNIQUE S1000 23000
   1170 ER_BLOB_KEY_WITHOUT_LENGTH S1000 42000
   1171 ER_PRIMARY_CANT_HAVE_NULL S1000 42000
   1172 ER_TOO_MANY_ROWS S1000 42000
   1173 ER_REQUIRES_PRIMARY_KEY S1000 42000
   1177 ER_CHECK_NO_SUCH_TABLE S1000 42000
   1178 ER_CHECK_NOT_IMPLEMENTED S1000 42000
   1179 ER_CANT_DO_THIS_DURING_AN_TRANSACTION S1000 25000
   1184 ER_NEW_ABORTING_CONNECTION S1000 08S01
   1189 ER_MASTER_NET_READ S1000 08S01
   1190 ER_MASTER_NET_WRITE S1000 08S01
   1203 ER_TOO_MANY_USER_CONNECTIONS S1000 42000
   1205 ER_LOCK_WAIT_TIMEOUT 41000 41000
   1207 ER_READ_ONLY_TRANSACTION S1000 25000
   1211 ER_NO_PERMISSION_TO_CREATE_USER S1000 42000
   1213 ER_LOCK_DEADLOCK 41000 40001
   1216 ER_NO_REFERENCED_ROW S1000 23000
   1217 ER_ROW_IS_REFERENCED S1000 23000
   1218 ER_CONNECT_TO_MASTER S1000 08S01
   1222 ER_WRONG_NUMBER_OF_COLUMNS_IN_SELECT S1000 21000
   1226 ER_USER_LIMIT_REACHED S1000 42000
   1230 ER_NO_DEFAULT S1000 42000
   1231 ER_WRONG_VALUE_FOR_VAR S1000 42000
   1232 ER_WRONG_TYPE_FOR_VAR S1000 42000
   1234 ER_CANT_USE_OPTION_HERE S1000 42000
   1235 ER_NOT_SUPPORTED_YET S1000 42000
   1239 ER_WRONG_FK_DEF S1000 42000
   1241 ER_OPERAND_COLUMNS S1000 21000
   1242 ER_SUBQUERY_NO_1_ROW S1000 21000
   1247 ER_ILLEGAL_REFERENCE S1000 42S22
   1248 ER_DERIVED_MUST_HAVE_ALIAS S1000 42000
   1249 ER_SELECT_REDUCED S1000 01000
   1250 ER_TABLENAME_NOT_ALLOWED_HERE S1000 42000
   1251 ER_NOT_SUPPORTED_AUTH_MODE S1000 08004
   1252 ER_SPATIAL_CANT_HAVE_NULL S1000 42000
   1253 ER_COLLATION_CHARSET_MISMATCH S1000 42000
   1261 ER_WARN_TOO_FEW_RECORDS S1000 01000
   1262 ER_WARN_TOO_MANY_RECORDS S1000 01000
   1263 ER_WARN_NULL_TO_NOTNULL S1000 01000
   1264 ER_WARN_DATA_OUT_OF_RANGE S1000 01000
   1265 ER_WARN_DATA_TRUNCATED S1000 01000
   1280 ER_WRONG_NAME_FOR_INDEX S1000 42000
   1281 ER_WRONG_NAME_FOR_CATALOG S1000 42000
   1286 ER_UNKNOWN_STORAGE_ENGINE S1000 42000

1.5. Connector/J Notes and Tips

1.5.1. Basic JDBC Concepts

   This section provides some general JDBC background.

1.5.1.1. Connecting to MySQL Using the DriverManager Interface

   When you are using JDBC outside of an application server, the
   DriverManager class manages the establishment of Connections.

   The DriverManager needs to be told which JDBC drivers it
   should try to make Connections with. The easiest way to do
   this is to use Class.forName() on the class that implements
   the java.sql.Driver interface. With MySQL Connector/J, the
   name of this class is com.mysql.jdbc.Driver. With this
   method, you could use an external configuration file to
   supply the driver class name and driver parameters to use
   when connecting to a database.

   The following section of Java code shows how you might
   register MySQL Connector/J from the main() method of your
   application:
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

public class LoadDriver {
    public static void main(String[] args) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
}

   After the driver has been registered with the DriverManager,
   you can obtain a Connection instance that is connected to a
   particular database by calling DriverManager.getConnection():

   Example 1. Obtaining a connection from the DriverManager

   This example shows how you can obtain a Connection instance
   from the DriverManager. There are a few different signatures
   for the getConnection() method. You should see the API
   documentation that comes with your JDK for more specific
   information on how to use them.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

...
try {
    Connection conn =
       DriverManager.getConnection("jdbc:mysql://localhost/test?" +
                                   "user=monty&password=greatsqldb");

    // Do something with the Connection

   ...
} catch (SQLException ex) {
    // handle any errors
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
}

   Once a Connection is established, it can be used to create
   Statement and PreparedStatement objects, as well as retrieve
   metadata about the database. This is explained in the
   following sections.

1.5.1.2. Using Statements to Execute SQL

   Statement objects allow you to execute basic SQL queries and
   retrieve the results through the ResultSet class which is
   described later.

   To create a Statement instance, you call the
   createStatement() method on the Connection object you have
   retrieved via one of the DriverManager.getConnection() or
   DataSource.getConnection() methods described earlier.

   Once you have a Statement instance, you can execute a SELECT
   query by calling the executeQuery(String) method with the SQL
   you want to use.

   To update data in the database, use the executeUpdate(String
   SQL) method. This method returns the number of rows affected
   by the update statement.

   If you don't know ahead of time whether the SQL statement
   will be a SELECT or an UPDATE/INSERT, then you can use the
   execute(String SQL) method. This method will return true if
   the SQL query was a SELECT, or false if it was an UPDATE,
   INSERT, or DELETE statement. If the statement was a SELECT
   query, you can retrieve the results by calling the
   getResultSet() method. If the statement was an UPDATE,
   INSERT, or DELETE statement, you can retrieve the affected
   rows count by calling getUpdateCount() on the Statement
   instance.

   Example 2. Using java.sql.Statement to execute a SELECT query
// assume that conn is an already created JDBC connection
Statement stmt = null;
ResultSet rs = null;

try {
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT foo FROM bar");

    // or alternatively, if you don't know ahead of time that
    // the query will be a SELECT...

    if (stmt.execute("SELECT foo FROM bar")) {
        rs = stmt.getResultSet();
    }

    // Now do something with the ResultSet ....
} finally {
    // it is a good idea to release
    // resources in a finally{} block
    // in reverse-order of their creation
    // if they are no-longer needed

    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { // ignore }

        rs = null;
    }

    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException sqlEx) { // ignore }

        stmt = null;
    }
}

1.5.1.3. Using CallableStatements to Execute Stored Procedures

   Starting with MySQL server version 5.0 when used with
   Connector/J 3.1.1 or newer, the java.sql.CallableStatement
   interface is fully implemented with the exception of the
   getParameterMetaData() method.

   For more information on MySQL stored procedures, please refer
   to http://dev.mysql.com/doc/mysql/en/stored-procedures.html.

   Connector/J exposes stored procedure functionality through
   JDBC's CallableStatement interface.

Note

   Current versions of MySQL server do not return enough
   information for the JDBC driver to provide result set
   metadata for callable statements. This means that when using
   CallableStatement, ResultSetMetaData may return NULL.

   The following example shows a stored procedure that returns
   the value of inOutParam incremented by 1, and the string
   passed in via inputParam as a ResultSet:

   Example 3. Stored Procedures
CREATE PROCEDURE demoSp(IN inputParam VARCHAR(255), \
                                        INOUT inOutParam INT)
BEGIN
    DECLARE z INT;
    SET z = inOutParam + 1;
    SET inOutParam = z;

    SELECT inputParam;

    SELECT CONCAT('zyxw', inputParam);
END

   To use the demoSp procedure with Connector/J, follow these
   steps:
    1. Prepare the callable statement by using
       Connection.prepareCall() .
       Notice that you have to use JDBC escape syntax, and that
       the parentheses surrounding the parameter placeholders
       are not optional:
       Example 4. Using Connection.prepareCall()
import java.sql.CallableStatement;

...

    //
    // Prepare a call to the stored procedure 'demoSp'
    // with two parameters
    //
    // Notice the use of JDBC-escape syntax ({call ...})
    //

    CallableStatement cStmt = conn.prepareCall("{call demoSp(?, ?)}");



    cStmt.setString(1, "abcdefg");

Note
       Connection.prepareCall() is an expensive method, due to
       the metadata retrieval that the driver performs to
       support output parameters. For performance reasons, you
       should try to minimize unnecessary calls to
       Connection.prepareCall() by reusing CallableStatement
       instances in your code.
    2. Register the output parameters (if any exist)
       To retrieve the values of output parameters (parameters
       specified as OUT or INOUT when you created the stored
       procedure), JDBC requires that they be specified before
       statement execution using the various
       registerOutputParameter() methods in the
       CallableStatement interface:
       Example 5. Registering output parameters
import java.sql.Types;
...
//
// Connector/J supports both named and indexed
// output parameters. You can register output
// parameters using either method, as well
// as retrieve output parameters using either
// method, regardless of what method was
// used to register them.
//
// The following examples show how to use
// the various methods of registering
// output parameters (you should of course
// use only one registration per parameter).
//

//
// Registers the second parameter as output, and
// uses the type 'INTEGER' for values returned from
// getObject()
//

cStmt.registerOutParameter(2, Types.INTEGER);

//
// Registers the named parameter 'inOutParam', and
// uses the type 'INTEGER' for values returned from
// getObject()
//

cStmt.registerOutParameter("inOutParam", Types.INTEGER);
...

    3. Set the input parameters (if any exist)
       Input and in/out parameters are set as for
       PreparedStatement objects. However, CallableStatement
       also supports setting parameters by name:
       Example 6. Setting CallableStatement input parameters
...

    //
    // Set a parameter by index
    //

    cStmt.setString(1, "abcdefg");

    //
    // Alternatively, set a parameter using
    // the parameter name
    //

    cStmt.setString("inputParameter", "abcdefg");

    //
    // Set the 'in/out' parameter using an index
    //

    cStmt.setInt(2, 1);

    //
    // Alternatively, set the 'in/out' parameter
    // by name
    //

    cStmt.setInt("inOutParam", 1);

...
    4. Execute the CallableStatement, and retrieve any result
       sets or output parameters.
       Although CallableStatement supports calling any of the
       Statement execute methods (executeUpdate(),
       executeQuery() or execute()), the most flexible method to
       call is execute(), as you do not need to know ahead of
       time if the stored procedure returns result sets:
       Example 7. Retrieving results and output parameter values
...

    boolean hadResults = cStmt.execute();

    //
    // Process all returned result sets
    //

    while (hadResults) {
        ResultSet rs = cStmt.getResultSet();

        // process result set
        ...

        hadResults = rs.getMoreResults();
    }

    //
    // Retrieve output parameters
    //
    // Connector/J supports both index-based and
    // name-based retrieval
    //

    int outputValue = cStmt.getInt(2); // index-based

    outputValue = cStmt.getInt("inOutParam"); // name-based

...

1.5.1.4. Retrieving AUTO_INCREMENT Column Values

   Before version 3.0 of the JDBC API, there was no standard way
   of retrieving key values from databases that supported auto
   increment or identity columns. With older JDBC drivers for
   MySQL, you could always use a MySQL-specific method on the
   Statement interface, or issue the query SELECT
   LAST_INSERT_ID() after issuing an INSERT to a table that had
   an AUTO_INCREMENT key. Using the MySQL-specific method call
   isn't portable, and issuing a SELECT to get the
   AUTO_INCREMENT key's value requires another round-trip to the
   database, which isn't as efficient as possible. The following
   code snippets demonstrate the three different ways to
   retrieve AUTO_INCREMENT values. First, we demonstrate the use
   of the new JDBC-3.0 method getGeneratedKeys() which is now
   the preferred method to use if you need to retrieve
   AUTO_INCREMENT keys and have access to JDBC-3.0. The second
   example shows how you can retrieve the same value using a
   standard SELECT LAST_INSERT_ID() query. The final example
   shows how updatable result sets can retrieve the
   AUTO_INCREMENT value when using the insertRow() method.

   Example 8. Retrieving AUTO_INCREMENT column values using
   Statement.getGeneratedKeys()
   Statement stmt = null;
   ResultSet rs = null;

   try {

    //
    // Create a Statement instance that we can use for
    // 'normal' result sets assuming you have a
    // Connection 'conn' to a MySQL database already
    // available

    stmt = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                                java.sql.ResultSet.CONCUR_UPDATABLE);

    //
    // Issue the DDL queries for the table for this example
    //

    stmt.executeUpdate("DROP TABLE IF EXISTS autoIncTutorial");
    stmt.executeUpdate(
            "CREATE TABLE autoIncTutorial ("
            + "priKey INT NOT NULL AUTO_INCREMENT, "
            + "dataField VARCHAR(64), PRIMARY KEY (priKey))");

    //
    // Insert one row that will generate an AUTO INCREMENT
    // key in the 'priKey' field
    //

    stmt.executeUpdate(
            "INSERT INTO autoIncTutorial (dataField) "
            + "values ('Can I Get the Auto Increment Field?')",
            Statement.RETURN_GENERATED_KEYS);

    //
    // Example of using Statement.getGeneratedKeys()
    // to retrieve the value of an auto-increment
    // value
    //

    int autoIncKeyFromApi = -1;

    rs = stmt.getGeneratedKeys();

    if (rs.next()) {
        autoIncKeyFromApi = rs.getInt(1);
    } else {

        // throw an exception from here
    }

    rs.close();

    rs = null;

    System.out.println("Key returned from getGeneratedKeys():"
        + autoIncKeyFromApi);
} finally {

    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException ex) {
            // ignore
        }
    }

    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException ex) {
            // ignore
        }
    }
}

   Example 9. Retrieving AUTO_INCREMENT column values using
   SELECT LAST_INSERT_ID()
   Statement stmt = null;
   ResultSet rs = null;

   try {

    //
    // Create a Statement instance that we can use for
    // 'normal' result sets.

    stmt = conn.createStatement();

    //
    // Issue the DDL queries for the table for this example
    //

    stmt.executeUpdate("DROP TABLE IF EXISTS autoIncTutorial");
    stmt.executeUpdate(
            "CREATE TABLE autoIncTutorial ("
            + "priKey INT NOT NULL AUTO_INCREMENT, "
            + "dataField VARCHAR(64), PRIMARY KEY (priKey))");

    //
    // Insert one row that will generate an AUTO INCREMENT
    // key in the 'priKey' field
    //

    stmt.executeUpdate(
            "INSERT INTO autoIncTutorial (dataField) "
            + "values ('Can I Get the Auto Increment Field?')");

    //
    // Use the MySQL LAST_INSERT_ID()
    // function to do the same thing as getGeneratedKeys()
    //

    int autoIncKeyFromFunc = -1;
    rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");

    if (rs.next()) {
        autoIncKeyFromFunc = rs.getInt(1);
    } else {
        // throw an exception from here
    }

    rs.close();

    System.out.println("Key returned from " +
                       "'SELECT LAST_INSERT_ID()': " +
                       autoIncKeyFromFunc);

} finally {

    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException ex) {
            // ignore
        }
    }

    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException ex) {
            // ignore
        }
    }
}

   Example 10. Retrieving AUTO_INCREMENT column values in
   Updatable ResultSets
   Statement stmt = null;
   ResultSet rs = null;

   try {

    //
    // Create a Statement instance that we can use for
    // 'normal' result sets as well as an 'updatable'
    // one, assuming you have a Connection 'conn' to
    // a MySQL database already available
    //

    stmt = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                                java.sql.ResultSet.CONCUR_UPDATABLE);

    //
    // Issue the DDL queries for the table for this example
    //

    stmt.executeUpdate("DROP TABLE IF EXISTS autoIncTutorial");
    stmt.executeUpdate(
            "CREATE TABLE autoIncTutorial ("
            + "priKey INT NOT NULL AUTO_INCREMENT, "
            + "dataField VARCHAR(64), PRIMARY KEY (priKey))");

    //
    // Example of retrieving an AUTO INCREMENT key
    // from an updatable result set
    //

    rs = stmt.executeQuery("SELECT priKey, dataField "
       + "FROM autoIncTutorial");

    rs.moveToInsertRow();

    rs.updateString("dataField", "AUTO INCREMENT here?");
    rs.insertRow();

    //
    // the driver adds rows at the end
    //

    rs.last();

    //
    // We should now be on the row we just inserted
    //

    int autoIncKeyFromRS = rs.getInt("priKey");

    rs.close();

    rs = null;

    System.out.println("Key returned for inserted row: "
        + autoIncKeyFromRS);

} finally {

    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException ex) {
            // ignore
        }
    }

    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException ex) {
            // ignore
        }
    }
}



   When you run the preceding example code, you should get the
   following output: Key returned from getGeneratedKeys(): 1 Key
   returned from SELECT LAST_INSERT_ID(): 1 Key returned for
   inserted row: 2 You should be aware, that at times, it can be
   tricky to use the SELECT LAST_INSERT_ID() query, as that
   function's value is scoped to a connection. So, if some other
   query happens on the same connection, the value will be
   overwritten. On the other hand, the getGeneratedKeys() method
   is scoped by the Statement instance, so it can be used even
   if other queries happen on the same connection, but not on
   the same Statement instance.

1.5.2. Using Connector/J with J2EE and Other Java Frameworks

   This section describes how to use Connector/J in several
   contexts.

1.5.2.1. General J2EE Concepts

   This section provides general background on J2EE concepts
   that pertain to use of Connector/J.

1.5.2.1.1. Understanding Connection Pooling

   Connection pooling is a technique of creating and managing a
   pool of connections that are ready for use by any thread that
   needs them.

   This technique of pooling connections is based on the fact
   that most applications only need a thread to have access to a
   JDBC connection when they are actively processing a
   transaction, which usually take only milliseconds to
   complete. When not processing a transaction, the connection
   would otherwise sit idle. Instead, connection pooling allows
   the idle connection to be used by some other thread to do
   useful work.

   In practice, when a thread needs to do work against a MySQL
   or other database with JDBC, it requests a connection from
   the pool. When the thread is finished using the connection,
   it returns it to the pool, so that it may be used by any
   other threads that want to use it.

   When the connection is loaned out from the pool, it is used
   exclusively by the thread that requested it. From a
   programming point of view, it is the same as if your thread
   called DriverManager.getConnection() every time it needed a
   JDBC connection, however with connection pooling, your thread
   may end up using either a new, or already-existing
   connection.

   Connection pooling can greatly increase the performance of
   your Java application, while reducing overall resource usage.
   The main benefits to connection pooling are:
     * Reduced connection creation time
       Although this is not usually an issue with the quick
       connection setup that MySQL offers compared to other
       databases, creating new JDBC connections still incurs
       networking and JDBC driver overhead that will be avoided
       if connections are recycled.
     * Simplified programming model
       When using connection pooling, each individual thread can
       act as though it has created its own JDBC connection,
       allowing you to use straight-forward JDBC programming
       techniques.
     * Controlled resource usage
       If you don't use connection pooling, and instead create a
       new connection every time a thread needs one, your
       application's resource usage can be quite wasteful and
       lead to unpredictable behavior under load.

   Remember that each connection to MySQL has overhead (memory,
   CPU, context switches, and so forth) on both the client and
   server side. Every connection limits how many resources there
   are available to your application as well as the MySQL
   server. Many of these resources will be used whether or not
   the connection is actually doing any useful work!

   Connection pools can be tuned to maximize performance, while
   keeping resource utilization below the point where your
   application will start to fail rather than just run slower.

   Luckily, Sun has standardized the concept of connection
   pooling in JDBC through the JDBC-2.0 Optional interfaces, and
   all major application servers have implementations of these
   APIs that work fine with MySQL Connector/J.

   Generally, you configure a connection pool in your
   application server configuration files, and access it via the
   Java Naming and Directory Interface (JNDI). The following
   code shows how you might use a connection pool from an
   application deployed in a J2EE application server:

   Example 11. Using a connection pool with a J2EE application
   server
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;


public class MyServletJspOrEjb {

    public void doSomething() throws Exception {
        /*
         * Create a JNDI Initial context to be able to
         *  lookup  the DataSource
         *
         * In production-level code, this should be cached as
         * an instance or static variable, as it can
         * be quite expensive to create a JNDI context.
         *
         * Note: This code only works when you are using servlets
         * or EJBs in a J2EE application server. If you are
         * using connection pooling in standalone Java code, you
         * will have to create/configure datasources using whatever
         * mechanisms your particular connection pooling library
         * provides.
         */

        InitialContext ctx = new InitialContext();

         /*
          * Lookup the DataSource, which will be backed by a pool
          * that the application server provides. DataSource instances
          * are also a good candidate for caching as an instance
          * variable, as JNDI lookups can be expensive as well.
          */

        DataSource ds =
          (DataSource)ctx.lookup("java:comp/env/jdbc/MySQLDB");

        /*
         * The following code is what would actually be in your
         * Servlet, JSP or EJB 'service' method...where you need
         * to work with a JDBC connection.
         */

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = ds.getConnection();

            /*
             * Now, use normal JDBC programming to work with
             * MySQL, making sure to close each resource when you're
             * finished with it, which allows the connection pool
             * resources to be recovered as quickly as possible
             */

            stmt = conn.createStatement();
            stmt.execute("SOME SQL QUERY");

            stmt.close();
            stmt = null;

            conn.close();
            conn = null;
        } finally {
            /*
             * close any jdbc instances here that weren't
             * explicitly closed during normal code path, so
             * that we don't 'leak' resources...
             */

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (sqlexception sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (sqlexception sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                conn = null;
            }
        }
    }
}

   As shown in the example above, after obtaining the JNDI
   InitialContext, and looking up the DataSource, the rest of
   the code should look familiar to anyone who has done JDBC
   programming in the past.

   The most important thing to remember when using connection
   pooling is to make sure that no matter what happens in your
   code (exceptions, flow-of-control, and so forth),
   connections, and anything created by them (such as statements
   or result sets) are closed, so that they may be re-used,
   otherwise they will be stranded, which in the best case means
   that the MySQL server resources they represent (such as
   buffers, locks, or sockets) may be tied up for some time, or
   worst case, may be tied up forever.

   What's the Best Size for my Connection Pool?

   As with all other configuration rules-of-thumb, the answer
   is: it depends. Although the optimal size depends on
   anticipated load and average database transaction time, the
   optimum connection pool size is smaller than you might
   expect. If you take Sun's Java Petstore blueprint application
   for example, a connection pool of 15-20 connections can serve
   a relatively moderate load (600 concurrent users) using MySQL
   and Tomcat with response times that are acceptable.

   To correctly size a connection pool for your application, you
   should create load test scripts with tools such as Apache
   JMeter or The Grinder, and load test your application.

   An easy way to determine a starting point is to configure
   your connection pool's maximum number of connections to be
   unbounded, run a load test, and measure the largest amount of
   concurrently used connections. You can then work backward
   from there to determine what values of minimum and maximum
   pooled connections give the best performance for your
   particular application.

1.5.2.2. Using Connector/J with Tomcat

   The following instructions are based on the instructions for
   Tomcat-5.x, available at
   http://jakarta.apache.org/tomcat/tomcat-5.0-doc/jndi-datasour
   ce-examples-howto.html which is current at the time this
   document was written.

   First, install the .jar file that comes with Connector/J in
   $CATALINA_HOME/common/lib so that it is available to all
   applications installed in the container.

   Next, Configure the JNDI DataSource by adding a declaration
   resource to $CATALINA_HOME/conf/server.xml in the context
   that defines your web application:
<Context ....>

  ...

  <Resource name="jdbc/MySQLDB"
               auth="Container"
               type="javax.sql.DataSource"/>

  <!-- The name you used above, must match _exactly_ here!

       The connection pool will be bound into JNDI with the name
       "java:/comp/env/jdbc/MySQLDB"
  -->

  <ResourceParams name="jdbc/MySQLDB">
    <parameter>
      <name>factory</name>
      <value>org.apache.commons.dbcp.BasicDataSourceFactory</value>
    </parameter>

    <!-- Don't set this any higher than max_connections on your
         MySQL server, usually this should be a 10 or a few 10's
         of connections, not hundreds or thousands -->

    <parameter>
      <name>maxActive</name>
      <value>10</value>
    </parameter>

    <!-- You don't want to many idle connections hanging around
         if you can avoid it, only enough to soak up a spike in
         the load -->

    <parameter>
      <name>maxIdle</name>
      <value>5</value>
    </parameter>

    <!-- Don't use autoReconnect=true, it's going away eventually
         and it's a crutch for older connection pools that couldn't
         test connections. You need to decide whether your application
         is supposed to deal with SQLExceptions (hint, it should), and
         how much of a performance penalty you're willing to pay
         to ensure 'freshness' of the connection -->

    <parameter>
      <name>validationQuery</name>
      <value>SELECT 1</value>
    </parameter>

   <!-- The most conservative approach is to test connections
        before they're given to your application. For most application
s
        this is okay, the query used above is very small and takes
        no real server resources to process, other than the time used
        to traverse the network.

        If you have a high-load application you'll need to rely on
        something else. -->

    <parameter>
      <name>testOnBorrow</name>
      <value>true</value>
    </parameter>

   <!-- Otherwise, or in addition to testOnBorrow, you can test
        while connections are sitting idle -->

    <parameter>
      <name>testWhileIdle</name>
      <value>true</value>
    </parameter>

    <!-- You have to set this value, otherwise even though
         you've asked connections to be tested while idle,
         the idle evicter thread will never run -->

    <parameter>
      <name>timeBetweenEvictionRunsMillis</name>
      <value>10000</value>
    </parameter>

    <!-- Don't allow connections to hang out idle too long,
         never longer than what wait_timeout is set to on the
         server...A few minutes or even fraction of a minute
         is sometimes okay here, it depends on your application
         and how much spikey load it will see -->

    <parameter>
      <name>minEvictableIdleTimeMillis</name>
      <value>60000</value>
    </parameter>

    <!-- Username and password used when connecting to MySQL -->

    <parameter>
     <name>username</name>
     <value>someuser</value>
    </parameter>

    <parameter>
     <name>password</name>
     <value>somepass</value>
    </parameter>

    <!-- Class name for the Connector/J driver -->

    <parameter>
       <name>driverClassName</name>
       <value>com.mysql.jdbc.Driver</value>
    </parameter>

    <!-- The JDBC connection url for connecting to MySQL, notice
         that if you want to pass any other MySQL-specific parameters
         you should pass them here in the URL, setting them using the
         parameter tags above will have no effect, you will also
         need to use &amp; to separate parameter values as the
         ampersand is a reserved character in XML -->

    <parameter>
      <name>url</name>
      <value>jdbc:mysql://localhost:3306/test</value>
    </parameter>

  </ResourceParams>
</Context>

   In general, you should follow the installation instructions
   that come with your version of Tomcat, as the way you
   configure datasources in Tomcat changes from time-to-time,
   and unfortunately if you use the wrong syntax in your XML
   file, you will most likely end up with an exception similar
   to the following:
Error: java.sql.SQLException: Cannot load JDBC driver class 'null ' SQ
L
state: null

1.5.2.3. Using Connector/J with JBoss

   These instructions cover JBoss-4.x. To make the JDBC driver
   classes available to the application server, copy the .jar
   file that comes with Connector/J to the lib directory for
   your server configuration (which is usually called default).
   Then, in the same configuration directory, in the
   subdirectory named deploy, create a datasource configuration
   file that ends with "-ds.xml", which tells JBoss to deploy
   this file as a JDBC Datasource. The file should have the
   following contents:
<datasources>
    <local-tx-datasource>
        <!-- This connection pool will be bound into JNDI with the nam
e
             "java:/MySQLDB" -->

        <jndi-name>MySQLDB</jndi-name>
        <connection-url>jdbc:mysql://localhost:3306/dbname</connection
-url>
        <driver-class>com.mysql.jdbc.Driver</driver-class>
        <user-name>user</user-name>
        <password>pass</password>

        <min-pool-size>5</min-pool-size>

        <!-- Don't set this any higher than max_connections on your
         MySQL server, usually this should be a 10 or a few 10's
         of connections, not hundreds or thousands -->

        <max-pool-size>20</max-pool-size>

        <!-- Don't allow connections to hang out idle too long,
         never longer than what wait_timeout is set to on the
         server...A few minutes is usually okay here,
         it depends on your application
         and how much spikey load it will see -->

        <idle-timeout-minutes>5</idle-timeout-minutes>

        <!-- If you're using Connector/J 3.1.8 or newer, you can use
             our implementation of these to increase the robustness
             of the connection pool. -->

        <exception-sorter-class-name>
  com.mysql.jdbc.integration.jboss.ExtendedMysqlExceptionSorter
        </exception-sorter-class-name>
        <valid-connection-checker-class-name>
  com.mysql.jdbc.integration.jboss.MysqlValidConnectionChecker
        </valid-connection-checker-class-name>

    </local-tx-datasource>
</datasources>

1.5.2.4. Using Connector/J with Spring

   The Spring Framework is a Java-based application framework
   designed for assisting in application design by providing a
   way to configure components. The technique used by Spring is
   a well known design pattern called Dependency Injection (see
   Inversion of Control Containers and the Dependency Injection
   pattern
   (http://www.martinfowler.com/articles/injection.html)). This
   article will focus on Java-oriented access to MySQL databases
   with Spring 2.0. For those wondering, there is a .NET port of
   Spring appropriately named Spring.NET.

   Spring is not only a system for configuring components, but
   also includes support for aspect oriented programming (AOP).
   This is one of the main benefits and the foundation for
   Spring's resource and transaction management. Spring also
   provides utilities for integrating resource management with
   JDBC and Hibernate.

   For the examples in this section the MySQL world sample
   database will be used. The first task is to setup a MySQL
   data source through Spring. Components within Spring use the
   "bean" terminology. For example, to configure a connection to
   a MySQL server supporting the world sample database you might
   use:
<util:map id="dbProps">
    <entry key="db.driver" value="com.mysql.jdbc.Driver"/>
    <entry key="db.jdbcurl" value="jdbc:mysql://localhost/world"/>
    <entry key="db.username" value="myuser"/>
    <entry key="db.password" value="mypass"/>
</util:map>


   In the above example we are assigning values to properties
   that will be used in the configuration. For the datasource
   configuration:
<bean id="dataSource"
       class="org.springframework.jdbc.datasource.DriverManagerDataSou
rce">
    <property name="driverClassName" value="${db.driver}"/>
    <property name="url" value="${db.jdbcurl}"/>
    <property name="username" value="${db.username}"/>
    <property name="password" value="${db.password}"/>
</bean>

   The placeholders are used to provide values for properties of
   this bean. This means that you can specify all the properties
   of the configuration in one place instead of entering the
   values for each property on each bean. We do, however, need
   one more bean to pull this all together. The last bean is
   responsible for actually replacing the placeholders with the
   property values.
<bean
 class="org.springframework.beans.factory.config.PropertyPlaceholderCo
nfigurer">
    <property name="properties" ref="dbProps"/>
</bean>

   Now that we have our MySQL data source configured and ready
   to go, we write some Java code to access it. The example
   below will retrieve three random cities and their
   corresponding country using the data source we configured
   with Spring.
// Create a new application context. this processes the Spring config
ApplicationContext ctx =
    new ClassPathXmlApplicationContext("ex1appContext.xml");
// Retrieve the data source from the application context
    DataSource ds = (DataSource) ctx.getBean("dataSource");
// Open a database connection using Spring's DataSourceUtils
Connection c = DataSourceUtils.getConnection(ds);
try {
    // retrieve a list of three random cities
    PreparedStatement ps = c.prepareStatement(
        "select City.Name as 'City', Country.Name as 'Country' " +
        "from City inner join Country on City.CountryCode = Country.Co
de " +
        "order by rand() limit 3");
    ResultSet rs = ps.executeQuery();
    while(rs.next()) {
        String city = rs.getString("City");
        String country = rs.getString("Country");
        System.out.printf("The city %s is in %s%n", city, country);
    }
} catch (SQLException ex) {
    // something has failed and we print a stack trace to analyse the
error
    ex.printStackTrace();
    // ignore failure closing connection
    try { c.close(); } catch (SQLException e) { }
} finally {
    // properly release our connection
    DataSourceUtils.releaseConnection(c, ds);
}

   This is very similar to normal JDBC access to MySQL with the
   main difference being that we are using DataSourceUtils
   instead of the DriverManager to create the connection.

   While it may seem like a small difference, the implications
   are somewhat far reaching. Spring manages this resource in a
   way similar to a container managed data source in a J2EE
   application server. When a connection is opened, it can be
   subsequently accessed in other parts of the code if it is
   synchronized with a transaction. This makes it possible to
   treat different parts of your application as transactional
   instead of passing around a database connection.

1.5.2.4.1. Using JdbcTemplate

   Spring makes extensive use of the Template method design
   pattern (see Template Method Pattern
   (http://en.wikipedia.org/wiki/Template_method_pattern)). Our
   immediate focus will be on the JdbcTemplate and related
   classes, specifically NamedParameterJdbcTemplate. The
   template classes handle obtaining and releasing a connection
   for data access when one is needed.

   The next example shows how to use NamedParameterJdbcTemplate
   inside of a DAO (Data Access Object) class to retrieve a
   random city given a country code.
public class Ex2JdbcDao {
     /**
     * Data source reference which will be provided by Spring.
     */
     private DataSource dataSource;

     /**
     * Our query to find a random city given a country code. Notice
     * the ":country" parameter towards the end. This is called a
     * named parameter.
     */
     private String queryString = "select Name from City " +
        "where CountryCode = :country order by rand() limit 1";

     /**
     * Retrieve a random city using Spring JDBC access classes.
     */
     public String getRandomCityByCountryCode(String cntryCode) {
         // A template that allows using queries with named parameters
         NamedParameterJdbcTemplate template =
         new NamedParameterJdbcTemplate(dataSource);
         // A java.util.Map is used to provide values for the paramete
rs
         Map params = new HashMap();
         params.put("country", cntryCode);
         // We query for an Object and specify what class we are expec
ting
         return (String)template.queryForObject(queryString, params, S
tring.class);
     }

    /**
    * A JavaBean setter-style method to allow Spring to inject the dat
a source.
    * @param dataSource
    */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

   The focus in the above code is on the
   getRandomCityByCountryCode() method. We pass a country code
   and use the NamedParameterJdbcTemplate to query for a city.
   The country code is placed in a Map with the key "country",
   which is the parameter is named in the SQL query.

   To access this code, you need to configure it with Spring by
   providing a reference to the data source.
<bean id="dao" class="code.Ex2JdbcDao">
    <property name="dataSource" ref="dataSource"/>
</bean>

   At this point, we can just grab a reference to the DAO from
   Spring and call getRandomCityByCountryCode().
// Create the application context
    ApplicationContext ctx =
    new ClassPathXmlApplicationContext("ex2appContext.xml");
    // Obtain a reference to our DAO
    Ex2JdbcDao dao = (Ex2JdbcDao) ctx.getBean("dao");

    String countryCode = "USA";

    // Find a few random cities in the US
    for(int i = 0; i < 4; ++i)
        System.out.printf("A random city in %s is %s%n", countryCode,
            dao.getRandomCityByCountryCode(countryCode));

   This example shows how to use Spring's JDBC classes to
   completely abstract away the use of traditional JDBC classes
   including Connection and PreparedStatement.

1.5.2.4.2. Transactional JDBC Access

   You might be wondering how we can add transactions into our
   code if we don't deal directly with the JDBC classes. Spring
   provides a transaction management package that not only
   replaces JDBC transaction management, but also allows
   declarative transaction management (configuration instead of
   code).

   In order to use transactional database access, we will need
   to change the storage engine of the tables in the world
   database. The downloaded script explicitly creates MyISAM
   tables which do not support transactional semantics. The
   InnoDB storage engine does support transactions and this is
   what we will be using. We can change the storage engine with
   the following statements.
ALTER TABLE City ENGINE=InnoDB;
ALTER TABLE Country ENGINE=InnoDB;
ALTER TABLE CountryLanguage ENGINE=InnoDB;

   A good programming practice emphasized by Spring is
   separating interfaces and implementations. What this means is
   that we can create a Java interface and only use the
   operations on this interface without any internal knowledge
   of what the actual implementation is. We will let Spring
   manage the implementation and with this it will manage the
   transactions for our implementation.

   First you create a simple interface:
public interface Ex3Dao {
    Integer createCity(String name, String countryCode,
    String district, Integer population);
}

   This interface contains one method that will create a new
   city record in the database and return the id of the new
   record. Next you need to create an implementation of this
   interface.
public class Ex3DaoImpl implements Ex3Dao {
    protected DataSource dataSource;
    protected SqlUpdate updateQuery;
    protected SqlFunction idQuery;

    public Integer createCity(String name, String countryCode,
        String district, Integer population) {
            updateQuery.update(new Object[] { name, countryCode,
                   district, population });
            return getLastId();
        }

    protected Integer getLastId() {
        return idQuery.run();
    }
}

   You can see that we only operate on abstract query objects
   here and don't deal directly with the JDBC API. Also, this is
   the complete implementation. All of our transaction
   management will be dealt with in the configuration. To get
   the configuration started, we need to create the DAO.
<bean id="dao" class="code.Ex3DaoImpl">
    <property name="dataSource" ref="dataSource"/>
    <property name="updateQuery">...</property>
    <property name="idQuery">...</property>
</bean>

   Now you need to setup the transaction configuration. The
   first thing you must do is create transaction manager to
   manage the data source and a specification of what
   transaction properties are required for for the dao methods.
<bean id="transactionManager"
  class="org.springframework.jdbc.datasource.DataSourceTransactionMana
ger">
    <property name="dataSource" ref="dataSource"/>
</bean>

<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="*"/>
    </tx:attributes>
</tx:advice>

   The preceding code creates a transaction manager that handles
   transactions for the data source provided to it. The txAdvice
   uses this transaction manager and the attributes specify to
   create a transaction for all methods. Finally you need to
   apply this advice with an AOP pointcut.
<aop:config>
    <aop:pointcut id="daoMethods"
        expression="execution(* code.Ex3Dao.*(..))"/>
     <aop:advisor advice-ref="txAdvice" pointcut-ref="daoMethods"/>
</aop:config>

   This basically says that all methods called on the Ex3Dao
   interface will be wrapped in a transaction. To make use of
   this, you only have to retrieve the dao from the application
   context and call a method on the dao instance.
Ex3Dao dao = (Ex3Dao) ctx.getBean("dao");
Integer id = dao.createCity(name,  countryCode, district, pop);

   We can verify from this that there is no transaction
   management happening in our Java code and it's all configured
   with Spring. This is a very powerful notion and regarded as
   one of the most beneficial features of Spring.

1.5.2.4.3. Connection Pooling

   In many sitations, such as web applications, there will be a
   large number of small database transactions. When this is the
   case, it usually makes sense to create a pool of database
   connections available for web requests as needed. Although
   MySQL does not spawn an extra process when a connection is
   made, there is still a small amount of overhead to create and
   setup the connection. Pooling of connections also alleviates
   problems such as collecting large amounts of sockets in the
   TIME_WAIT state.

   Setting up pooling of MySQL connections with Spring is as
   simple as changing the data source configuration in the
   application context. There are a number of configurations
   that we can use. The first example is based on the Jakarta
   Commons DBCP library
   (http://jakarta.apache.org/commons/dbcp/). The example below
   replaces the source configuration that was based on
   DriverManagerDataSource with DBCP's BasicDataSource.
<bean id="dataSource" destroy-method="close"
  class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="${db.driver}"/>
    <property name="url" value="${db.jdbcurl}"/>
    <property name="username" value="${db.username}"/>
    <property name="password" value="${db.password}"/>
    <property name="initialSize" value="3"/>
</bean>

   The configuration of the two solutions is very similar. The
   difference is that DBCP will pool connections to the database
   instead of creating a new connection every time one is
   requested. We have also set a parameter here called
   initialSize. This tells DBCP that we want three connections
   in the pool when it is created.

   Another way to configure connection pooling is to configure a
   data source in our J2EE application server. Using JBoss as an
   example, you can set up the MySQL connection pool by creating
   a file called mysql-local-ds.xml and placing it in the
   server/default/deploy directory in JBoss. Once we have this
   setup, we can use JNDI to look it up. With Spring, this
   lookup is very simple. The data source configuration looks
   like this.
<jee:jndi-lookup id="dataSource" jndi-name="java:MySQL_DS"/>

1.5.3. Common Problems and Solutions

   There are a few issues that seem to be commonly encountered
   often by users of MySQL Connector/J. This section deals with
   their symptoms, and their resolutions.

   Questions
     * [1]1.5.3.1: When I try to connect to the database with
       MySQL Connector/J, I get the following exception:
SQLException: Server configuration denies access to data source
SQLState: 08001
VendorError: 0
       What's going on? I can connect just fine with the MySQL
       command-line client.
     * [2]1.5.3.2: My application throws an SQLException 'No
       Suitable Driver'. Why is this happening?
     * [3]1.5.3.3: I'm trying to use MySQL Connector/J in an
       applet or application and I get an exception similar to:
SQLException: Cannot connect to MySQL server on host:3306.
Is there a MySQL server running on the machine/port you
are trying to connect to?

(java.security.AccessControlException)
SQLState: 08S01
VendorError: 0
     * [4]1.5.3.4: I have a servlet/application that works fine
       for a day, and then stops working overnight
     * [5]1.5.3.5: I'm trying to use JDBC-2.0 updatable result
       sets, and I get an exception saying my result set is not
       updatable.
     * [6]1.5.3.6: I cannot connect to the MySQL server using
       Connector/J, and I'm sure the connection paramters are
       correct.
     * [7]1.5.3.7: I am trying to connect to my MySQL server
       within my application, but I get the following error and
       stack trace:
java.net.SocketException
MESSAGE: Software caused connection abort: recv failed

STACKTRACE:

java.net.SocketException: Software caused connection abort: recv faile
d
at java.net.SocketInputStream.socketRead0(Native Method)
at java.net.SocketInputStream.read(Unknown Source)
at com.mysql.jdbc.MysqlIO.readFully(MysqlIO.java:1392)
at com.mysql.jdbc.MysqlIO.readPacket(MysqlIO.java:1414)
at com.mysql.jdbc.MysqlIO.doHandshake(MysqlIO.java:625)
at com.mysql.jdbc.Connection.createNewIO(Connection.java:1926)
at com.mysql.jdbc.Connection.<init>(Connection.java:452)
at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.ja
va:411)
     * [8]1.5.3.8: My application is deployed through JBoss and
       I am using transactions to handle the statements on the
       MySQL database. Under heavy loads I am getting a error
       and stack trace, but these only occur after a fixed
       period of heavy activity.
     * [9]1.5.3.9: When using gcj an
       java.io.CharConversionException is raised when working
       with certain character sequences.
     * [10]1.5.3.10: Updating a table that contains a primary
       key that is either FLOAT or compound primary key that
       uses FLOAT fails to update the table and raises an
       exception.

   Questions and Answers

   1.5.3.1: When I try to connect to the database with MySQL
   Connector/J, I get the following exception:
SQLException: Server configuration denies access to data source
SQLState: 08001
VendorError: 0

   What's going on? I can connect just fine with the MySQL
   command-line client.

   MySQL Connector/J must use TCP/IP sockets to connect to
   MySQL, as Java does not support Unix Domain Sockets.
   Therefore, when MySQL Connector/J connects to MySQL, the
   security manager in MySQL server will use its grant tables to
   determine whether the connection should be allowed.

   You must add the necessary security credentials to the MySQL
   server for this to happen, using the GRANT statement to your
   MySQL Server. See GRANT Syntax
   (http://dev.mysql.com/doc/refman/5.0/en/grant.html), for more
   information.

Note

   Testing your connectivity with the mysql command-line client
   will not work unless you add the --host flag, and use
   something other than localhost for the host. The mysql
   command-line client will use Unix domain sockets if you use
   the special hostname localhost. If you are testing
   connectivity to localhost, use 127.0.0.1 as the hostname
   instead.

Warning

   Changing privileges and permissions improperly in MySQL can
   potentially cause your server installation to not have
   optimal security properties.

   1.5.3.2: My application throws an SQLException 'No Suitable
   Driver'. Why is this happening?

   There are three possible causes for this error:
     * The Connector/J driver is not in your CLASSPATH, see
       Section Section, "Connector/J Installation."
     * The format of your connection URL is incorrect, or you
       are referencing the wrong JDBC driver.
     * When using DriverManager, the jdbc.drivers system
       property has not been populated with the location of the
       Connector/J driver.

   1.5.3.3: I'm trying to use MySQL Connector/J in an applet or
   application and I get an exception similar to:
SQLException: Cannot connect to MySQL server on host:3306.
Is there a MySQL server running on the machine/port you
are trying to connect to?

(java.security.AccessControlException)
SQLState: 08S01
VendorError: 0

   Either you're running an Applet, your MySQL server has been
   installed with the "--skip-networking" option set, or your
   MySQL server has a firewall sitting in front of it.

   Applets can only make network connections back to the machine
   that runs the web server that served the .class files for the
   applet. This means that MySQL must run on the same machine
   (or you must have some sort of port re-direction) for this to
   work. This also means that you will not be able to test
   applets from your local file system, you must always deploy
   them to a web server.

   MySQL Connector/J can only communicate with MySQL using
   TCP/IP, as Java does not support Unix domain sockets. TCP/IP
   communication with MySQL might be affected if MySQL was
   started with the "--skip-networking" flag, or if it is
   firewalled.

   If MySQL has been started with the "--skip-networking" option
   set (the Debian Linux package of MySQL server does this for
   example), you need to comment it out in the file
   /etc/mysql/my.cnf or /etc/my.cnf. Of course your my.cnf file
   might also exist in the data directory of your MySQL server,
   or anywhere else (depending on how MySQL was compiled for
   your system). Binaries created by MySQL AB always look in
   /etc/my.cnf and [datadir]/my.cnf. If your MySQL server has
   been firewalled, you will need to have the firewall
   configured to allow TCP/IP connections from the host where
   your Java code is running to the MySQL server on the port
   that MySQL is listening to (by default, 3306).

   1.5.3.4: I have a servlet/application that works fine for a
   day, and then stops working overnight

   MySQL closes connections after 8 hours of inactivity. You
   either need to use a connection pool that handles stale
   connections or use the "autoReconnect" parameter (see Section
   Section, "Driver/Datasource Class Names, URL Syntax and
   Configuration Properties for Connector/J").

   Also, you should be catching SQLExceptions in your
   application and dealing with them, rather than propagating
   them all the way until your application exits, this is just
   good programming practice. MySQL Connector/J will set the
   SQLState (see java.sql.SQLException.getSQLState() in your
   APIDOCS) to "08S01" when it encounters network-connectivity
   issues during the processing of a query. Your application
   code should then attempt to re-connect to MySQL at this
   point.

   The following (simplistic) example shows what code that can
   handle these exceptions might look like:

   Example 12. Example of transaction with retry logic
public void doBusinessOp() throws SQLException {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    //
    // How many times do you want to retry the transaction
    // (or at least _getting_ a connection)?
    //
    int retryCount = 5;

    boolean transactionCompleted = false;

    do {
        try {
            conn = getConnection(); // assume getting this from a
                                    // javax.sql.DataSource, or the
                                    // java.sql.DriverManager

            conn.setAutoCommit(false);

            //
            // Okay, at this point, the 'retry-ability' of the
            // transaction really depends on your application logic,
            // whether or not you're using autocommit (in this case
            // not), and whether you're using transacational storage
            // engines
            //
            // For this example, we'll assume that it's _not_ safe
            // to retry the entire transaction, so we set retry
            // count to 0 at this point
            //
            // If you were using exclusively transaction-safe tables,
            // or your application could recover from a connection goi
ng
            // bad in the middle of an operation, then you would not
            // touch 'retryCount' here, and just let the loop repeat
            // until retryCount == 0.
            //
            retryCount = 0;

            stmt = conn.createStatement();

            String query = "SELECT foo FROM bar ORDER BY baz";

            rs = stmt.executeQuery(query);

            while (rs.next()) {
            }

            rs.close();
            rs = null;

            stmt.close();
            stmt = null;

            conn.commit();
            conn.close();
            conn = null;

            transactionCompleted = true;
        } catch (SQLException sqlEx) {

            //
            // The two SQL states that are 'retry-able' are 08S01
            // for a communications error, and 40001 for deadlock.
            //
            // Only retry if the error was due to a stale connection,
            // communications problem or deadlock
            //

            String sqlState = sqlEx.getSQLState();

            if ("08S01".equals(sqlState) || "40001".equals(sqlState))
{
                retryCount--;
            } else {
                retryCount = 0;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                    // You'd probably want to log this . . .
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    // You'd probably want to log this as well . . .
                }
            }

            if (conn != null) {
                try {
                    //
                    // If we got here, and conn is not null, the
                    // transaction should be rolled back, as not
                    // all work has been done

                    try {
                        conn.rollback();
                    } finally {
                        conn.close();
                    }
                } catch (SQLException sqlEx) {
                    //
                    // If we got an exception here, something
                    // pretty serious is going on, so we better
                    // pass it up the stack, rather than just
                    // logging it. . .

                    throw sqlEx;
                }
            }
        }
    } while (!transactionCompleted && (retryCount > 0));
}

Note

   Use of the autoReconnect option is not recommended because
   there is no safe method of reconnecting to the MySQL server
   without risking some corruption of the connection state or
   database state information. Instead, you should use a
   connection pool which will enable your application to connect
   to the MySQL server using an available connection from the
   pool. The autoReconnect facility is deprecated, and may be
   removed in a future release.

   1.5.3.5: I'm trying to use JDBC-2.0 updatable result sets,
   and I get an exception saying my result set is not updatable.

   Because MySQL does not have row identifiers, MySQL
   Connector/J can only update result sets that have come from
   queries on tables that have at least one primary key, the
   query must select every primary key and the query can only
   span one table (that is, no joins). This is outlined in the
   JDBC specification.

   Note that this issue only occurs when using updatable result
   sets, and is caused because Connector/J is unable to
   guarantee that it can identify the correct rows within the
   result set to be updated without having a unique reference to
   each row. There is no requirement to have a unique field on a
   table if you are using UPDATE or DELETE statements on a table
   where you can individually specify the criteria to be matched
   using a WHERE clause.

   1.5.3.6: I cannot connect to the MySQL server using
   Connector/J, and I'm sure the connection paramters are
   correct.

   Make sure that the skip-networking option has not been
   enabled on your server. Connector/J must be able to
   communicate with your server over TCP/IP, named sockets are
   not supported. Also ensure that you are not filtering
   connections through a Firewall or other network security
   system. For more informaiton, see Can't connect to [local]
   MySQL server
   (http://dev.mysql.com/doc/refman/5.0/en/can-not-connect-to-se
   rver.html).

   1.5.3.7: I am trying to connect to my MySQL server within my
   application, but I get the following error and stack trace:
java.net.SocketException
MESSAGE: Software caused connection abort: recv failed

STACKTRACE:

java.net.SocketException: Software caused connection abort: recv faile
d
at java.net.SocketInputStream.socketRead0(Native Method)
at java.net.SocketInputStream.read(Unknown Source)
at com.mysql.jdbc.MysqlIO.readFully(MysqlIO.java:1392)
at com.mysql.jdbc.MysqlIO.readPacket(MysqlIO.java:1414)
at com.mysql.jdbc.MysqlIO.doHandshake(MysqlIO.java:625)
at com.mysql.jdbc.Connection.createNewIO(Connection.java:1926)
at com.mysql.jdbc.Connection.<init>(Connection.java:452)
at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.ja
va:411)

   The error probably indicates that you are using a older
   version of the Connector/J JDBC driver (2.0.14 or 3.0.x) and
   you are trying to connect to a MySQL server with version 4.1x
   or newer. The older drivers are not compatible with 4.1 or
   newer of MySQL as they do not support the newer
   authentication mechanisms.

   It is likely that the older version of the Connector/J driver
   exists within your application directory or your CLASSPATH
   includes the older Connector/J package.

   1.5.3.8: My application is deployed through JBoss and I am
   using transactions to handle the statements on the MySQL
   database. Under heavy loads I am getting a error and stack
   trace, but these only occur after a fixed period of heavy
   activity.

   This is a JBoss, not Connector/J, issue and is connected to
   the use of transactions. Under heavy loads the time taken for
   transactions to complete can increase, and the error is
   caused because you have exceeded the predefined timeout.

   You can increase the timeout value by setting the
   TransactionTimeout attribute to the TransactionManagerService
   within the /conf/jboss-service.xml file (pre-4.0.3) or
   /deploy/jta-service.xml for JBoss 4.0.3 or later. See
   TransactionTimeoute
   (http://wiki.jboss.org/wiki/Wiki.jsp?page=TransactionTimeout)
   within the JBoss wiki for more information.

   1.5.3.9: When using gcj an java.io.CharConversionException is
   raised when working with certain character sequences.

   This is a known issue with gcj which raises an exception when
   it reaches an unknown character or one it cannot convert. You
   should add useJvmCharsetConverters=true to your connection
   string to force character conversion outside of the gcj
   libraries, or try a different JDK.

   1.5.3.10: Updating a table that contains a primary key that
   is either FLOAT or compound primary key that uses FLOAT fails
   to update the table and raises an exception.

   Connector/J adds conditions to the WHERE clause during an
   UPDATE to check the old values of the primary key. If there
   is no match then Connector/J considers this a failure
   condition and raises an exception.

   The problem is that rounding differences between supplied
   values and the values stored in the database may mean that
   the values never match, and hence the update fails. The issue
   will affect all queries, not just those from Connector/J.

   To prevent this issue, use a primary key that does not use
   FLOAT. If you have to use a floating point column in your
   primary key use DOUBLE or DECIMAL types in place of FLOAT.

1.6. Connector/J Support

1.6.1. Connector/J Community Support

   MySQL AB provides assistance to the user community by means
   of its mailing lists. For Connector/J related issues, you can
   get help from experienced users by using the MySQL and Java
   mailing list. Archives and subscription information is
   available online at http://lists.mysql.com/java.

   For information about subscribing to MySQL mailing lists or
   to browse list archives, visit http://lists.mysql.com/. See
   MySQL Mailing Lists
   (http://dev.mysql.com/doc/refman/5.1/en/mailing-lists.html).

   Community support from experienced users is also available
   through the JDBC Forum (http://forums.mysql.com/list.php?39).
   You may also find help from other users in the other MySQL
   Forums, located at http://forums.mysql.com. See MySQL
   Community Support at the MySQL Forums
   (http://dev.mysql.com/doc/refman/5.1/en/forums.html).

1.6.2. How to Report Connector/J Bugs or Problems

   The normal place to report bugs is http://bugs.mysql.com/,
   which is the address for our bugs database. This database is
   public, and can be browsed and searched by anyone. If you log
   in to the system, you will also be able to enter new reports.

   If you have found a sensitive security bug in MySQL, you can
   send email to security_at_mysql.com
   (mailto:security_at_mysql.com).

   Writing a good bug report takes patience, but doing it right
   the first time saves time both for us and for yourself. A
   good bug report, containing a full test case for the bug,
   makes it very likely that we will fix the bug in the next
   release.

   This section will help you write your report correctly so
   that you don't waste your time doing things that may not help
   us much or at all.

   If you have a repeatable bug report, please report it to the
   bugs database at http://bugs.mysql.com/. Any bug that we are
   able to repeat has a high chance of being fixed in the next
   MySQL release.

   To report other problems, you can use one of the MySQL
   mailing lists.

   Remember that it is possible for us to respond to a message
   containing too much information, but not to one containing
   too little. People often omit facts because they think they
   know the cause of a problem and assume that some details
   don't matter.

   A good principle is this: If you are in doubt about stating
   something, state it. It is faster and less troublesome to
   write a couple more lines in your report than to wait longer
   for the answer if we must ask you to provide information that
   was missing from the initial report.

   The most common errors made in bug reports are (a) not
   including the version number of Connector/J or MySQL used,
   and (b) not fully describing the platform on which
   Connector/J is installed (including the JVM version, and the
   platform type and version number that MySQL itself is
   installed on).

   This is highly relevant information, and in 99 cases out of
   100, the bug report is useless without it. Very often we get
   questions like, "Why doesn't this work for me?" Then we find
   that the feature requested wasn't implemented in that MySQL
   version, or that a bug described in a report has already been
   fixed in newer MySQL versions.

   Sometimes the error is platform-dependent; in such cases, it
   is next to impossible for us to fix anything without knowing
   the operating system and the version number of the platform.

   If at all possible, you should create a repeatable, stanalone
   testcase that doesn't involve any third-party classes.

   To streamline this process, we ship a base class for
   testcases with Connector/J, named
   'com.mysql.jdbc.util.BaseBugReport'. To create a testcase for
   Connector/J using this class, create your own class that
   inherits from com.mysql.jdbc.util.BaseBugReport and override
   the methods setUp(), tearDown() and runTest().

   In the setUp() method, create code that creates your tables,
   and populates them with any data needed to demonstrate the
   bug.

   In the runTest() method, create code that demonstrates the
   bug using the tables and data you created in the setUp
   method.

   In the tearDown() method, drop any tables you created in the
   setUp() method.

   In any of the above three methods, you should use one of the
   variants of the getConnection() method to create a JDBC
   connection to MySQL:
     * getConnection() - Provides a connection to the JDBC URL
       specified in getUrl(). If a connection already exists,
       that connection is returned, otherwise a new connection
       is created.
     * getNewConnection() - Use this if you need to get a new
       connection for your bug report (i.e. there's more than
       one connection involved).
     * getConnection(String url) - Returns a connection using
       the given URL.
     * getConnection(String url, Properties props) - Returns a
       connection using the given URL and properties.

   If you need to use a JDBC URL that is different from
   'jdbc:mysql:///test', override the method getUrl() as well.

   Use the assertTrue(boolean expression) and assertTrue(String
   failureMessage, boolean expression) methods to create
   conditions that must be met in your testcase demonstrating
   the behavior you are expecting (vs. the behavior you are
   observing, which is why you are most likely filing a bug
   report).

   Finally, create a main() method that creates a new instance
   of your testcase, and calls the run method:
public static void main(String[] args) throws Exception {
      new MyBugReport().run();
 }

   Once you have finished your testcase, and have verified that
   it demonstrates the bug you are reporting, upload it with
   your bug report to http://bugs.mysql.com/.

1.6.3. Connector/J Change History

   The Connector/J Change History (Changelog) is located with
   the main Changelog for MySQL. See MySQL Connector/J Change
   History
   (http://dev.mysql.com/doc/refman/5.1/en/cj-news.html).

References

   1. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-1
   2. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-2
   3. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-3
   4. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-4
   5. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-5
   6. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-6
   7. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-7
   8. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-8
   9. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-9
  10. file://localhost/home/paul/bk-test/mysqldoc/refman-common/connector-j-nolink.html#qandaitem-1-5-3-10
