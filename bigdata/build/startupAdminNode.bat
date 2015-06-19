title AdminNode
java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=256M -XX:MaxPermSize=256M -Djava.net.preferIPv4Stack=true  -Djgroups.bind_addr=10.25.192.142 -DadminNode=true -jar bboss-rt.jar  %1