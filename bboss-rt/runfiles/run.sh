#!/bin/sh
cd `dirname \$0`

parse_jvm_options() {
  if [ -f "\$1" ]; then
    echo "`grep "^-" "\$1" | tr '\n' ' '`"
  fi
}

JVM_OPTIONS_FILE=jvm.options

RT_JAVA_OPTS="`parse_jvm_options "\$JVM_OPTIONS_FILE"` \$RT_JAVA_OPTS"
echo \$RT_JAVA_OPTS
case \$1 in
   start)
    nohup java \$RT_JAVA_OPTS -jar ${project}-${bboss_version}.jar --conf=resources/application.properties > ${project}.log &
    tail -f ${project}.log
     ;;
   stop)
    java -jar ${project}-${bboss_version}.jar  stop --shutdownLevel=9 --conf=resources/application.properties
     ;;
   restart)
    nohup java \$RT_JAVA_OPTS -jar ${project}-${bboss_version}.jar restart --shutdownLevel=9 --conf=resources/application.properties > ${project}.log &
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
