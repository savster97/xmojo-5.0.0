@echo off

set XMOJO_HOME=..

call %XMOJO_HOME%\bin\setenv.bat

set CLASSPATH=classes;.;%CLASSPATH%

echo Starting the Shopping Cart Application

%JAVA_HOME%\bin\java tutorials.application.shoppingcart.ShoppingCartApplication






