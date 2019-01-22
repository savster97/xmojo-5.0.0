XMOJO_HOME=..

export XMOJO_HOME

. $XMOJO_HOME/bin/setenv.sh

CLASSPATH=classes:.:$CLASSPATH

export CLASSPATH

echo Starting the Shopping Cart Application

$JAVA_HOME/bin/java tutorials.application.shoppingcart.ShoppingCartApplication
