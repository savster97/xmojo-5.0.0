rem set JAVA_HOME=

if exist "%JAVA_HOME%\bin\java.exe"  goto JavaHome

if "%JAVA_HOME%"=="" goto noJavaHome

:noJavaHome

echo."
echo    Warning : Java_Home environment variable is not set.
echo              Please set the JAVA_HOME environment variable
echo              in setenv.bat file under bin directory.
echo."

pause

goto DONE

:JavaHome

rem ------------------------------------------
echo Setting the CLASSPATH done.
rem ------------------------------------------

if exist "%XMOJO_HOME%\bin\setenv.bat"  goto CPATH

set XMOJO_HOME=.

:CPATH

set CLASSPATH=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\dt.jar;%XMOJO_HOME%\conf;%XMOJO_HOME%\lib\xmojoutils.jar;%XMOJO_HOME%\lib\xmojo.jar;%XMOJO_HOME%\lib\xmojoadaptors.jar;%XMOJO_HOME%\lib\xmojotools.jar;%XMOJO_HOME%\lib\org.mortbay.jetty.jar;%XMOJO_HOME%\lib\crimson.jar;%XMOJO_HOME%\lib\jaxp.jar;%XMOJO_HOME%\lib\xalan.jar;%XMOJO_HOME%\lib\javax.servlet.jar;

set JAVA_COMPILER=NONE

:DONE
