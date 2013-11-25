mvc实战策略：
1.将mvc工程导入eclipse
2.准备好tomcat 6和jdk 15或以上
3.修改i18n.xml中的WebRoot路径
用户可以根据自己的情况设置docBase属性的值
<?xml version="1.0" encoding="UTF-8"?>
<Context   path="/i18n"   docBase="D:\d\workspace\bbossgroups-3.6.0\bestpractice\i18n\WebRoot"   debug="0"/>
4.启动tomcat，输入以下地址即可访问mvc的数据绑定实例了：
http://localhost:8080/i18n/examples/i18n.jsp
