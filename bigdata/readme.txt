IS_STATIC_2010_6_35 
IS_STATIC_2010_6_0
IS_STATIC_2010_6_11 
IS_STATIC_2010_6_12 
IS_STATIC_2010_6_16 
IS_STATIC_2010_6_17 
IS_STATIC_2010_8_1
IS_STATIC_2010_8_23 
IS_STATIC_2010_8_25 
IS_STATIC_2010_8_32 

nohup ./startupAdminNode.sh jobs=testpagine,test|datanode=false >admin.log &
nohup ./deleteData.sh jobs=testpagine,test|datanode=false >delete.log &
nohup ./startupDataNode.sh &
dataset:

"^dataset:(hive|hdfs|file):.*$"
dataset:hdfs:m2m/data04/2010


ST_PID


org.apache.avro.AvroRuntimeException: Duplicate in union:null
        at org.apache.avro.Schema$UnionSchema.<init>(Schema.java:789)
        at org.apache.avro.Schema.createUnion(Schema.java:167)
        at org.apache.sqoop.connector.kite.util.KiteDataTypeUtil.createAvroFieldSchema(KiteDataTypeUtil.java:82)
        at org.apache.sqoop.connector.kite.util.KiteDataTypeUtil.createAvroSchema(KiteDataTypeUtil.java:65)
        at org.apache.sqoop.connector.kite.KiteDatasetExecutor.createDataset(KiteDatasetExecutor.java:63)
        at org.apache.sqoop.connector.kite.KiteLoader.getExecutor(KiteLoader.java:52)
        at org.apache.sqoop.connector.kite.KiteLoader.load(KiteLoader.java:62)
        at org.apache.sqoop.connector.kite.KiteLoader.load(KiteLoader.java:36)
        at org.apache.sqoop.job.mr.SqoopOutputFormatLoadExecutor$ConsumerThread.run(SqoopOutputFormatLoadExecutor.java:250)
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
        at java.lang.Thread.run(Thread.java:745)


SELECT ST_PID, ST_LOGINID, RE_EN_PID,ST_LOGINTIME, ST_CONNECTTIME, ST_UPDATETIME,   ST_TOTALWKTIME, ST_WKTIME, ST_RMNTIME,   ST_BERRORCODE, ST_WERRORCODE, ST_BALMCODE,ST_WALMCODE, ST_GPSSTA, ST_LONGITUDE,ST_LATITUDE, ST_ALTITUDE, ST_VELOCITY,ST_ORIENTATION, ST_SATICUNT, ST_SGNLQ,ST_STEPPOS, ST_ENGV, to_char(ST_OILLEV) as ST_OILLEV,ST_BATTERYVOL, ST_ERRDEALSTA, ST_CMMCTSCH,ST_UINTRESERV10, ST_UINTRESERV11, ST_UINTRESERV12,ST_UINTRESERV13, ST_UINTRESERV14, ST_UINTRESERV15,ST_UINTRESERV16, ST_UINTRESERV17, ST_UINTRESERV18,ST_UINTRESERV19, ST_UINTRESERV20, ST_UINTRESERV21,ST_UINTRESERV22, ST_UINTRESERV23, ST_UINTRESERV24,ST_UINTRESERV25, ST_UINTRESERV26, ST_UINTRESERV27,ST_UINTRESERV28, ST_UINTRESERV29, ST_UINTRESERV30,ST_UINTRESERV31, ST_UINTRESERV32, ST_FLOATRESERV13,ST_FLOATRESERV14, ST_FLOATRESERV15, ST_FLOATRESERV16,ST_FLOATRESERV17, ST_FLOATRESERV18, ST_FLOATRESERV19,ST_FLOATRESERV20, ST_FLOATRESERV21, ST_FLOATRESERV22,ST_FLOATRESERV23, ST_FLOATRESERV24, ST_FLOATRESERV25,ST_FLOATRESERV29, ST_FLOATRESERV30, ST_FLOATRESERV31,ST_FLOATRESERV32, ST_CHECKMARK, ST_STATE FROM ISS.IS_STATIC ${CONDITIONS}
-----------------------------------

1、    早期数据：10.11.16.243

用户名/密码：hx/edcr9637_

tns配置信息：

ISSBAK =

  (DESCRIPTION =

    (ADDRESS = (PROTOCOL = TCP)(HOST = 10.11.16.243)(PORT = 1521))

    (CONNECT_DATA =

      (SERVER = DEDICATED)

      (SERVICE_NAME = ISS)

    )

  )

