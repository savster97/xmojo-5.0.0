@rem The XMOJO Project
@rem Please set your Java path here.set

@if "%JAVA_HOME%" == "" set JAVA_HOME=c:\jdk1.3

@echo off

cd ..

@if NOT EXIST %JAVA_HOME%\bin\java.exe echo "Please set the JAVA_HOME parameter in" %0
@if NOT EXIST %JAVA_HOME%\bin\java.exe goto FINISH

@echo on

set CLASSPATH=.\lib\AdventNetUpdateManager.jar;.\lib\crimson.jar;.\lib\jaxp.jar;.\lib\xalan.jar;

set JAVA_COMPILER=NONE

%JAVA_HOME%\bin\java -Xmx100m -Dtools.discSpaceCheck=false com.adventnet.tools.update.installer.UpdateManager -u conf %*

cd bin

