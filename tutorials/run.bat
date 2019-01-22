@echo off

set XMOJO_HOME=..

call %XMOJO_HOME%\bin\setenv.bat

set CLASSPATH=classes;.;%CLASSPATH%

echo Starting the Shopping Cart Example Agent

%JAVA_HOME%\bin\java tutorials.agent.RunAgent






