#!/bin/sh
#$Id: setenv.sh,v 1.6 2003/07/07 11:45:33 chandrars Exp $

if [ -z "${JAVA_HOME}" ]; then
   echo "JAVA_HOME is not set ..."
   exit 1
else 
	export JAVA_HOME
fi


if [ ! -e $XMOJO_HOME/bin/setenv.sh ]
 then
       XMOJO_HOME=.
fi

CLASSPATH=$XMOJO_HOME/conf:$XMOJO_HOME/lib/xmojoutils.jar:$XMOJO_HOME/lib/xmojo.jar:$XMOJO_HOME/lib/xmojoadaptors.jar:$XMOJO_HOME/lib/xmojotools.jar:$XMOJO_HOME/lib/org.mortbay.jetty.jar:$XMOJO_HOME/lib/crimson.jar:$XMOJO_HOME/lib/jaxp.jar:$XMOJO_HOME/lib/xalan.jar:$XMOJO_HOME/lib/javax.servlet.jar:

export CLASSPATH

JAVA_COMPILER=NONE
export JAVA_COMPILER

