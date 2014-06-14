release version : bbossgroups-3.9.1
release date: 2014/06/14
新增功能：
1.会话共享框架
2.增加序列化插件机制
改进功能：
1.令牌模块功能扩展，增加临时认证令牌和持久化认证令牌，以及ticket机制（用于sso），解决集群环境下token校验问题
2.标签库功能改进：性能提升，增加size标签
3.FileBlob增加流下载功能
修复一系列bug.


release version : bbossgroups-3.7.6
release date: 2014/01/08

bboss group website:
http://www.bbossgroups.com

bboss group project blog:
http://yin-bp.iteye.com/


bboss group project github source url:
https://github.com/bbossgroups/bbossgroups-3.5
bbossgroups-3.7.6 release futures
1.优化资源管理，应用卸载主动释放资源
2.mvc添加xml插件
3.标签库修复cms分页模板发布分页导航不起作用问题
4.修复数据库连接池配置账号加密时导致工作流引擎activiti无法正常启动的bug

bbossgroups-3.7.3 release futures


1.offset重新设置后，statement中位置没有正确处理
2.增加一组more接口，相应的分页标签逻辑也要处理
3.识别最后一页，more查询漏洞
4.tagnumer ，more查询漏洞
5.去掉持久层与soa的依赖关系
6.去掉aop对持久层的依赖关系
7.解除bboss对cms.jar依赖关系
8.将平台中的htmlparser-1.5-20050925.jar及相关源码放到taglib工程中
9.框架eclipse工程全部采用UTF-8编码
