XMOJO_HOME=../../..

export XMOJO_HOME

. $XMOJO_HOME/bin/setenv.sh

CLASSPATH=classes:$CLASSPATH

export CLASSPATH

echo Starting the Standard MBean Example Agent

$JAVA_HOME/bin/java examples.agent.RunStandardAgent
