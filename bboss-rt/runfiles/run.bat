@echo off
if ""%1""==""start"" goto start
if ""%1""==""stop"" goto stop
if ""%1""==""restart"" goto restart
if ""%1""=="""" goto info
:info

echo Usage:
echo   ${project} keyword
echo   ----------------------------------------------------------------
echo   start                            -- Start ${project}
echo   stop                             -- stop ${project}
echo   restart                          -- Restart ${project}
echo   ----------------------------------------------------------------

goto end
:start
    java ${vm} -jar ${project}-${bboss_version}.jar
goto end
:stop
    java -jar ${project}-${bboss_version}.jar stop
goto end
:restart
    java ${vm} -jar ${project}-${bboss_version}.jar restart
goto end
:end
echo end