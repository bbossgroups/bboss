准备了两个工程
session工程 ----如果只需要session共享功能，则整合这个工程中的配置文件和jar包即可
sessionmonitor工程----如果需要session共享以及监控功能，则整合这个工程中的配置文件和jar包即可

一、session工程使用指南
session工程包含session功能验证sessiontest.jsp文件，会话共享需要的所有配置文件和最小依赖jar包，
工程的结构说明：
/session/resources/org/frameworkset/soa/serialconf.xml --序列化插件配置文件，类别名配置
/session/resources/mongodb.xml   --mongodb配置文件
/session/resources/sessionconf.xml --session配置文件
/session/session.xml    --session工程web部署文件
/session/WebRoot/sessiontest.jsp  --session功能验证文件
/session/WebRoot/WEB-INF/web.xml   --配置session共享拦截过滤器
/session/WebRoot/WEB-INF/lib      --会话共享最小依赖jar文件存放目录
工程搭建和部署步骤如下：
1.下载工程并导入eclipse
2.参考以下文档配置/resources/mongodb.xml中的mongodb连接
http://yin-bp.iteye.com/blog/2064662
参考其中的章节【6.mongodb客户端配置 】
2.部署，将工程根目录下的部署文件session.xml拷贝到tomcat目录下：
F:\environment\apache-tomcat-7.0.30\conf\Catalina\localhost
根据实际情况修改session.xml中的应用路径docBase：
<?xml version="1.0" encoding="UTF-8"?>
<Context   path="/session"   docBase="F:\workspace\bbossgroups-3.5\bestpractice\session\WebRoot"   debug="0"/>
3.启动tomcat，
启动tomcat后，在浏览器中输入以下地址，验证session共享功能，同时在监控工程sessionmonitor的监控页面查看session的变化情况，
http://localhost:8080/session/sessiontest.jsp

二、sessionmonitor工程使用指南
sessionmonitor工程包含session功能验证jsp页面和会话共享监控功能，
工程的结构说明：
/sessionmonitor/resources/org/frameworkset/soa/serialconf.xml --序列化插件配置文件，类别名配置
/sessionmonitor/resources/mongodb.xml   --mongodb配置文件
/sessionmonitor/resources/sessionconf.xml --session配置文件
/sessionmonitor/sessionmonitor.xml    --sessionmonitor工程web部署文件
/sessionmonitor/WebRoot/sessiontest.jsp  --session功能验证文件
/sessionmonitor/WebRoot/WEB-INF/web.xml   --配置session共享拦截过滤器
/sessionmonitor/WebRoot/WEB-INF/lib      --会话共享及监控最小依赖jar文件存放目录
/sessionmonitor/WebRoot/session/sessionManager/sessionManager.jsp          --session监控jsp页面入口地址
/sessionmonitor/WebRoot/session/sessionManager/sessionList.jsp  --session监控session 列表页面
/sessionmonitor/WebRoot/session/sessionManager/viewSessionInfo.jsp  --sesion详细信息查看页面
工程搭建和部署步骤如下：
1.下载工程并导入eclipse
2.参考以下文档配置/resources/mongodb.xml中的mongodb连接
http://yin-bp.iteye.com/blog/2064662
参考其中的章节【6.mongodb客户端配置 】
2.部署，将工程根目录下的部署文件sessionmonitor.xml拷贝到tomcat目录下：
F:\environment\apache-tomcat-7.0.30\conf\Catalina\localhost
根据实际情况修改sessionmonitor.xml中的应用路径docBase：
<?xml version="1.0" encoding="UTF-8"?>
<Context   path="/sessionmonitor"   docBase="F:\workspace\bbossgroups-3.5\bestpractice\sessionmonitor\WebRoot"   debug="0"/>
3.启动tomcat，
启动tomcat后，在浏览器中访问以下地址，查看所有应用session数据
http://localhost:8080/sessionmonitor/session/sessionManager/sessionManager.jsp
在浏览器中输入以下地址，验证session共享功能，同时在监控页面查看session的变化情况，
http://localhost:8080/sessionmonitor/sessiontest.jsp