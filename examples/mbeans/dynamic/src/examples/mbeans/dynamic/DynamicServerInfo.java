/**
 * The XMOJO Project 5
 * Copyright © 2003 XMOJO.org. All rights reserved.

 * NO WARRANTY

 * BECAUSE THE LIBRARY IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY FOR
 * THE LIBRARY, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN
 * OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES
 * PROVIDE THE LIBRARY "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS
 * TO THE QUALITY AND PERFORMANCE OF THE LIBRARY IS WITH YOU. SHOULD THE
 * LIBRARY PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,
 * REPAIR OR CORRECTION.

 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL
 * ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR REDISTRIBUTE
 * THE LIBRARY AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY
 * GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE
 * USE OR INABILITY TO USE THE LIBRARY (INCLUDING BUT NOT LIMITED TO LOSS OF
 * DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD
 * PARTIES OR A FAILURE OF THE LIBRARY TO OPERATE WITH ANY OTHER SOFTWARE),
 * EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGES.
**/

package examples.mbeans.dynamic;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.RuntimeOperationsException;
import java.lang.reflect.Method;
import java.util.Date;
import examples.applications.server.ServerInfo;

/**
 * A simple Dynamic MBean for exposing the management information of a
 * Web Server.
**/

public class DynamicServerInfo extends NotificationBroadcasterSupport implements DynamicMBean {

        ///// Variables Declaration ///////

        private static boolean log = false;

        private boolean flag = false;

        //used for notification handling...
        int sequenceNumber = 1;

        MBeanAttributeInfo[] attribInfoArray = null;

        MBeanOperationInfo[] opInfoArray = null;

        MBeanParameterInfo[] paramInfoArray = null;

        MBeanConstructorInfo[] consInfoArray = null;

        MBeanNotificationInfo[] notifInfoArray = null;

        ServerInfo server = null;

        //////// Constructor Definitions ////////

        public DynamicServerInfo() {
                server = new ServerInfo();
        }

        public DynamicServerInfo(String serverName, String serverId,
                boolean serverStarted, int port) {
                server = new ServerInfo(serverName, serverId,
                        serverStarted, port);
        }

        // DynamicMBean implementation for getMBeanInfo

        public MBeanInfo getMBeanInfo() {
/*                if (log) {
                        System.out.println("Constructing MBeanInfo");
                } */
                MBeanInfo minfo = null;

                try {
                        // constructing MBean constructors
                        constructConstructors();

                        // constructing MBean attributes
                        constructAttributes();

                        // constructing MBean operations
                        constructOperations();

                        // construct MBean Notifications
                        constructNotifications();

                        // constructing the MBean information
                        minfo = new MBeanInfo("DynamicServerInfo", "Dynamic MBean for managing the web-server",
                                attribInfoArray, consInfoArray,
                                opInfoArray, notifInfoArray);

                } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                }

