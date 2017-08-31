#!/bin/sh
nohup java ${vm} -jar ${project}-${bboss_version}.jar restart > ${project}.log &
tail -f ${project}.log
