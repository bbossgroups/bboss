#!/bin/sh
cd `dirname $0`
java -jar ${project}-boot-${bboss_version}.jar stop --shutdownLevel=9
