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

package examples.agent;


import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.modelmbean.RequiredModelMBean;
import javax.management.modelmbean.ModelMBeanInfo;

import com.adventnet.adaptors.rmi.RMIAdaptor;
import com.adventnet.adaptors.html.HtmlAdaptor;
import com.adventnet.adaptors.html.JettyHtmlServer;
import com.adventnet.utils.jmx.*;
import com.adventnet.agent.utilities.common.*;
import com.adventnet.agent.logging.*;

import examples.applications.server.ServerInfo;
import examples.mbeans.model.ModelServerInfo;

/**
 *	This class is the main JMXAgent application. This takes care of creating and
 *	registering the various protocol interfaces (Adaptors) to expose the MBeans.
 *	This JMXAgent provides two Adaptors namely
 *	<pre>
 				RMI
 				HTML
 *	</pre>
 *	@author <a href="mailto:xmojo-support@xmojo.org">XMOJO Team</a>
 *	@version 1.0
 */

public class RunModelAgent
{
	/**
	 * The MBean Repository for the JMXAgent
	 */
	MBeanServer server = null;

        /**
         * The Unique identifier for a MBean
         */
        ObjectName name = null;

	boolean isMBeanInfoFromXML = false;

    /**
     * Variables representing the different configurations for different adaptors.
     */
    private RMIAdaptor rmiadaptor = null;
    private int rmiPort = 1099;

    private HtmlAdaptor htmladaptor = null;
    private String htmlRootDir = ".";
    private int htmlPort = 8030; // HTML Adaptor Port will be overridden by the port value
                                // in %htmlRootDir%/conf/http/etc/JettyConfig.xml file.

	private int loggingLevel = Level.FATAL;

	///////////////////////Model MBean Declaration./////////////////////////
	/**
	 * Variable on whose behalf the ModelMBean is formed
	 */
	ServerInfo svrInfo = null;
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor with a String array as parameter.
	 *
	 * @param args
	 */
	public RunModelAgent(String[] args)
	{
		// setting Logging Level
		LogFactory.setLoggingLevel(loggingLevel);

		isMBeanInfoFromXML = false;

		try
		{
			if(args != null && args.length > 0)
			{
				if(args[0].equalsIgnoreCase("xml"))
					isMBeanInfoFromXML = true;
			}
		}
		catch(Exception ex) {
    		ex.printStackTrace();
        }
	}


	/**
	 * This method creates the Server Repository for the JMX Agent.
	 */
    private void createMBS() {
        try {
            server = MBeanServerFactory.createMBeanServer("myDomain");
            System.out.println("MBeanServer instance creation successful");
        } catch (Exception e) {
            System.out.println("MBeanServer instance creation failed");
            e.printStackTrace();
        }
    }

	/**
	 * This method registers the RMI Interface and HTML interface for the JMXAgent.
	 */
	private void registerAdaptors()
	{
		registerRMIAdaptor();
		registerHTMLAdaptor();
	}

        /**
         * This method registers the RMI Adaptor for the JMXAgent.
         */
        private void registerRMIAdaptor() {
                try {
                        rmiadaptor = new RMIAdaptor();
                        rmiadaptor.setPort(rmiPort);
                        name = new ObjectName("Adaptors:type=RMIAdaptor");
                        server.registerMBean(rmiadaptor, name);
                } catch (Exception e) {
                        System.out.println("Exception while initializing RMI adaptor");
                        e.printStackTrace();
                }
        }

        /**
         * This method registers the HTML Adaptor for the JMXAgent.
         */
        private void registerHTMLAdaptor() {
                try {
                        htmladaptor = new HtmlAdaptor();
                        htmladaptor.setParentDir(htmlRootDir);
                        htmladaptor.addHttpServerInterface(
                                new JettyHtmlServer());
                        name = new ObjectName("Adaptors:type=HTMLAdaptor");
                        server.registerMBean(htmladaptor, name);
                } catch (Exception e) {
                        System.out.println("Exception while initializing HTML adaptor");
                        e.printStackTrace();
                }
        }

	/**
	 * This method creates and registers the ModelMBean with the MBeanServer.
	 */
	private void registerMBeans()
	{
		try
		{
			svrInfo = new ServerInfo();

			name = new ObjectName("ModelDomain:name=serverInfo");
			ModelMBeanInfo mbeanInfo = null;

			if(isMBeanInfoFromXML)
			{
    			System.out.println("MBeanInfo obtained from xml file");
				mbeanInfo = Utilities.convertXmlToModelMBeanInfo(
							"xml/ServerInfo.xml");
			}
			else
			{
    			System.out.println("MBeanInfo obtained from the ModelServerInfo class");
				mbeanInfo = ModelServerInfo.getMBeanInfo(name, "ServerInfoMBean");
			}

			RequiredModelMBean rmm = new RequiredModelMBean(mbeanInfo);

			rmm.setManagedResource(svrInfo, "ObjectReference");
			server.registerMBean(rmm, name);
			System.out.println("ModelMBeanRegistration successful");
		}
		catch(Exception e)
		{
			System.out.println("Exception occured while registering the ModelMBean!!!"+
			e.getMessage());
		}
	}

	/**
	 *	The main method to start the JMXAgent.
	 */
	public static void main(String[] args)
	{
		RunModelAgent run = new RunModelAgent(args);
		run.createMBS();
		run.registerAdaptors();
		run.registerMBeans();
	}
}