#!/bin/sh

case \$1 in
   start)
    nohup java ${vm} -jar ${project}-${bboss_version}.jar > ${project}.log &
    tail -f ${project}.log
     ;;
   stop)
    java -jar ${project}-${bboss_version}.jar  stop
     ;;
   restart)
    nohup java ${vm} -jar ${project}-${bboss_version}.jar restart > ${project}.log &
    tail -f ${project}.log
    ;;
   *)
     echo
     echo "Usage:";
     echo "  ${project} keyword [value1 [value2]] ";
     echo "  ----------------------------------------------------------------";
     echo "  start                             -- Start ${project}";
     echo "  stop                              -- stop ${project}";
     echo "  restart                           -- Restart ${project}";
     echo "  ----------------------------------------------------------------";
     echo
     ;;
esac
