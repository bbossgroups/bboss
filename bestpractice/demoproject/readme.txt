demoproject实战策略：
1.将demoproject工程导入eclipse
2.准备好tomcat 6和jdk 15或以上
3.在tomcat 6的conf\Catalina\localhost下增加mvc.xml文件，内容为：
<?xml version='1.0' encoding='utf-8'?>
<Context docBase="D:\workspace\bbossgroups-3.5\bestpractice\demoproject\WebRoot" path="/demoproject" debug="0" reloadable="false">
</Context>
用户可以根据自己的情况设置docBase属性的值

4.启动tomcat，输入以下地址即可访问mvc的数据绑定实例了：
http://localhost:8080/demoproject/examples/index.page
