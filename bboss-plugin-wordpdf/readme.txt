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
默认安装目录
/usr/local/share/swftools
http://www.foolabs.com/xpdf/download.html
zlib 安装
jpeg.c:462: error: conflicting types for ‘jpeg_load_from_mem’

make[2]: g++: Command not found
http://www.rpmfind.net/linux/rpm2html/search.php?query=gcc-c%2B%2B
安装：gcc-c++-4.4.6-4.el6.i686.rpm libstdc++-devel-4.4.6-4.el6.i686.rpm

s -Wformat -O -fomit-frame-pointer  modules/swfaction.c -o modules/swfaction.o
In file included from modules/.././bitio.h:23,
                 from modules/../rfxswf.h:37,
                 from modules/swfaction.c:24:
modules/.././types.h:39:2: error: #error "no way to define 64 bit integer"
modules/.././types.h:42:2: error: #error "don't know how to define 32 bit integer"
modules/.././types.h:45:2: error: #error "don't know how to define 16 bit integer"
modules/.././types.h:48:2: error: #error "don't know how to define 8 bit integer"
make[1]: *** [modules/swfaction.o] Error 1
make[1]: Leaving directory `/opt/openoffice/tools/swftools-0.9.1/lib'
make: *** [all] Error 2

rm -f config.cache
 LDFLAGS="-L/usr/local/lib" CPPFLAGS="-I/usr/local/include" ./configure

Furthermore, a new installation of jpeglib (the following assumes it's in /usr/local/lib) often requires doing a: 
 ranlib /usr/local/lib/libjpeg.a
 ldconfig /usr/local/lib



   编译解压并进入下载的swftools目录运行
./configure（确保没有依赖软件包的错误）
make
make install
     可能出现的异常
修改一个源文件错误
这个时候，遇到报错
jpeg.c:463: error: conflicting types for ‘jpeg_load_from_mem’
jpeg.h:15: error: previous declaration of ‘jpeg_load_from_mem’ was here
make[1]: *** [jpeg.o] Error 1
原来是函数的定义和头文件的声明有点冲突，解决方式比较简单，修改 swftools-0.9.1\swftools-0.9.1\lib\jpeg.c 的 463行：
改为：
int jpeg_load_from_mem(unsigned char*_data, int size, unsigned char**dest, int*width, int*height)
继续即可，

基于giflib 5.0.3安装swftools报以下错误
pe -Wno-write-strings -Wformat -O -fomit-frame-pointer  gif2swf.c -o gif2swf.o
gif2swf.c: In function ‘MovieAddFrame’:
gif2swf.c:233: error: too few arguments to function ‘DGifOpenFileName’
gif2swf.c:239: warning: implicit declaration of function ‘PrintGifError’
gif2swf.c: In function ‘CheckInputFile’:
gif2swf.c:491: error: too few arguments to function ‘DGifOpenFileName’
make[1]: *** [gif2swf.o] Error 1
make[1]: Leaving directory `/opt/openoffice/tools/swftools-0.9.1/src'
make: *** [all] Error 2

卸载giflib 5.0.3 安装giflib-4.1.6.tar.gz问题解决

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
./soffice -accept="socket,host=localhost,port=2002;urp;StarOffice.Service.Manager" 
卸载OpenOffice
yum remove openoffice.org-core 即可完全卸载 OpenOffice.org

安装OpenOffice from source：
安装dmake
安装ant
http://kobyla.info/distfiles/ archive zip
http://kobyla.info/distfiles/ zlib


wget http://www.cpan.org/modules/by-module/Compress/Compress-Raw-Zlib-2.040.tar.gz
tar xvzf Compress-Raw-Zlib-2.040.tar.gz
cd Compress-Raw-Zlib-2.040
perl Makefile.PL
make
make install

Archive::Zip
perl Makefile.PL &&
make &&
make test
make install