                return minfo;
        }

        // DynamicMBean implementation for getAttribute

        public Object getAttribute(String attrName)
                throws AttributeNotFoundException , MBeanException ,
        ReflectionException {

                try {
                        if (flag) {

                                if (attrName.equals("StartTime")) {
                                        return server.getStartTime().toString();
                                }
                                if (attrName.equals("ServerUpTime")) {
                                        return new Long(
                                                server.getServerUpTime());
                                }
                                if (attrName.equals("RestartCount")) {
                                        return new Integer(
                                                server.getRestartCount());
                                }
                        }

                        if (attrName.equals("ServerName")) {
                                return server.getServerName();
                        } else if (attrName.equals("ServerId")) {
                                return server.getServerId();
                        } else if (attrName.equals("ServerStarted")) {
                                return new Boolean(
                                        server.isServerStarted());
                        } else if (attrName.equals("ServerPort")) {
                                return new Integer(server.getPort());
                        } else {
                                if (log) {
                                        System.out.println("Attribute : ["+ attrName + "] does not exist");
                                }
                                throw new AttributeNotFoundException("The specified attribute does not exist");
                        }
                } catch (RuntimeException e) {
                        throw new RuntimeOperationsException(e,
                                e.getMessage());
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        // Dynamic MBean implementation for setAttribute

        public void setAttribute(Attribute attrib)
                throws AttributeNotFoundException ,
        InvalidAttributeValueException , MBeanException ,
        ReflectionException {
                String attrName = attrib.getName();
                Object attrValue = attrib.getValue();

                try {
                        if (attrName.equals("ServerPort")) {
                                if (attrValue instanceof java.lang.Integer) {
                                        server.setPort(
                                                ((Integer) attrValue).
                                                intValue());
                                } else {
                                        if (log) {
                                                System.out.println("setPort is invoked with improper argument");
                                        }
                                        throw new InvalidAttributeValueException(
                                                "The attribute value does not match properly");
                                }
                        } else {
                                if (log) {
                                        System.out.println("Attribute : ["+ attrName + "] does not exist");
                                }
                                throw new AttributeNotFoundException("The specified attribute : ["+ attrName + "] does not exist");
                        }
                } catch (RuntimeException e) {
                        if (log) {
                                System.out.println("RuntimeException caught while invoking setAttribute("+
                                        attrName + ", "+attrValue + ")) and exception is re-thrown");
                        }
                        throw new RuntimeOperationsException(e,
                                e.getMessage());
                }
                catch (Exception e) {
                        if (log) {
                             //   System.out.println("Exception caught while invoking setAttribute("+
                             //           attrName + ", "+attrValue + "))");
                        }
                      //  e.printStackTrace();
                }
        }

        // Dynamic MBean implementation for getAttributes

        public AttributeList getAttributes(String[] attribs) {

                AttributeList attribList = new AttributeList();

                for (int index = 0; index < attribs.length ; index ++) {
                        try {
                                Object obj = getAttribute(attribs[index]);
                                Attribute a = new Attribute(attribs[index],
                                        obj);
                                attribList.add(a);
                        } catch (Exception e) {
                                // Exception should not be thrown to the caller.
                                if (log) {
                                        System.out.println("Exception occured while performing getAttributes");
                                }
                        }
                }

                return attribList;
        }

        // Dynamic MBean implementation for setAttributes

        public AttributeList setAttributes(AttributeList attribList) {

                AttributeList newList = new AttributeList();

                for (int index = 0; index < attribList.size(); index ++) {
                        try {
                                Attribute attr = (Attribute) attribList.get(index);
                                setAttribute(attr);
                                newList.add(attr);
                        } catch (Exception e) {
                                // Exception should not be thrown to the caller.
                                if (log) {
                                        System.out.println("Exception occured while performing setAttributes");
                                }
                        }
                }

                return newList;
        }

        // Dynamic MBean implementation for invoke

        public Object invoke(String actionName, Object[] params,
                String[] signature) throws MBeanException ,
        ReflectionException {
                if (log) {
                        System.out.println("Inside invoke and actionName is : ["+ actionName + "]");
                }

                Method m = null;

                int len = 0;

                if (signature != null) {
                        len = signature.length;
                }

                Class[] sig = new Class[len];

                try {
                        for (int index = 0; index < len; index ++) {
                                sig[index] =
                                        Class.forName(signature[index]);
                        }

                        if (! flag && actionName.equals("restart")) {

                                throw new RuntimeOperationsException(
                                        new RuntimeException("Specified operation is not found.")
                                        , "The restart is hidden.  Invoke the showDetails first so that restart can be enabled");
                        }
                        if (actionName.equals("showDetails") ||
                                actionName.equals("hideDetails")) {
                                m = DynamicServerInfo.class.getMethod(
                                        actionName, sig);
                                if (m == null) {
                                        if (log) {
                                                System.out.println("  Unable to invoke ["+ actionName + "] using Java Reflection");
                                        }
                                        throw new ReflectionException(
                                                new Exception("Exception occured while invoking the method")
                                                , "Method signature not specified properly");
                                } else {
                                        return m.invoke(this, params);
                                }
                        }


                        if (actionName.equals("startService") ||
                                actionName.equals("stopService") ||
                                actionName.equals("getServerName") ||
                                actionName.equals("getServerId") ||
                                actionName.equals("isServerStarted") ||
                                actionName.equals("getPort") ||
                                actionName.equals("setPort") ||
                                actionName.equals("getStartTime") ||
                                actionName.equals("restart") ||
                                actionName.equals("getServerUpTime") ||
                                actionName.equals("getRestartCount")) {
                                m = ServerInfo.class.getMethod(actionName, sig);

                                if (m == null) {
                                        if (log) {
                                                System.out.println("  Unable to invoke ["+ actionName + "] using Java Reflection");
                                        }
                                        throw new ReflectionException(
                                                new Exception("Exception occured while invoking the method")
                                                , "Method signature not specified properly");
                                } else {
                                        Object result =
                                                m.invoke(server, params);
                                        if (actionName.equals("startService")) {
                                                Notification n =
                                                        new Notification(
                                                        "server.started",
                                                        this.getClass().getName(), sequenceNumber,
                                                        new Date().
                                                        getTime(), "Server started");
                                                sendNotification(n);
                                                sequenceNumber++;

                                        }
                                        if (actionName.equals("stopService")) {
                                                Notification n =
                                                        new Notification(
                                                        "server.stopped",
                                                        this.getClass().getName(), sequenceNumber,
                                                        new Date().
                                                        getTime(), "Server stopped");
                                                sendNotification(n);
                                                sequenceNumber++;

                                        }
                                        return result;
                                }
                        } else {
                                if (log) {
                                        System.out.println("  ["+
                                                actionName + "] Not found");
                                }
                                throw new RuntimeOperationsException(
                                        new RuntimeException("Specified action is not found.")
                                        , "Kindly check the actionName string passed");
                        }
                } catch (Exception e) {
                        if (log) {
                                System.out.println("  Exception caught inside catch block of invoke");
                        }
                        e.printStackTrace();
                }
                return null;
        }


        public void constructConstructors()
                throws IllegalArgumentException {
                consInfoArray = new MBeanConstructorInfo[2];

                consInfoArray[0] =
                        new MBeanConstructorInfo("DynamicServerInfo",
                        "Default public constructor", null);

                // constructing the parameters of the constructor

                paramInfoArray = new MBeanParameterInfo[4];

                paramInfoArray[0] =
                        new MBeanParameterInfo("server_name", "java.lang.String",
                        "Name of the server");

                paramInfoArray[1] =
                        new MBeanParameterInfo("server_id", "java.lang.String",
                        "Identifier for the server");

                paramInfoArray[2] =
                        new MBeanParameterInfo("started", "boolean", "A boolean flag to check whether the server is active");

                paramInfoArray[3] =
                        new MBeanParameterInfo("port", "int", "Port at which the server is listening");

                consInfoArray[1] =
                        new MBeanConstructorInfo("DynamicServerInfo",
                        "Three argument constructor", paramInfoArray);

        }

        public void constructAttributes() throws IllegalArgumentException {
                if (flag) {
                        attribInfoArray = new MBeanAttributeInfo[7];
                } else {
                        attribInfoArray = new MBeanAttributeInfo[4];
                }

                attribInfoArray[0] =
                        new MBeanAttributeInfo("ServerName", "java.lang.String",
                        "The name of the server", true, false, false);

                attribInfoArray[1] =
                        new MBeanAttributeInfo("ServerId", "java.lang.String",
                        "The server identity", true, false, false);

                attribInfoArray[2] =
                        new MBeanAttributeInfo("ServerStarted", "boolean",
                        "To check the server status", true, false, true);

                attribInfoArray[3] =
                        new MBeanAttributeInfo("ServerPort", "int", "The port at which the server is listening",
                        true, true, false);

                if (flag) {
                        if (log) {
                                System.out.println("Additional [3] attributes constructed");
                        }

                        attribInfoArray[4] =
                                new MBeanAttributeInfo("StartTime", "java.lang.String",
                                "The start time of the server", true,
                                false, false);

                        attribInfoArray[5] =
                                new MBeanAttributeInfo("ServerUpTime",
                                "long", "The server uptime", true,
                                false, false);

                        attribInfoArray[6] =
                                new MBeanAttributeInfo("RestartCount",
                                "int", "The number of times the server restarted",
                                true, false, false);

                }
        }

        public void constructOperations() throws IllegalArgumentException {
                if (flag) {
                        opInfoArray = new MBeanOperationInfo[5];
                } else {
                        opInfoArray = new MBeanOperationInfo[4];
                }

                opInfoArray[0] =
                        new MBeanOperationInfo("startService", "To start the server",
                        null, "void", MBeanOperationInfo.ACTION);

                opInfoArray[1] =
                        new MBeanOperationInfo("stopService", "To stop the server",
                        null, "void", MBeanOperationInfo.ACTION);

                opInfoArray[2] =
                        new MBeanOperationInfo("showDetails", "To show additional details",
                        null, "void", MBeanOperationInfo.ACTION);

                opInfoArray[3] =
                        new MBeanOperationInfo("hideDetails", "To hide additional details",
                        null, "void", MBeanOperationInfo.ACTION);

                if (flag) {
                        if (log) {
                                System.out.println("Additional [1] operation constructed");
                        }
                        opInfoArray[4] =
                                new MBeanOperationInfo("restart", "To restart the server",
                                null, "void", MBeanOperationInfo.ACTION);
                }
        }

        public void constructNotifications()
                throws IllegalArgumentException {
                notifInfoArray = new MBeanNotificationInfo[1];

                String[] notifTypes = new String[]{"server.started", "server.stopped"};

                notifInfoArray[0] =
                        new MBeanNotificationInfo(notifTypes, "examples.mbeans.dynamic.DynamicServerInfo",
                        "Server status (started/stopped) event");
        }

        public void showDetails() {
                flag = true;
        }

        public void hideDetails() {
                flag = false;
        }

        public static void enableLog() {
                log = true;
        }

        public static void disableLog() {
                log = false;
        }
}



