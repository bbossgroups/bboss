#!/bin/sh

parse_jvm_options() {
  if [ -f "\$1" ]; then
    echo "`grep "^-" "\$1" | tr '\n' ' '`"
  fi
}

JVM_OPTIONS_FILE=jvm.options

RT_JAVA_OPTS="`parse_jvm_options "\$JVM_OPTIONS_FILE"` \$RT_JAVA_OPTS"
echo \$RT_JAVA_OPTS
nohup java \$RT_JAVA_OPTS -jar ${project}-boot-${bboss_version}.jar restart --shutdownLevel=9 > ${project}.log &
tail -f ${project}.log
