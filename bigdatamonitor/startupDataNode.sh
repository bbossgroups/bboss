#!/bin/sh
java -Xms2048m -Xmx2048m -Xmn512m -XX:PermSize=256M -XX:MaxPermSize=512M -Djgroups.bind_addr=10.0.15.167 -Djava.net.preferIPv4Stack=true  -DadminNode=false -jar bboss-rt.jar

 
