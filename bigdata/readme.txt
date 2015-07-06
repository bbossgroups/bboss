hx2015is_static_often没有完成的任务块数：
7,58,89

hx20131211没有完成的任务块数：
8,76,80 106 166 192 208 210 225 231 256  266 267 297 355 356 357 


327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,354,358,359,360,361,362,363,364,365,366,287,288,289,290,291,292,293,294,295,296,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,0,1,2,3,4,5,6,7,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,447,448,449,450,451,452,453,454,455,456,457,458,459,460,461,462,463,464,465,466,467,468,469,470,471,472,473,474,475,477,478,479,480,481,482,483,484,485,486,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,246,247,248,249,250,251,252,253,254,255,257,258,259,260,261,262,263,264,265,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,407,408,409,410,411,412,413,414,415,417,418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,434,435,436,437,438,441,442,443,444,445,446,205,206,207,209,211,212,213,214,215,216,217,218,219,220,221,222,223,224,226,227,228,229,230,232,233,234,235,236,237,238,239,240,241,242,243,244,245,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,77,78,79,81,164,165,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,193,194,195,196,197,198,199,200,201,202,203,204,367,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,386,387,388,389,390,391,392,394,395,396,397,398,399,400,401,402,403,404,405,406




Ip:10.11.16.243

账号:10.11.16.243\fadmin

密码（不要手输，要复制）:apDPusr/31ltdxw



 GRANT CONNECT TO HX
 GRANT RESOURCE TO HX
 
  GRANT DBA TO HX
  
CREATE INDEX IND_static_ST_PID ON iss.is_static_often_20131211
(ST_PID);

CREATE INDEX IND_SELDOM3_ST_PID ON iss.is_static_seldom_20131211
(ST_PID);


hx20120401_20120901有以下8个任务无法执行，需要重新执行：
第一次
is_static_78
is_static_73
is_static_55

is_static_14982
is_static_14983 
is_static_14993
is_static_14995
is_static_14998

第二次
is_static_73_9
is_static_14993_5
is_static_14993_6
is_static_14993_8
is_static_14995_9
is_static_14998_2
is_static_14998_6 


select value from v$parameter where name = 'sessions'; 

hx20111212_20120331排除
is_static_0
is_static_1 is_static_4 is_static_5 is_static_6 is_static_7 is_static_8 is_static_9
is_static_10
is_static_11
is_static_12 is_static_13 is_static_14 is_static_15 is_static_16 is_static_17 is_static_18 is_static_19 
is_static_20 is_static_21 is_static_22 is_static_23 is_static_24 is_static_25 is_static_26 is_static_27 is_static_28 is_static_29
is_static_3 is_static_30 is_static_31 is_static_33 is_static_34	
is_static_9993 is_static_9994 is_static_9995 is_static_9996 is_static_9997 is_static_9998 is_static_9999

1.发容器包
2.动态加载数据源

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

iss.is_static_seldom_20131211(未处理)

通过主键关联，如下语句关联所有查询慢。您着情考虑

select t.st_loginid,t.st_ma_serialno,t.st_state,t.st_logintime,t.st_connecttime,t.st_updatetime,t.st_totalwktime,t.st_rmntime,t.st_berrorcode,t.st_werrorcode,t.st_balmcode,t.st_walmcode,t.st_longitude,t.st_latitude,t.st_saticunt,t.st_steppos,t.st_engv,t.st_oillev,t.st_batteryvol from iss.is_static_often_20131211 t.ST_CHECKMARK='20130807000128'
select a.st_wktime,a.st_gpssta,a.st_velocity,a.st_orientation,a.st_sgnlq,a.st_errdealsta,a.st_cmmctsch,a.st_altitude,a.st_uintreserv10,a.st_uintreserv11,a.st_uintreserv12,a.st_uintreserv13,a.st_uintreserv14,a.st_uintreserv15,a.st_uintreserv16,a.st_uintreserv17,a.st_uintreserv18,a.st_uintreserv19,a.st_uintreserv20,a.st_uintreserv21,a.st_uintreserv22,a.st_uintreserv23,a.st_uintreserv24,a.st_uintreserv25,a.st_uintreserv26,a.st_uintreserv27,a.st_uintreserv28,a.st_uintreserv29,a.st_uintreserv30,a.st_uintreserv31,a.st_uintreserv32,a.st_floatreserv13,a.st_floatreserv14,a.st_floatreserv15,a.st_floatreserv16,a.st_floatreserv17,a.st_floatreserv18,a.st_floatreserv19,a.st_floatreserv20,a.st_floatreserv21,a.st_floatreserv22,a.st_floatreserv23,a.st_floatreserv24,a.st_floatreserv25,a.st_floatreserv26,a.st_floatreserv27,a.st_floatreserv28,a.st_floatreserv29,a.st_floatreserv30,a.st_floatreserv31,a.st_floatreserv32 from iss.is_static_seldom_20131211  a where  t.st_pid = a.st_pid

select t.st_loginid,t.st_ma_serialno,t.st_state,t.st_logintime,t.st_connecttime,t.st_updatetime,t.st_totalwktime,t.st_rmntime,t.st_berrorcode,t.st_werrorcode,t.st_balmcode,t.st_walmcode,t.st_longitude,t.st_latitude,t.st_saticunt,t.st_steppos,t.st_engv,t.st_oillev,t.st_batteryvol,a.st_wktime,a.st_gpssta,a.st_velocity,a.st_orientation,a.st_sgnlq,a.st_errdealsta,a.st_cmmctsch,a.st_altitude,a.st_uintreserv10,a.st_uintreserv11,a.st_uintreserv12,a.st_uintreserv13,a.st_uintreserv14,a.st_uintreserv15,a.st_uintreserv16,a.st_uintreserv17,a.st_uintreserv18,a.st_uintreserv19,a.st_uintreserv20,a.st_uintreserv21,a.st_uintreserv22,a.st_uintreserv23,a.st_uintreserv24,a.st_uintreserv25,a.st_uintreserv26,a.st_uintreserv27,a.st_uintreserv28,a.st_uintreserv29,a.st_uintreserv30,a.st_uintreserv31,a.st_uintreserv32,a.st_floatreserv13,a.st_floatreserv14,a.st_floatreserv15,a.st_floatreserv16,a.st_floatreserv17,a.st_floatreserv18,a.st_floatreserv19,a.st_floatreserv20,a.st_floatreserv21,a.st_floatreserv22,a.st_floatreserv23,a.st_floatreserv24,a.st_floatreserv25,a.st_floatreserv26,a.st_floatreserv27,a.st_floatreserv28,a.st_floatreserv29,a.st_floatreserv30,a.st_floatreserv31,a.st_floatreserv32 from iss.is_static_often_20131211  PARTITION  (IS_STATIC_OFTEN_20131206) t,iss.is_static_seldom_20131211  a where t.ST_CHECKMARK = a.ST_CHECKMARK   

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

