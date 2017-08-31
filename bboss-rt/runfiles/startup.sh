#!/bin/sh
nohup java ${vm} -jar ${project}-${bboss_version}.jar > ${project}.log &
tail -f ${project}.log

