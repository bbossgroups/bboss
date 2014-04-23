bbossupload文件上传下载实战策略：
1.将bbossupload工程导入eclipse
2.修改/bbossupload/src/poolman.xml中derby数据库文件路径：
<url>jdbc:derby:D:/d/workspace/bbossgroups-3.6.0/bestpractice/bbossupload/database/cimdb</url>
其中的D:/d/workspace/bbossgroups-3.6.0/bestpractice/bbossupload/database/cimdb需要修改为你的工程所在的实际路径
 
2.准备好tomcat 6和jdk 15或以上
3.在tomcat 6的conf\Catalina\localhost下增加upload.xml文件，内容为(路径需要调整为实际路径)：
<?xml version='1.0' encoding='utf-8'?>
<Context docBase="D:\workspace\bbossgroups-3.5\bestpractice\bbossupload\WebRoot" path="/upload" debug="0" reloadable="false">
</Context>
用户可以根据自己的情况设置docBase属性的值

4.启动tomcat，输入以下地址即可访问mvc的附件上传下载实例了：
http://localhost:8080/upload
