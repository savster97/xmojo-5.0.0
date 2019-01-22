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
import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.RuntimeOperationsException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *	This class is a example for DynamicMBean. This class exposes the management
 *	information of a web server.
 *	@author <a href="mailto:xmojo-support@xmojo.org">XMOJO Team</a>
 *	@version 1.0
 */

public class DynamicServerInfo implements DynamicMBean {

        /////// Variables declaration ///////

        private static boolean log = false;

        private String serverName = null;

        private String serverId = null;

        private boolean serverStarted = true;

        private int port = 8080;

        private Date startTime = new Date();

        private long serverUpTime;

        private int restartCount;

        private boolean flag = false;

        MBeanAttributeInfo[] attribInfoArray = null;

        MBeanOperationInfo[] opInfoArray = null;

        MBeanParameterInfo[] paramInfoArray = null;

        MBeanConstructorInfo[] consInfoArray = null;

        MBeanNotificationInfo[] notifInfoArray = null;

        // DynamicMBean implementation for getMBeanInfo

        public MBeanInfo getMBeanInfo() {
                if (log) {
                        System.out.println("Constructing getMBeanInfo");
                }
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
                                        return getStartTime().toString();
                                }
                                if (attrName.equals("ServerUpTime")) {
                                        return new Long(getServerUpTime());
                                }
                                if (attrName.equals("RestartCount")) {
                                        return new Integer(
                                                getRestartCount());
                                }
                        }

                        if (attrName.equals("ServerName")) {
                                return getServerName();
                        }
                        else if (attrName.equals("ServerId")) {
                                return getServerId();
                        }
                        else if (attrName.equals("ServerStarted")) {
                                return new Boolean(isServerStarted());
                        }
                        else if (attrName.equals("ServerPort")) {
                                return new Integer(getPort());
                        }
                        else {
                                if (log) {
                                        System.out.println("Attribute : ["+
                                                attrName + "] does not exist");
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
                                        setPort( ((Integer) attrValue).
                                                intValue());
                                } else {
                                        if (log) {
                                                System.out.println("setPort is invoked with improper argument");
                                        }
                                        throw new InvalidAttributeValueException(
                                                "The attribute value does not match properly");
                                }
                        }
                        else {
                                if (log) {
                                        System.out.println("Attribute : ["+
                                                attrName + "] does not exist");
                                }
                                throw new AttributeNotFoundException("The specified attribute : ["+
                                        attrName + "] does not exist");
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
                                System.out.println("Exception caught while invoking setAttribute("+
                                        attrName + ", "+attrValue + "))");
                        }
                        e.printStackTrace();
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
                                Attribute attr =
                                        (Attribute) attribList.get(index);
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
                                if (log) {
                                        System.out.println("  ["+
                                                actionName + "] ~~ cannot be performed without invoking showDetails");
                                }
                                throw new RuntimeOperationsException(
                                        new RuntimeException("Specified operation is not found.")
                                        , "The restart is hidden.  Invoke the showDetails first so that restart can be enabled");
                        }
                        if (actionName.equals("startService") ||
                                actionName.equals("stopService") ||
                                actionName.equals("showDetails") ||
                                actionName.equals("hideDetails") ||
                                actionName.equals("getServerName") ||
                                actionName.equals("getServerId") ||
                                actionName.equals("isServerStarted") ||
                                actionName.equals("getPort") ||
                                actionName.equals("setPort") ||
                                actionName.equals("getStartTime") ||
                                actionName.equals("restart") ||
                                actionName.equals("getServerUpTime") ||
                                actionName.equals("getRestartCount")) {
                                m = DynamicServerInfo.class.getMethod(
                                        actionName, sig);

                                if (m == null) {
                                        if (log) {
                                                System.out.println("  Unable to invoke ["+
                                                        actionName + "] using Java Reflection");
                                        }
                                        throw new ReflectionException(
                                                new Exception("Exception occured while invoking the method")
                                                , "Method signature not specified properly");
                                } else {
                                        return m.invoke(this, params);
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
                        new MBeanNotificationInfo(notifTypes, "ServerNotification",
                        "Notification information");
        }

        // Constructor definitions

        public DynamicServerInfo() {
                serverName = "test-server";
                serverId = "test-server_1";
                serverStarted = true;
                port = 8072;
        }

        public DynamicServerInfo(String serverName, String serverId,
                boolean serverStarted, int port) {
                this.serverName = serverName ;
                this.serverId = serverId ;
                this.serverStarted = serverStarted ;
                this.port = port ;
        }

        // Methods definitions for the default exposed attributes and operations

        public String getServerName() {
                return serverName;
        }

        public String getServerId() {
                return serverId;
        }

        public boolean isServerStarted() {
                return serverStarted;
        }

        public int getPort() {
                return port;
        }

        public void setPort(int port) {
                stopService();
                port = this.port;
                startService();
        }

        public void startService() {
                // start the Server
                System.out.println("Server started at port [" + port + "] successfully. ");
                serverStarted = true;
                startTime = new Date();
        }

        public void stopService() {
                // stop the Server
                System.out.println("Server running at port [" + port + "] stopped. ");
                serverStarted = false;
        }

        public void showDetails() {
                flag = true;
        }

        public void hideDetails() {
                flag = false;
        }

        // Methods exposed during runtime when showDetails is invoked.

        public String getStartTime() {
                return startTime.toString();
        }

        public long getServerUpTime() {
                return (System.currentTimeMillis() - startTime.getTime());
        }

        public int getRestartCount() {
                return restartCount;
        }

        public void restart() {
                stopService();
                startService();
                restartCount ++;
                startTime = new Date();
        }

        public static void enableLog() {
                log = true;
        }

        public static void disableLog() {
                log = false;
        }
}

