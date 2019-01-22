@echo off

set XMOJO_HOME=..\..\..

call %XMOJO_HOME%\bin\setenv.bat

set CLASSPATH=classes;.;%CLASSPATH%

echo Starting the Dynamic MBean Example Agent

%JAVA_HOME%\bin\java examples.agent.RunDynamicAgent






