1.linux下安装openffice
参考文档
http://wiki.openoffice.org/w/images/7/7e/Installation_Guide_OOo3.pdf
默认安装目录：
/opt/openoffice.org3
2.linux下安装swftools
下载swftools 0.9.1版本
http://wiki.swftools.org/wiki/Main_Page#Installation_guides
http://wiki.swftools.org/wiki/Installation
在安装swftools 0.9.2时报错，转而安装swftools 0.9.1
默认安装目录/usr/local/bin
/usr/local/share/swftools

3.jdk安装
下载linux版本jdk 64位
安装
./jdk-6u31-linux-x64-rpm.bin 
配置环境变量
vi /etc/profile
JAVA_HOME=/opt/jdk1.5
PATH=$JAVA_HOME/bin:$PATH
CLASSPATH=.$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar export JAVA_HOME,PATH,CLASSPATH
让环境变量生效
source /etc/profile

在java中执行openoffice的启动操作时报以下错误：
WriterDoc: Start up or connect to the remote service manager.
CE> /opt/openoffice.org3/program/../basis-link/ure-link/bin/javaldx: /lib64/libc.so.6: version `GLIBC_2.7' not found (required by /opt/openoffice.org/ure/bin/../lib/libuno_sal.so.3)
CE> /opt/openoffice.org3/program/../basis-link/ure-link/bin/javaldx: /lib64/libc.so.6: version `GLIBC_2.7' not found (required by /opt/openoffice.org/ure/bin/../lib/libxml2.so.2)
CE> /opt/openoffice.org3/program/soffice.bin: /lib64/libc.so.6: version `GLIBC_2.7' not found (required by /opt/openoffice.org3/program/../basis-link/ure-link/lib/libuno_sal.so.3)
CE> /opt/openoffice.org3/program/soffice.bin: /lib64/libc.so.6: version `GLIBC_2.11' not found (required by /opt/openoffice.org3/program/../basis-link/program/libsvt.so)
CE> /opt/openoffice.org3/program/soffice.bin: /lib64/libc.so.6: version `GLIBC_2.7' not found (required by /opt/openoffice.org3/program/../basis-link/program/libsvt.so)
CE> /opt/openoffice.org3/program/soffice.bin: /lib64/libc.so.6: version `GLIBC_2.11' not found (required by /opt/openoffice.org3/program/../basis-link/program/libvcl.so)
CE> /opt/openoffice.org3/program/soffice.bin: /lib64/libc.so.6: version `GLIBC_2.7' not found (required by /opt/openoffice.org3/program/../basis-link/program/../ure-link/lib/libxml2.so.2)


