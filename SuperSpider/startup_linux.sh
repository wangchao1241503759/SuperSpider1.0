#!/bin/bash
. /etc/profile

min_heap_size="4000m"
max_heap_size="4000m"

#!/bin/sh

#加载环境变量
source /etc/profile
W_EXIT_STATUS=65
number_of_expected_args=1

# Define some constants
PROJECT_PATH=$PWD

LOG_PATH=$PROJECT_PATH/logs

mkdir $LOG_PATH


classpath=$PROJECT_PATH/WEB-INF/classes/:$PROJECT_PATH/WEB-INF/lib/*:$PROJECT_PATH/WEB-INF/

start()
{
  nohup java -Xms$min_heap_size -Xmx$max_heap_size -XX:PermSize=128m -Xloggc:$LOG_PATH/gc.log -XX:+PrintGCTimeStamps -XX:-PrintGCDetails -cp $classpath cn.com.infcn.startup.QuickStartServer > $LOG_PATH/superspider.log &
  echo $! >$LOG_PATH/superspider.pid
  echo $! >$LOG_PATH/app.pid
}
stop()
{
  kill  `cat $LOG_PATH/app.pid`
}


case $1 in
"restart")
   stop
   start
;;
"start")
   start
;;
"stop")
   stop
;;
*) echo "only accept params start|stop|restart" ;;
esac
