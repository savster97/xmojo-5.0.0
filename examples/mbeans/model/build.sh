export XMOJO_HOME=../../..

. $XMOJO_HOME/bin/setenv.sh

mkdir -p classes

echo Compilation started

$JAVA_HOME/bin/javac -d classes src/examples/applications/server/ServerInfo.java src/examples/mbeans/model/ModelServerInfo.java src/examples/agent/RunModelAgent.java

echo Compilation done

if [ ! -d "conf" ]
 then
       echo Copying conf directory
       cp -rf $XMOJO_HOME/conf .
fi
