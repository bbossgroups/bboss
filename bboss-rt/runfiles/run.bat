@echo off
if ""%1""==""start"" goto start
if ""%1""==""stop"" goto stop
if ""%1""==""restart"" goto restart
if ""%1""=="""" goto info
:info

echo Usage:
echo   ${cmd} keyword
echo   ----------------------------------------------------------------
echo   start                            -- Start ${cmd}
echo   stop                         -- stop ${cmd}
echo   restart                          -- Restart ${cmd}
echo   ----------------------------------------------------------------

goto end
:start
    java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar
goto end
:stop
    java -jar bboss-rt-${bboss_version}.jar stop
goto end
:restart
    java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar restart
goto end
:end
echo end