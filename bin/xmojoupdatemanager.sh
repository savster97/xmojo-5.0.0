#!/bin/sh
# Searching for java in your system.

cd ..

PRODUCT_HOME=.
export PRODUCT_HOME

JAVA_EXE=`which java`
if [ $? -ne 0 ]; then
	echo java command not found
	exit 1
fi

JAVA_HOME_DIR=`dirname $JAVA_EXE`

JAVA_HOME=`dirname $JAVA_HOME_DIR`

JAVA_COMPILER=NONE
export JAVA_HOME JAVA_COMPILER
CLASSPATH=./lib/AdventNetUpdateManager.jar:./lib/crimson.jar:./lib/jaxp.jar:./lib/xalan.jar
export CLASSPATH

$JAVA_HOME/bin/java -Xmx100m -Dtools.discSpaceCheck=false com.adventnet.tools.update.installer.UpdateManager -u conf $*
cd bin

