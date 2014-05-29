#proxy command  
#ver:0.1bate 
#20111129 by dongnan 
 
#/usr/local/haproxy/sbin/haproxy  
#HA-Proxy version 1.4.18 2011/09/16 
#Copyright 2000-2011 Willy Tarreau <w@1wt.eu> 
# 
#Usage : haproxy [-f <cfgfile>]* [ -vdVD ] [ -n <maxconn> ] [ -N <maxpconn> ] 
#        [ -p <pidfile> ] [ -m <max megs> ] 
#        -v displays version ; -vv shows known build options. 
#        -d enters debug mode ; -db only disables background mode. 
#        -V enters verbose mode (disables quiet mode) 
#        -D goes daemon 
#        -q quiet mode : don't display messages 
#        -c check mode : only check config files and exit 
#        -n sets the maximum total # of connections (2000) 
#        -m limits the usable amount of memory (in MB) 
#        -N sets the default, per-proxy maximum # of connections (2000) 
#        -p writes pids of all children to this file 
#        -de disables epoll() usage even when available 
#        -ds disables speculative epoll() usage even when available 
#        -dp disables poll() usage even when available 
#        -sf/-st [pid ]* finishes/terminates old pids. Must be last arguments. 
 
#variables 
haproxy_dir=/usr/local/haproxy/ 
haproxy_conf=${haproxy_dir}haproxy.cfg 
haproxy_pid=${haproxy_dir}logs/haproxy.pid 
haproxy_cmd=${haproxy_dir}sbin/haproxy 
#test variables 
#file $haproxy_dir; file $haproxy_conf; file $haproxy_cmd; file $haproxy_pid 
 
 
 
if [ "$#" -eq "0" ];then 
    echo "usage: $0 {start|stop|restart}" 
    exit 1 
fi 
 
if [ "$1" = "start" ];then 
#echo $1 
    $haproxy_cmd -f $haproxy_conf 
elif [ "$1" = "stop" ];then 
#echo $1 
    kill 'cat $haproxy_pid' 
elif [ "$1" = "restart" ];then 
#echo $1 
    $haproxy_cmd -f $haproxy_conf -st 'cat $haproxy_pid' 
 
else 
   echo "usage: $0 arguments only start and stop or restart !" 
fi 
