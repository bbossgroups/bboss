#!/bin/sh
nohup java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar > trace-hdfs.log &
