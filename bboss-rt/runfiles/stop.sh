#!/bin/sh
cd `dirname \$0`
java -jar ${project}-${bboss_version}.jar stop --conf=resources/application.properties  --shutdownLevel=C
