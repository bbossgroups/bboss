title Data Node
java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=256M -XX:MaxPermSize=256M -Djava.net.preferIPv4Stack=true  -DadminNode=false -jar bboss-rt.jar 