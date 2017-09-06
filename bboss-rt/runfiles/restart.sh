#!/bin/sh
nohup java ${vm} -jar ${project}-${bboss_version}.jar restart --shutdownLevel=C > ${project}.log &
tail -f ${project}.log
