#!/bin/bash
#
# /etc/init.d/aws-demo
#
# chkconfig: 2345 1 99
# description: aws demo service

# Source function library.
. /etc/init.d/functions

name=aws-demo
user=awsdemo
lockfile="/var/lock/subsys/${name}"
pidfile="/var/run/${name}"

start() {
    echo -n $"Starting $name: "
    daemon --user $user "/opt/aws-demo/scripts/start-aws-demo.sh &"
    retval=$?
    echo
    [ $retval -eq 0 ] && touch $lockfile
    pid=`ps -ef | grep $name | grep java | awk '{print $2}'`
    echo $pid > $pidfile
    return $retval
}

stop() {
    echo -n $"Stopping $name: "
    killproc -p $pidfile
    retval=$?
    echo
    [ $retval -eq 0 ] && rm -f $lockfile
    return $retval
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    *)
        echo "Usage: $name {start|stop|restart}"
        exit 1
        ;;
esac

exit $?