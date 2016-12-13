@echo off
title=≈¿≥Ê
set "CURRENT_DIR=%~dp0%"
cd %CURRENT_DIR%

set P_ROOT=%CURRENT_DIR%

set CLASSPATH=%P_ROOT%\WEB-INF\classes\;%P_ROOT%\WEB-INF\lib\*;%P_ROOT%\WEB-INF\


java -Xms1024m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=128m -cp %CLASSPATH% cn.com.infcn.startup.QuickStartServer
pause