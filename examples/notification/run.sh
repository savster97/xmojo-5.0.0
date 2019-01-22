XMOJO_HOME=../..

export XMOJO_HOME

. $XMOJO_HOME/bin/setenv.sh

CLASSPATH=classes:$CLASSPATH

export CLASSPATH

echo Starting the Notification Example Agent

$JAVA_HOME/bin/java examples.agent.RunNotifAgent