a)  2010年数据

select * from iss.is_static t;

b)  2011.12.12-201203031年数据

select * from iss.is_static_20111212_20120331 t；

c）2012.04.01-2012.0901年数据

   select * from iss.is_static_20120401_20120901 t；

d）2013.12.11备份数据

iss.is_static_often_20131211 

iss.is_static_seldom_20131211

通过主键关联，如下语句关联所有查询慢。您着情考虑

select t.st_loginid,

       t.st_ma_serialno,

       t.st_state,

       t.st_logintime,

       t.st_connecttime,

       t.st_updatetime,

       t.st_totalwktime,

       t.st_rmntime,

       t.st_berrorcode,

       t.st_werrorcode,

       t.st_balmcode,

       t.st_walmcode,

       t.st_longitude,

       t.st_latitude,

       t.st_saticunt,

       t.st_steppos,

       t.st_engv,

       t.st_oillev,

       t.st_batteryvol,

       a.st_wktime,

       a.st_gpssta,

       a.st_velocity,

       a.st_orientation,

       a.st_sgnlq,

       a.st_errdealsta,

       a.st_cmmctsch,

       a.st_altitude,

       a.st_uintreserv10,

       a.st_uintreserv11,

       a.st_uintreserv12,

       a.st_uintreserv13,

       a.st_uintreserv14,

       a.st_uintreserv15,

       a.st_uintreserv16,

       a.st_uintreserv17,

       a.st_uintreserv18,

       a.st_uintreserv19,

       a.st_uintreserv20,

       a.st_uintreserv21,

       a.st_uintreserv22,

       a.st_uintreserv23,

       a.st_uintreserv24,

       a.st_uintreserv25,

       a.st_uintreserv26,

       a.st_uintreserv27,

       a.st_uintreserv28,

       a.st_uintreserv29,

       a.st_uintreserv30,

       a.st_uintreserv31,

       a.st_uintreserv32,

       a.st_floatreserv13,

       a.st_floatreserv14,

       a.st_floatreserv15,

       a.st_floatreserv16,

       a.st_floatreserv17,

       a.st_floatreserv18,

       a.st_floatreserv19,

       a.st_floatreserv20,

       a.st_floatreserv21,

       a.st_floatreserv22,

       a.st_floatreserv23,

       a.st_floatreserv24,

       a.st_floatreserv25,

       a.st_floatreserv26,

       a.st_floatreserv27,

       a.st_floatreserv28,

       a.st_floatreserv29,

       a.st_floatreserv30,

       a.st_floatreserv31,

       a.st_floatreserv32

  from iss.is_static_often_20131211 t,iss.is_static_seldom_20131211  a

where  t.st_pid = a.st_pid  

2、    125库：10.11.16.125

用户名/密码：  HX/www!31hx#com$cn

tns配置信息：

16.125 =

  (DESCRIPTION =

    (ADDRESS_LIST =

      (ADDRESS = (PROTOCOL = TCP)(HOST = 10.11.16.125)(PORT = 1521))

    )

    (CONNECT_DATA =

      (SERVICE_NAME = orcl)

    )

  )

设备历史常用字段数据表：newiss.is_static_often_20150410

设备历史非常用字段数据表：newiss.is_static_seldom_20150410

3、    123库：10.11.16.123

用户名/密码：  HX/www!31hx#com$cn

tns配置信息:

 

16.123 =

  (DESCRIPTION =

    (ADDRESS_LIST =

      (ADDRESS = (PROTOCOL = TCP)(HOST = 10.11.16.123)(PORT = 1521))

    )

    (CONNECT_DATA =

      (SERVICE_NAME = orcl)

    )

  )

数据库表名：is_static_often【历史数据常用字段表】,

is_static_seldom【历史数据非常用字段表】

Static表拆分成两张表static_often、static_seldom

select * from newiss.is_static_often t

select * from newiss.is_static_seldom t

通过主键进行关联。

其他：

1）、地址：10.11.16.24

2）、数据库实例名：iss

3）、用户名与密码：

HX              

www!31hx#com$cn  

4)、数据库表名：is_static

查询参考如下：

select * from iss.is_static t

监听配置参考如下：

16.24 =

  (DESCRIPTION =

    (ADDRESS_LIST =

      (ADDRESS = (PROTOCOL = TCP)(HOST = 10.11.16.24)(PORT = 1521))

    )

    (CONNECT_DATA =

      (SERVICE_NAME = iss)

    )

