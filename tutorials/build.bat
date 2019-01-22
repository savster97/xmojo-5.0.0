@echo off

set XMOJO_HOME=..

call %XMOJO_HOME%\bin\setenv.bat

if exist "classes\" goto Compile

mkdir classes

:Compile

echo Starting compilation

%JAVA_HOME%\bin\javac -d classes src\tutorials\mbeans\dynamic\*.java src\tutorials\mbeans\standard\*.java src\tutorials\application\shoppingcart\*.java src\tutorials\agent\*.java

echo Compilation finished

if exist "conf\" goto DONE

REM ----------------------------------------------
REM Copying the conf directory, that will be
REM required for initializing the Jetty Web Server
REM ----------------------------------------------

echo Copying conf directory

mkdir conf

XCOPY /S %XMOJO_HOME%\conf conf\

:DONE




