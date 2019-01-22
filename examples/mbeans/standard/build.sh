XMOJO_HOME=../../..

export XMOJO_HOME

. $XMOJO_HOME/bin/setenv.sh

mkdir -p classes

$JAVA_HOME/bin/javac -d classes src/examples/mbeans/standard/*.java src/examples/agent/RunStandardAgent.java

if [ ! -d "conf" ]
 then
       echo Copying conf directory
       cp -rf $XMOJO_HOME/conf .
fi



   


