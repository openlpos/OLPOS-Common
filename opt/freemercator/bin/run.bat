@echo off
rem basic script to launch the GPOS app.  change the share location if you want to point to a different directory

for %%i in (..\thirdparty\*.jar) do call lcp.bat %%i

java -Dlog4j.configuration="..\etc\log4j.properties" -DSHARE="..\share" -DSCRIPTS="../scripts" -classpath "../lib/mercator.jar;%LOCALCLASSPATH%;../etc/"  com.globalretailtech.pos.apps.GPOS %1 %2 %3 %4 %5 %6 %7 %8 %9
