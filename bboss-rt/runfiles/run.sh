#!/bin/sh

case \$1 in
   start)
    nohup java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar > ${cmd}.log &
    tail -f ${cmd}.log
     ;;
   stop)
    java -jar bboss-rt-${bboss_version}.jar  stop
     ;;
   restart)
    nohup java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar restart > ${cmd}.log &
    tail -f ${cmd}.log
    ;;
   *)
     echo
     echo "Usage:";
     echo "  ${cmd} keyword [value1 [value2]] ";
     echo "  ----------------------------------------------------------------";
     echo "  start                             -- Start ${cmd}";
     echo "  stop                              -- stop ${cmd}";
     echo "  restart                           -- Restart ${cmd}";
     echo "  ----------------------------------------------------------------";
     echo
     ;;
esac
