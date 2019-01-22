@echo off
call .\setenv.bat

cd ..

%JAVA_HOME%\bin\java -mx200M com.adventnet.agent.remotebrowser.RemoteBrowser

cd bin
