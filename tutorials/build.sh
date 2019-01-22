XMOJO_HOME=..

export XMOJO_HOME

. $XMOJO_HOME/bin/setenv.sh

mkdir -p classes

echo Starting compilation

$JAVA_HOME/bin/javac -d classes src/tutorials/mbeans/dynamic/*.java src/tutorials/mbeans/standard/*.java src/tutorials/application/shoppingcart/*.java src/tutorials/agent/*.java

echo Compilation finished

if [ ! -d "conf" ]
 then
       echo Copying conf directory
       cp -rf $XMOJO_HOME/conf .
fi