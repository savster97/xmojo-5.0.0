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

package examples.mbeans.model;

import examples.applications.server.ServerInfo;
import java.lang.reflect.Constructor;

import javax.management.Descriptor;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.ObjectName;



/**
 *	This class is a example for ModelMBean. This class exposes the management
 *	information of a web server.
 *	@author <a href="mailto:xmojo-support@xmojo.org">XMOJO Team</a>
 *	@version 1.0
 */

 public class ModelServerInfo
 {
	/**
	 * Build the MBeanInfo which represents the management interface exposed
	 * by the MBean which comprises of attributes, constructors, operations
	 * and notifications.
	 */
	public static ModelMBeanInfo getMBeanInfo(ObjectName mbeanObjName, String mbeanName)
	{
		ModelMBeanInfo modelMBeanInfo = null;

		ModelMBeanOperationInfo[] operInfo = new ModelMBeanOperationInfo[6];
		ModelMBeanNotificationInfo[] notifInfo = new ModelMBeanNotificationInfo[1];

		try
		{
			Descriptor modelMBeanDescriptor = new DescriptorSupport(
				   new String[]	{(	  "name="+mbeanObjName),
									  "descriptorType=mbean",
									  ("displayName="+mbeanName),
									  "log=T",
									  "logfile=modelmbean.log",
									  "currencyTimeLimit=60",
									  "persistPolicy=noMoreOftenThan",
									  "persistPeriod=10",
									  "persistLocation=jmxPersistDir",
									  "persistName=serverInfo"
								});

			modelMBeanInfo = new ModelMBeanInfoSupport(
									"examples.applications.server.ServerInfo",
							       	"Web Server management application",
							       	getAttributesInfo(),
							       	getConstructorsInfo(),
							       	getOperationsInfo(),
							       	getNotificationsInfo());

			modelMBeanInfo.setMBeanDescriptor(modelMBeanDescriptor);

		} catch (Exception e)
		    {
			System.out.println("\nException in buildDynamicMBeanInfo : " + e.getMessage());
			e.printStackTrace();
		}

		return modelMBeanInfo;
	}

	/**
	 * Build the Attribute information.
	 */
	private static ModelMBeanAttributeInfo[] getAttributesInfo()
	{
		ModelMBeanAttributeInfo[] attrInfo = new ModelMBeanAttributeInfo[7];

		try
		{
			Descriptor portAttrDesc = new DescriptorSupport();
			portAttrDesc.setField("name","Port");
			portAttrDesc.setField("descriptorType","attribute");
			portAttrDesc.setField("displayName","Port");
			portAttrDesc.setField("getMethod","getPort");
			portAttrDesc.setField("setMethod","setPort");
			portAttrDesc.setField("currencyTimeLimit","0");

			attrInfo[0] = new ModelMBeanAttributeInfo("Port",
								     "int",
								     "Port: Server's port number",
								     true,
								     true,
								     false,
								     portAttrDesc);

			Descriptor restartCountAttrDesc = new DescriptorSupport();
			restartCountAttrDesc.setField("name","RestartCount");
			restartCountAttrDesc.setField("descriptorType", "attribute");
			restartCountAttrDesc.setField("default", "0");
			restartCountAttrDesc.setField("displayName","RestartCount");
			restartCountAttrDesc.setField("getMethod","getRestartCount");

			attrInfo[1] = new ModelMBeanAttributeInfo("RestartCount",
								     "int",
								     "RestartCount: number of times the server is restarted",
								     true,
								     false,
								     false,
								     restartCountAttrDesc);

			Descriptor serverIdAttrDesc = new DescriptorSupport();
			serverIdAttrDesc.setField("name","ServerId");
			serverIdAttrDesc.setField("descriptorType", "attribute");
			serverIdAttrDesc.setField("default", "test-server_1");
			serverIdAttrDesc.setField("displayName","ServerId");
			serverIdAttrDesc.setField("getMethod","getServerId");

			attrInfo[2] = new ModelMBeanAttributeInfo("ServerId",
								     "java.lang.String",
								     "ServerId: Server ID",
								     true,
								     false,
								     false,
								     serverIdAttrDesc);

			Descriptor serverNameAttrDesc = new DescriptorSupport();
			serverNameAttrDesc.setField("name","ServerName");
			serverNameAttrDesc.setField("descriptorType", "attribute");
			serverNameAttrDesc.setField("default", "test-server");
			serverNameAttrDesc.setField("displayName","ServerName");
			serverNameAttrDesc.setField("getMethod","getServerName");

			attrInfo[3] = new ModelMBeanAttributeInfo("ServerName",
								     "java.lang.String",
								     "ServerName: name of the server",
								     true,
								     false,
								     false,
								     serverNameAttrDesc);

			Descriptor serverUpTimeAttrDesc = new DescriptorSupport();
			serverUpTimeAttrDesc.setField("name","ServerUpTime");
			serverUpTimeAttrDesc.setField("descriptorType", "attribute");
			serverUpTimeAttrDesc.setField("default", "0");
			serverUpTimeAttrDesc.setField("displayName","ServerUpTime");
			serverUpTimeAttrDesc.setField("getMethod","getServerUpTime");

			attrInfo[4] = new ModelMBeanAttributeInfo("ServerUpTime",
								     "long",
								     "ServerUpTime: no. of milli seconds passed from the start of the server",
								     true,
								     false,
								     false,
								     serverUpTimeAttrDesc);

			Descriptor serverTimeAttrDesc = new DescriptorSupport();
			serverTimeAttrDesc.setField("name","StartTime");
			serverTimeAttrDesc.setField("descriptorType", "attribute");
			serverTimeAttrDesc.setField("default", "0");
			serverTimeAttrDesc.setField("displayName","StartTime");
			serverTimeAttrDesc.setField("getMethod","getStartTime");

			attrInfo[5] = new ModelMBeanAttributeInfo("StartTime",
								     "long",
								     "StartTime: time at which the server started",
								     true,
								     false,
								     false,
								     serverTimeAttrDesc);

			Descriptor serverStartedAttrDesc = new DescriptorSupport();
			serverStartedAttrDesc.setField("name","ServerStarted");
			serverStartedAttrDesc.setField("descriptorType", "attribute");
			serverStartedAttrDesc.setField("default", "false");
			serverStartedAttrDesc.setField("displayName","ServerStarted");
			serverStartedAttrDesc.setField("getMethod","isServerStarted");

			attrInfo[6] = new ModelMBeanAttributeInfo("ServerStarted",
								     "boolean",
								     "ServerStarted: indicates whether the server is started",
								     true,
								     false,
								     true,
								     serverStartedAttrDesc);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return attrInfo;
	}

	/**
	 * Build the Constructor information.
	 */
	private static ModelMBeanConstructorInfo[] getConstructorsInfo()
	{
		ModelMBeanConstructorInfo[] constInfo = new ModelMBeanConstructorInfo[2];

		try
		{
			Class svrInfoClass = ServerInfo.class;

			Constructor[] constructors = svrInfoClass.getConstructors();

			Descriptor serverInfoDefaultConstBeanDesc = new DescriptorSupport();
			serverInfoDefaultConstBeanDesc.setField("name","ServerInfo");
			serverInfoDefaultConstBeanDesc.setField("descriptorType", "operation");
			serverInfoDefaultConstBeanDesc.setField("role","constructor");

			constInfo[0] = new ModelMBeanConstructorInfo(
									"ServerInfo(): Constructs a ServerInfo Application",
									 constructors[0],
									 serverInfoDefaultConstBeanDesc);

			Descriptor serverInfoForuParamsConstBeanDesc = new DescriptorSupport();
			serverInfoForuParamsConstBeanDesc.setField("name","ServerInfoFourParams");
			serverInfoForuParamsConstBeanDesc.setField("descriptorType", "operation");
			serverInfoForuParamsConstBeanDesc.setField("role","constructor");

			constInfo[1] = new ModelMBeanConstructorInfo(
									"ServerInfo(): Constructs a ServerInfo Application and takes four params",
									 constructors[1],
									 serverInfoForuParamsConstBeanDesc);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return constInfo;
	}

	/**
	 * Build the Operation information.
	 */
	private static ModelMBeanOperationInfo[] getOperationsInfo()
	{
		ModelMBeanOperationInfo[] operInfo = new ModelMBeanOperationInfo[11];

		try
		{
			MBeanParameterInfo[] params = null;

			Descriptor startServiceOperDesc = new DescriptorSupport();
			startServiceOperDesc.setField("name","startService");
			startServiceOperDesc.setField("descriptorType","operation");
			startServiceOperDesc.setField("class","ServerInfo");
			startServiceOperDesc.setField("role","operation");

			operInfo[0] = new ModelMBeanOperationInfo("startService",
								     "startService(): Starts the server",
								     params,
								     "void",
								     MBeanOperationInfo.ACTION,
								     startServiceOperDesc);

			Descriptor stopServiceOperDesc = new DescriptorSupport();
			stopServiceOperDesc.setField("name","stopService");
			stopServiceOperDesc.setField("descriptorType","operation");
			stopServiceOperDesc.setField("class","ServerInfo");
			stopServiceOperDesc.setField("role","operation");

			operInfo[1] = new ModelMBeanOperationInfo("stopService",
								     "stopService(): Stops the server",
								     params,
								     "void",
								     MBeanOperationInfo.ACTION,
								     stopServiceOperDesc);

			Descriptor restartOperDesc = new DescriptorSupport();
			restartOperDesc.setField("name","restart");
			restartOperDesc.setField("descriptorType","operation");
			restartOperDesc.setField("class","ServerInfo");
			restartOperDesc.setField("role","operation");

			operInfo[2] = new ModelMBeanOperationInfo("restart",
								     "restart(): Restarts the server",
								     params,
								     "void",
								     MBeanOperationInfo.ACTION,
								     restartOperDesc);

			Descriptor getPortOperDesc = new DescriptorSupport();
			getPortOperDesc.setField("name","getPort");
			getPortOperDesc.setField("descriptorType","operation");
			getPortOperDesc.setField("class","ServerInfo");
			getPortOperDesc.setField("role","getter");

			operInfo[3] = new ModelMBeanOperationInfo("getPort",
								     "getPort(): Gets the Port number",
								     params,
								     "int",
								     MBeanOperationInfo.INFO,
								     getPortOperDesc);

			Descriptor getRestartCountOperDesc = new DescriptorSupport();
			getRestartCountOperDesc.setField("name","getRestartCount");
			getRestartCountOperDesc.setField("descriptorType","operation");
			getRestartCountOperDesc.setField("class","ServerInfo");
			getRestartCountOperDesc.setField("role","getter");

			operInfo[4] = new ModelMBeanOperationInfo("getRestartCount",
								     "getRestartCount(): Gets the restart count",
								     params,
								     "int",
								     MBeanOperationInfo.INFO,
								     getRestartCountOperDesc);

			Descriptor getServerIdOperDesc = new DescriptorSupport();
			getServerIdOperDesc.setField("name","getServerId");
			getServerIdOperDesc.setField("descriptorType","operation");
			getServerIdOperDesc.setField("class","ServerInfo");
			getServerIdOperDesc.setField("role","getter");

			operInfo[5] = new ModelMBeanOperationInfo("getServerId",
								     "getServerId(): Gets the server ID",
								     params,
								     "java.lang.String",
								     MBeanOperationInfo.INFO,
								     getServerIdOperDesc);

			Descriptor getServerNameOperDesc = new DescriptorSupport();
			getServerNameOperDesc.setField("name","getServerName");
			getServerNameOperDesc.setField("descriptorType","operation");
			getServerNameOperDesc.setField("class","ServerInfo");
			getServerNameOperDesc.setField("role","getter");

			operInfo[6] = new ModelMBeanOperationInfo("getServerName",
								     "getServerName(): Gets the server name",
								     params,
								     "java.lang.String",
								     MBeanOperationInfo.INFO,
								     getServerNameOperDesc);

			Descriptor getServerUpTimeOperDesc = new DescriptorSupport();
			getServerUpTimeOperDesc.setField("name","getServerUpTime");
			getServerUpTimeOperDesc.setField("descriptorType","operation");
			getServerUpTimeOperDesc.setField("class","ServerInfo");
			getServerUpTimeOperDesc.setField("role","getter");

			operInfo[7] = new ModelMBeanOperationInfo("getServerUpTime",
								     "getServerUpTime(): Gets the server up time",
								     params,
								     "java.lang.String",
								     MBeanOperationInfo.INFO,
								     getServerUpTimeOperDesc);

			Descriptor getServerStartTimeOperDesc = new DescriptorSupport();
			getServerStartTimeOperDesc.setField("name","getStartTime");
			getServerStartTimeOperDesc.setField("descriptorType","operation");
			getServerStartTimeOperDesc.setField("class","ServerInfo");
			getServerStartTimeOperDesc.setField("role","getter");

			operInfo[8] = new ModelMBeanOperationInfo("getStartTime",
								     "getStartTime(): Gets the server start up time",
								     params,
								     "long",
								     MBeanOperationInfo.INFO,
								     getServerStartTimeOperDesc);

			Descriptor getServerStartedOperDesc = new DescriptorSupport();
			getServerStartedOperDesc.setField("name","isServerStarted");
			getServerStartedOperDesc.setField("descriptorType","operation");
			getServerStartedOperDesc.setField("class","ServerInfo");
			getServerStartedOperDesc.setField("role","getter");

			operInfo[9] = new ModelMBeanOperationInfo("isServerStarted",
								     "isServerStarted(): Checks whether server is started",
								     params,
								     "boolean",
								     MBeanOperationInfo.INFO,
								     getServerStartedOperDesc);

			Descriptor setPortOperDesc = new DescriptorSupport();
			setPortOperDesc.setField("name","setPort");
			setPortOperDesc.setField("descriptorType","operation");
			setPortOperDesc.setField("class","ServerInfo");
			setPortOperDesc.setField("role","setter");

			MBeanParameterInfo[] setPortParms = new MBeanParameterInfo[]
									{ (new MBeanParameterInfo("portNumber",
														"int",
														"new port number") )} ;

			operInfo[10] = new ModelMBeanOperationInfo("setPort",
								     "setPort(): Sets the Port number",
								     setPortParms,
								     "void",
								     MBeanOperationInfo.ACTION,
								     setPortOperDesc);

		}
		catch (Exception e)
		{
			System.out.println("\nException in getOperationsInfo() : " + e.getMessage());
			e.printStackTrace();
		}

		return operInfo;
	}

	/**
	 * Build the Notification information.
	 */
	private static ModelMBeanNotificationInfo[] getNotificationsInfo()
	{
		ModelMBeanNotificationInfo[] notifInfo = new ModelMBeanNotificationInfo[0];

		return notifInfo;
	}
 }