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

/**
 * A simple Dynamic MBean for configuring a logger instance.
**/

public class DynamicLogConfig implements DynamicMBean {

        ///// Variables Declaration ///////

        private static boolean log = false;

        boolean stopper = false;

        AttributeList alist = new AttributeList();

        MBeanAttributeInfo[] attribInfoArray = null;

        MBeanOperationInfo[] opInfoArray = null;

        MBeanParameterInfo[] paramInfoArray = null;

        MBeanConstructorInfo[] consInfoArray = null;

        MBeanNotificationInfo[] notifInfoArray = null;

        // DynamicMBean implementation for getMBeanInfo

        public MBeanInfo getMBeanInfo() {
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

                        minfo = new MBeanInfo("DynamicLogConfig", "Dynamic MBean for managing the log file configuration",
                                attribInfoArray, consInfoArray,
                                opInfoArray, notifInfoArray);

                } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                }

                return minfo;
        }

        // DynamicMBean implementation for getAttribute

        public Object getAttribute(String attributeName)
                throws AttributeNotFoundException , MBeanException ,
        ReflectionException {

                try {

                        if (attributeName.equals("LogLevel")) {
                                return getLogLevel();
                        }
                        else if (attributeName.equals("LogFileName")) {
                                return getLogFileName();
                        }
                        else if (attributeName.equals("LogDirectory")) {
                                return getLogDirectory();
                        }
                        else if (attributeName.equals("MaxLines")) {
                                return new Integer(getMaxLines());
                        }
                        else if (attributeName.equals("RotationLimit")) {
                                return new Integer(getRotationLimit());
                        }
                        else {
                                if (log) {
                                        System.out.println("[" + attributeName + "] not found");
                                }
                                throw new AttributeNotFoundException("The specified attribute does not exist");
                        }
                } catch (RuntimeException e) {
                        if (log) {
                                System.out.println("RuntimeException caught inside catch block of getAttribute and thrown");
                        }
                        throw new RuntimeOperationsException(e,
                                e.getMessage());
                }
                catch (Exception e) {
                        if (log) {
                                System.out.println("Exception caught inside catch block of getAttribute");
                        }
                        e.printStackTrace();
                }

                return null;

        }

        // Dynamic MBean implementation for setAttribute

        public void setAttribute(Attribute attrib)
                throws AttributeNotFoundException ,
        InvalidAttributeValueException , MBeanException ,
        ReflectionException {
                String attributeName = attrib.getName();
                Object attributeValue = attrib.getValue();

                try {

                        if (! stopper) {
                                System.out.println("  ** setAttribute cannot be invoked as Logger is running");
                                throw new IllegalArgumentException("Write operation is not permitted when the logger is running.  Kindly stop the logger and try again");
                        } else {
                                if (attributeName.equals("LogLevel")) {
                                        setLogLevel(
                                                (String) attributeValue);
                                }
                                else if (attributeName.equals("LogFileName")) {
                                        setLogFileName(
                                                (String) attributeValue);
                                }
                                else if (attributeName.equals("LogDirectory")) {
                                        setLogDirectory(
                                                (String) attributeValue);
                                }
                                else if (attributeName.equals("MaxLines")) {
                                        setMaxLines( ((Integer)
                                                attributeValue).
                                                intValue());
                                }
                                else if (attributeName.equals("RotationLimit")) {
                                        setRotationLimit( ((Integer)
                                                attributeValue).
                                                intValue());
                                }
                                else {
                                        if (log) {
                                                System.out.println("  ** ["+
                                                        attributeName +
                                                        "] does not exist");
                                        }
                                        throw new AttributeNotFoundException(
                                                "The specified attribute does not exist");
                                }
                        }
                } catch (Exception e) {
                        if (log) {
                                System.out.println("Exception caught inside catch block of setAttribute");
                        }
                        e.printStackTrace();
                }

        }

        // Dynamic MBean implementation of getAttributes

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
                                        System.out.println(
                                                "Exception caught while performing getAttributes for attribute :: [" +
                                                attribs[index] + "]");
                                }

                        }
                }

                return attribList;
        }

        // Dynamic MBean implementation of setAttributes

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
                                        System.out.println(
                                                "Exception caught while performing setAttributes for attribute :: [" +
                                                ((Attribute)
                                                attribList.get(index)).
                                                getName() +
                                                " with the value :: [" +
                                                ((Attribute)
                                                attribList.get(index)).
                                                getValue() + "]");
                                }

                        }
                }

                return newList;
        }

        // Dynamic MBean implementation of invoke

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

                        if (actionName.equals("startLogging") ||
                                actionName.equals("stopLogging") ||
                                actionName.equals("getLogLevel") ||
                                actionName.equals("setLogLevel") ||
                                actionName.equals("getLogFileName") ||
                                actionName.equals("setLogFileName") ||
                                actionName.equals("getLogDirectory") ||
                                actionName.equals("setLogDirectory") ||
                                actionName.equals("getMaxLines") ||
                                actionName.equals("setMaxLines") ||
                                actionName.equals("getRotationLimit")
                                || actionName.equals("setRotationLimit")) {

                                if (stopper && actionName.equals("stopLogging")) {
                                        if (log) {
                                                System.out.println("  Logger already stopped");
                                        }
                                        throw new IllegalArgumentException(
                                                "Logger is already stopped.  Invoke startLogging so that stopLogging is enabled.");
                                } else if (!stopper &&
                                        actionName.equals("startLogging")) {
                                        if (log) {
                                                System.out.println("  Logger already started");
                                        }
                                        throw new IllegalArgumentException(
                                                "Logger is already started.  Invoke stopLogging so that startLogging is enabled.");
                                } else if (stopper &&
                                        (actionName.equals("setLogLevel")
                                        || actionName.equals("setLogFileName")
                                        || actionName.equals("setLogDirectory")
                                        || actionName.equals("setMaxLines")
                                        || actionName.equals("setRotationLimit"))) {
                                        if (log) {
                                                System.out.println("  Logger running.  Write operation not permitted");
                                        }
                                        throw new IllegalArgumentException(
                                                "Write operation is not permitted when the logger is running.  Kindly stop the logger and try again");
                                } else {
                                        m = DynamicLogConfig.class.getMethod(
                                                actionName, sig);

                                        if (m == null) {
                                                if (log) {
                                                        System.out.println(
                                                                "  Unable to invoke ["+
                                                                actionName +
                                                                "] using Java Reflection");
                                                }

                                                throw new ReflectionException(
                                                        new Exception("Problem while invoking the method")
                                                        , "Method signature not specified properly");
                                        } else {
                                                if (log) {
                                                        System.out.println(
                                                                "  ["+
                                                                actionName +
                                                                "] to be invoked");
                                                }
                                                return m.invoke(this,
                                                        params);
                                        }
                                }
                        } else {
                                if (log) {
                                        System.out.println("  [" + actionName + "] not found");
                                }
                                throw new RuntimeOperationsException(
                                        new RuntimeException("Specified action is not found.")
                                        , "Kindly check the actionName string passed");
                        }
                } catch (Exception e) {
                        if (log) {
                           //     System.out.println("  Exception caught inside catch block of invoke");
                        }
                     //   e.printStackTrace();
                }
                return null;
        }

        public void constructConstructors()
                throws IllegalArgumentException {
                consInfoArray = new MBeanConstructorInfo[2];

                consInfoArray[0] =
                        new MBeanConstructorInfo("DynamicLogConfig", "Default public constructor",
                        null);

                // constructing the parameters of the constructor

                paramInfoArray = new MBeanParameterInfo[5];

                paramInfoArray[0] =
                        new MBeanParameterInfo("log_level", "java.lang.String",
                        "Logging level");

                paramInfoArray[1] =
                        new MBeanParameterInfo("log_filename", "java.lang.String",
                        "Name of the log file");

                paramInfoArray[2] =
                        new MBeanParameterInfo("log_directory", "java.lang.String",
                        "Directory for the log file");

                paramInfoArray[3] =
                        new MBeanParameterInfo("max_lines", "int", "Maximum lines to be logged per file");

                paramInfoArray[4] =
                        new MBeanParameterInfo("rotation_limit", "int",
                        "Maximum number of files that can be created");

                consInfoArray[1] =
                        new MBeanConstructorInfo("DynamicLogConfig", "Three argument constructor",
                        paramInfoArray);

        }

        public void constructAttributes() throws IllegalArgumentException {

                attribInfoArray = new MBeanAttributeInfo[5];

                if (!stopper) {
                        if (log) {
                                System.out.println("Constructing readOnly attributes");
                        }

                        attribInfoArray[0] =
                                new MBeanAttributeInfo("LogLevel", "java.lang.String",
                                "logging level", true, false, false);

                        attribInfoArray[1] =
                                new MBeanAttributeInfo("LogFileName",
                                "java.lang.String", "name of the log file",
                                true, false, false);

                        attribInfoArray[2] =
                                new MBeanAttributeInfo("LogDirectory",
                                "java.lang.String", "Directory of the log file",
                                true, false, false);

                        attribInfoArray[3] =
                                new MBeanAttributeInfo("MaxLines", "int",
                                "The maximum number of lines to be logged per file",
                                true, false, false);

                        attribInfoArray[4] =
                                new MBeanAttributeInfo("RotationLimit",
                                "int", "The maximum number of files to be created",
                                true, false, false);
                } else {
                        if (log) {
                                System.out.println("Constructing readWrite attributes");
                        }


                        attribInfoArray[0] =
                                new MBeanAttributeInfo("LogLevel", "java.lang.String",
                                "The name of the server", true, true,
                                false);

                        attribInfoArray[1] =
                                new MBeanAttributeInfo("LogFileName",
                                "java.lang.String", "The server identity",
                                true, true, false);

                        attribInfoArray[2] =
                                new MBeanAttributeInfo("LogDirectory",
                                "java.lang.String", "Directory of the log file",
                                true, true, false);

                        attribInfoArray[3] =
                                new MBeanAttributeInfo("MaxLines", "int",
                                "The maximum number of lines to be logged per file",
                                true, true, false);

                        attribInfoArray[4] =
                                new MBeanAttributeInfo("RotationLimit",
                                "int", "The maximum number of files to be created",
                                true, true, false);
                }

        }

        public void constructOperations() throws IllegalArgumentException {
                opInfoArray = new MBeanOperationInfo[1];

                if (stopper) {
                        if (log) {
                                System.out.println("Constructing operations ~~ startLogging");
                        }

                        opInfoArray[0] =
                                new MBeanOperationInfo("startLogging",
                                "To start the logging", null, "java.lang.String",
                                MBeanOperationInfo.ACTION);
                } else {
                        if (log) {
                                System.out.println("Constructing operations ~~ stopLogging");
                        }

                        opInfoArray[0] =
                                new MBeanOperationInfo("stopLogging",
                                "To stop the logging", null, "java.lang.String",
                                MBeanOperationInfo.ACTION);
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

        public DynamicLogConfig() {
                level = "INFO";
                filename = "test.log";
                directory = "C:\\mylogs";
                max_lines = 1000;
                rotation_num = 10;
        }

        public DynamicLogConfig(String level, String filename,
                String directory, int max_lines, int rotation_num) {
                this.level = level;
                this.filename = filename;
                this.directory = directory;
                this.max_lines = max_lines;
                this.rotation_num = rotation_num;
        }

        private String level = null;

        private String filename = null;

        private String directory = null;

        private int max_lines;

        private int rotation_num;

        public String getLogLevel() {
                if (log) {
                        System.out.println("Inside getLogLevel.  Level value is : ["+ level + "]");
                }

                // get the log level
                return level;
        }

        public void setLogLevel(String level) {
                // set the log level
                this.level = level;

                if (log) {
                        System.out.println("Inside setLogLevel.  Level value is : ["+ level + "]");
                }
        }

        public String getLogFileName() {
                if (log) {
                        System.out.println("Inside getLogFileName.  Filename value is : ["+ filename + "]");
                }

                // get the log filename
                return filename;
        }

        public void setLogFileName(String filename) {
                // set the log filename
                this.filename = filename;

                if (log) {
                        System.out.println("Inside setLogFileName.  Filename value is : ["+ filename + "]");
                }
        }

        public String getLogDirectory() {
                if (log) {
                        System.out.println("Inside getDirectory.  Directory value is : ["+ directory + "]");
                }

                // get the directory
                return directory;
        }

        public void setLogDirectory(String directory) {
                // set the directory
                this.directory = directory;

                if (log) {
                        System.out.println("Inside setDirectory.  Directory value is : ["+ directory + "]");
                }
        }

        public int getMaxLines() {
                if (log) {
                        System.out.println("Inside getMaxLines.  Max Lines value is : ["+ max_lines + "]");
                }

                // get the Maximum lines
                return max_lines;
        }

        public void setMaxLines(int max_lines) {
                // set the Maximum lines
                this.max_lines = max_lines;

                if (log) {
                        System.out.println("Inside setMaxLines.  Max Lines value is : ["+ max_lines + "]");
                }
        }

        public int getRotationLimit() {
                if (log) {
                        System.out.println("Inside getRotationLimit.  Rotation limit value is : ["+ rotation_num + "]");
                }

                // get the rotation limit
                return rotation_num;
        }

        public void setRotationLimit(int rotation_num) {
                // set the rotation limit
                this.rotation_num = rotation_num;

                if (log) {
                        System.out.println("Inside setRotationLimit.  Rotation limit value is : ["+ rotation_num + "]");
                }
        }

        public String startLogging() {
                stopper = false;

                if (log) {
                        System.out.println("Inside startLogging. The stopper value is : {"+ stopper + "}");
                }

                return "Logging started";
        }

        public String stopLogging() {
                stopper = true;

                if (log) {
                        System.out.println("Inside stopLogging. The stopper value is : {"+ stopper + "}");
                }

                return "Logging stopped";
        }

        public static void enableLog() {
                log = true;
        }

        public static void disableLog() {
                log = false;
        }
}
