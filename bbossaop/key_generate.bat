@echo off

set capath=D:\workspace\bbossgroup-2.0-RC2\bbossaop\resources\keystore
set brokerpwd=123456
set clientpwd=123456

"%JAVA_HOME%\bin\keytool.exe" -genkey -alias server -keyalg RSA -keystore %capath%\server.ks -dname "CN=172.16.7.108, OU=yuhuaqu, O=creator, L=changsha, ST=hunan, C=CN" -keypass %brokerpwd% -storepass %brokerpwd% -validity 1800

"%JAVA_HOME%\bin\keytool.exe" -export -alias server -keystore %capath%\server.ks -file %capath%\server_cert -storepass %brokerpwd%

"%JAVA_HOME%\bin\keytool.exe" -genkey -alias client -keyalg RSA -keystore %capath%\client.ks -dname "CN=172.16.7.108, OU=yuhuaqu,O=creator, L=changsha, ST=hunan, C=CN" -keypass %clientpwd% -storepass %clientpwd% -validity 1800

"%JAVA_HOME%\bin\keytool.exe" -export -alias client -keystore %capath%\client.ks -file %capath%\client_cert -storepass %clientpwd%

"%JAVA_HOME%\bin\keytool.exe" -import -alias server -keystore %capath%\client.ts -file %capath%\server_cert -storepass %clientpwd%

"%JAVA_HOME%\bin\keytool.exe" -import -alias client -keystore %capath%\server.ts -file %capath%\client_cert -storepass %brokerpwd%

keytool -printcert -file D:\workspace\bbossgroup-2.0-RC2\bbossaop\resources\keystore\client_cert 