configure: error: cups/cups.h could not be found. libcupsys2-dev or cups???-devel missing?
tar jxvf cups-1.6.1-source.tar.bz2  

 CFLAGS "-I/usr/local/cups-1.6.1"
	 CPPFLAGS "-I/usr/local/cups-1.6.1"
	 CXXFLAGS "-I/usr/local/cups-1.6.1"
	 DSOFLAGS "-L/usr/local/cups-1.6.1"
	 LDFLAGS "-L/usr/local/cups-1.6.1"
 ./configure --prefix=/usr/local/cups-1.6.1
make
make install

then openoffice configure is :
./configure --with-use-shell=bash --prefix=/usr/local/cups-1.6.1
./configure --disable-odk --disable-binfilter --includedir=/usr/local/cups-1.6.1/include/cups/
./configure --disable-odk --disable-binfilter --disable-cups

checking for gperf... no
checking for gperf... no
configure: error: gperf not found but needed. Install it and/or specify --with-gperf=/path/to/it.

./configure --prefix=/usr &&
make
make install 

then openoffice configure is:
先拷贝junit-4.jar到/usr/share/java/下面，然后执行下述指令即可
./configure --disable-odk --disable-binfilter --disable-cups --with-junit=/usr/share/java/junit-4.jar

执行成功后在/opt/openoffice/source/aoo-3.4.1/main下执行：
./bootstrap 

报错：
[root@localhost main]# ./bootstrap 
Can't locate Digest/SHA.pm in @INC (@INC contains: /usr/lib64/perl5/site_perl/5.8.8/x86_64-linux-thread-multi /usr/lib64/perl5/site_perl/5.8.7/x86_64-linux-thread-multi /usr/lib64/perl5/site_perl/5.8.6/x86_64-linux-thread-multi /usr/lib64/perl5/site_perl/5.8.5/x86_64-linux-thread-multi /usr/lib/perl5/site_perl/5.8.8 /usr/lib/perl5/site_perl/5.8.7 /usr/lib/perl5/site_perl/5.8.6 /usr/lib/perl5/site_perl/5.8.5 /usr/lib/perl5/site_perl /usr/lib64/perl5/vendor_perl/5.8.8/x86_64-linux-thread-multi /usr/lib64/perl5/vendor_perl/5.8.7/x86_64-linux-thread-multi /usr/lib64/perl5/vendor_perl/5.8.6/x86_64-linux-thread-multi /usr/lib64/perl5/vendor_perl/5.8.5/x86_64-linux-thread-multi /usr/lib/perl5/vendor_perl/5.8.8 /usr/lib/perl5/vendor_perl/5.8.7 /usr/lib/perl5/vendor_perl/5.8.6 /usr/lib/perl5/vendor_perl/5.8.5 /usr/lib/perl5/vendor_perl /usr/lib64/perl5/5.8.8/x86_64-linux-thread-multi /usr/lib/perl5/5.8.8 .) at /opt/openoffice/source/aoo-3.4.1/main/solenv/bin/download_external_dependencies.pl line 66.
BEGIN failed--compilation aborted at /opt/openoffice/source/aoo-3.4.1/main/solenv/bin/download_external_dependencies.pl line 66.

dmake is located in search path

bundling of dictionaries is disabled.

source LinuxX86-64Env.Set.sh

在服务器JVM启动参数中加入如下命令
-Djava.library.path=‘OpenOffice安装路径’/URE/bin

windows:
	将sigar-x86-winnt.dll放入‘OpenOffice安装路径’/URE/bin/下
linux:
	将libsigar-x86-linux.so放入‘OpenOffice安装路径’/URE/bin/下

注：linux下OpenOffice安装路径可能为2个，请注意此次使用的路径需要下有/ure/bin文件夹



vmware ip配置

Ethernet adapter VMware Network Adapter VMnet8:

        Connection-specific DNS Suffix  . :
        IP Address. . . . . . . . . . . . : 192.168.172.1
        Subnet Mask . . . . . . . . . . . : 255.255.255.0
        Default Gateway . . . . . . . . . :
