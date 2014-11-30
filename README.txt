# bboss group website:
http://www.bbossgroups.com

# bboss group project blog:
http://yin-bp.iteye.com/

# bboss group project source url:
https://github.com/bbossgroups/bbossgroups-3.5

# bboss eclipse工程目录一览表
##|--antbuildall    
包含bboss所有项目的ant构建指令，指令一栏表:
   ### build-all.bat
   jdk 1.5一次性打包所有工程指令，发布的jar和依赖资源文件存放在distrib目录下，会将各个模块依赖的jar包更新为新发布的jar文件

   ### build-all-jdk6.bat
   jdk 1.6一次性打包所有工程指令，发布的jar和依赖资源文件存放在distrib目录下，会将各个模块依赖的jar包更新为新发布的jar文件
   
   ### build-all-jdk7.bat
     jdk 1.7一次性打包所有工程指令，发布的jar和依赖资源文件存放在distrib目录下，会将各个模块引用jar包更新为新发布的jar文件
   
   ### cleanall.bat
    清空所有已经发布出来的jar包和资源文件，并且清空各个工程的classes目录
   
   ### build-cleanjars.bat
    清空所有已经发布出来的jar包和资源文件
   
   ### build-bboss-core-only.bat
    ioc工程打包指令，发布的jar和依赖资源文件存放在bboss-core/distrib目录下，其他模块引用的jar包全部更新为新发布的jar文件
   
   ### build-bboss-rpc-only.bat
    rpc工程打包指令，发布的jar和依赖资源文件存放在bboss-rpc/distrib目录下，其他模块引用的jar包全部更新为新发布的jar文件
   
   ### build-bboss-security-web.bat
    bboss会话共享监控服务和令牌服务工程，发布的jar和依赖资源文件存放在bboss-security-web/distrib目录下，其他模块引用的jar包全部更新为新发布的jar文件
   
   ### build-bboss-persistent.bat
    持久层jdk5打包指令，发布的jar和依赖资源文件存放在bboss-persistent/distrib目录下，其他模块引用的jar包全部更新为新发布的jar文件
   
   ### build-bboss-persistent-jdk6.bat
    持久层jdk6打包指令，发布的jar和依赖资源文件存放在bboss-persistent/distrib目录下，其他模块引用的jar包更新为新发布的jar文件
   
   ### build-bboss-persistent-jdk7.bat
    持久层jdk7打包指令，发布的jar和依赖资源文件存放在bboss-persistent/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-security.bat
    会话共享和令牌管理打包指令，发布的jar和依赖资源文件存放在bboss-security/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-schedule.bat
    quartz任务管理打包指令，发布的jar和依赖资源文件存放在bboss-schedule/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-event-only.bat
    分布式事件打包指令，发布的jar和依赖资源文件存放在bbossevent/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-hessian.bat
    hessian服务发布打包指令，发布的jar和依赖资源文件存放在bboss-hession/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-jodconverter-core.bat
    文档处理jodconverter插件打包指令，发布的jar和依赖资源文件存放在bboss-jodconverter-core/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-mvc-only-noresources.bat mvc工程打包指令，发布的jar和依赖资源文件存放在bboss-mvc/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-soa.bat
    序列化核心工程打包指令，发布的jar和依赖资源文件存放在bboss-soa/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-taglib.bat
    标签库打包指令，发布的jar和依赖资源文件存放在bboss-taglib/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-util.bat
    工具包打包指令，发布的jar和依赖资源文件存放在bboss-util/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-velocity.bat
    velocity引擎打包指令，发布的jar和依赖资源文件存放在bboss-velocity/distrib目录下，其他模块引用的jar包更新为新发布的jar文件

   ### build-bboss-wordpdf.bat
    文档处理jodconverter插件web工程打包指令，发布的jar和依赖资源文件存放在bboss-plugin-wordpdf/distrib目录下，其他模块引用的jar包更新为新发布的jar文件                  
                  
## |--apache-ant-1.7.1  

bboss ant构建时依赖的ant工具包，所有的工程ant构建都依赖这个ant环境，无需安装额外的ant

## |--bboss-core   

bboss ioc、序列化核心工程,独立ant构建指令：bboss-core/run.bat,发布的jar和依赖资源文件存放在bboss-core/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bbossevent  

bboss分布式事件工程,独立ant构建指令：bbossevent/run.bat,发布的jar和依赖资源文件存放在bbossevent/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包
## |--bboss-hession bboss hessian服务发布工程,独立ant构建指令：bboss-hession/build.bat,发布的jar和依赖资源文件存放在bboss-hession/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-jodconverter-core 

bbossword文档处理jodconverter插件工程,独立ant构建指令：bboss-jodconverter-core/run.bat,发布的jar和依赖资源文件存放在bboss-jodconverter-core/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-mvc  

bboss mvc工程,独立ant构建指令：bboss-mvc/build.bat,发布的jar和依赖资源文件存放在bboss-mvc/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-persistent

 bboss持久层框架工程,独立ant构建指令：bboss-persistent/run-jdk7.bat,发布的jar和依赖资源文件存放在bboss-persistent/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-plugin-hibernate 

bboss hibernate事务托管工程

## |--bboss-plugin-wordpdf 

bbossword文档处理jodconverter插件web工程,独立ant构建指令：bboss-plugin-wordpdf/run.bat,发布的jar和依赖资源文件存放在bboss-plugin-wordpdf/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-rpc 

 bboss rpc框架工程,独立ant构建指令：bboss-rpc/build.bat,发布的jar和依赖资源文件存放在bboss-rpc/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-schedule 

 bboss quartz任务管理工程,独立ant构建指令：bboss-schedule/run.bat,发布的jar和依赖资源文件存放在bboss-schedule/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-security

  bboss会话共享和令牌管理核心工程,独立ant构建指令：bboss-security/run.bat,发布的jar和依赖资源文件存放在bboss-security/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-security-web 

bboss会话共享监控服务和令牌服务工程,独立ant构建指令：bboss-security-web/build.bat,发布的jar和依赖资源文件存放在bboss-security-web/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-soa

  bboss序列化核心工程,独立ant构建指令：bboss-soa/build.bat,发布的jar和依赖资源文件存放在bboss-soa/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-taglib

 bboss标签库工程,独立ant构建指令：bboss-taglib/run.bat,发布的jar和依赖资源文件存放在bboss-taglib/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-util  

bboss工具包工程,独立ant构建指令：bboss-util/run.bat,发布的jar和依赖资源文件存放在bboss-util/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bboss-velocity

  bboss版velocity引擎（针对持久层）,独立ant构建指令：bboss-velocity/run.bat,发布的jar和依赖资源文件存放在bboss-velocity/distrib目录下,构建完毕后不会同步更新其他工程下的依赖包

## |--bestpractice

  包含了bboss各模块最佳实践和最小依赖包demo工程，demo具体说明请参考文档：http://yin-bp.iteye.com/blog/2122876

## |--database   

持久层demo依赖的derby文件数据库存放目录

## |--文档
存放bboss的文档

ant构建补充说明：bboss提供了统一的ant构建指令集，执行这些构建指令时会将产生的jar包同步更新到其他相关的工程中；同时在每个工程目录下都提供了单独的ant构建指令，如果只需构建对应的工程模块同时不需要将构建的jar包分发到其他相关工程中，
则可以执行工程下的ant构建指令。

## bboss项目下载地址，参考文档：http://yin-bp.iteye.com/blog/1080824