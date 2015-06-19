#!/bin/sh
java -Xms1024m -Xmx1024m -Xmn512m -XX:PermSize=256M -XX:MaxPermSize=512M -Djava.net.preferIPv4Stack=true  -DadminNode=false -jar bboss-rt.jar 
