#!/bin/sh
#$Id: mbeanbrowser.sh,v 1.2 2003/06/05 17:08:30 jayavasanthan Exp $

. ./setenv.sh

cd ..

$JAVA_HOME/bin/java -mx200M -DJAVA_HOME=$JAVA_HOME com.adventnet.agent.remotebrowser.RemoteBrowser

cd  bin
