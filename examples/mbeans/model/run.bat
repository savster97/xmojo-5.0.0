@echo off

set XMOJO_HOME=..\..\..

call %XMOJO_HOME%\bin\setenv.bat

set CLASSPATH=classes;.;%CLASSPATH%

echo Starting the Model MBean Example Agent

%JAVA_HOME%\bin\java examples.agent.RunModelAgent %1






