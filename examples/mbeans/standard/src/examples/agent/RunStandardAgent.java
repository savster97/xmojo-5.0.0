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

 import javax.management.MBeanServerFactory;
 import javax.management.MBeanServer;
 import javax.management.ObjectName;
 import com.adventnet.adaptors.rmi.RMIAdaptor;
 import com.adventnet.adaptors.html.HtmlAdaptor;
 import com.adventnet.adaptors.html.JettyHtmlServer;
 import com.adventnet.agent.logging.*;
 import examples.mbeans.standard.ServerInfo;
 import examples.mbeans.standard.Overloaded;

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


 public class RunStandardAgent
 {

	/**
	 * The MBean Repository for the JMXAgent
	 */
     MBeanServer mbs = null;

	/**
	 * The Unique identifier for a MBean
	 */
     ObjectName name = null;

	/**
	 * Variables representing the different configurations for different adaptors.
	 */
	private RMIAdaptor rmiadaptor = null;
	private int rmiPort = 1099;

	private HtmlAdaptor htmladaptor= null;
	private String htmlRootDir = ".";
	private int htmlPort = 8030;  // HTML Adaptor Port will be overridden by the port value
	                              // in %htmlRootDir%/conf/http/etc/JettyConfig.xml file.

	private int loggingLevel = Level.FATAL;

     public RunStandardAgent()
     {
		 // setting Logging Level
		 LogFactory.setLoggingLevel(loggingLevel);

     }

	/**
	 * This method creates the Server Repository for the JMX Agent.
	 */
     private void createMBS()
     {
         try
         {
             mbs = MBeanServerFactory.createMBeanServer("myDomain");
             System.out.println("MBeanServer instance creation successful");
         }
         catch(Exception e)
         {
             System.out.println("MBeanServer instance creation failed");
             e.printStackTrace();
         }
     }

	/**
	 * This method registers the adaptors for the JMXAgent.
	 */
     private void registerAdaptors()
     {
         registerRMIAdaptor();
         registerHTMLAdaptor();
     }

	/**
	 * This method registers the RMI Adaptor for the JMXAgent.
	 */
     private void registerRMIAdaptor()
     {
        try
        {
            rmiadaptor = new RMIAdaptor();
            rmiadaptor.setPort(rmiPort);
            name = new ObjectName("Adaptors:type=RMIAdaptor");
            mbs.registerMBean(rmiadaptor, name);
        }
        catch(Exception e)
        {
            System.out.println("Exception while initializing RMI adaptor");
            e.printStackTrace();
        }
     }

	/**
	 * This method registers the HTML Adaptor for the JMXAgent.
	 */
     private void registerHTMLAdaptor()
     {
        try
        {
            htmladaptor = new HtmlAdaptor();
            htmladaptor.setParentDir(htmlRootDir);
            htmladaptor.addHttpServerInterface(new JettyHtmlServer());
            name = new ObjectName("Adaptors:type=HTMLAdaptor");
            mbs.registerMBean(htmladaptor, name);
        }
        catch(Exception e)
        {
            System.out.println("Exception while initializing HTML adaptor");
            e.printStackTrace();
        }
     }

	/**
	 * This method creates and registers the StandardMBeans with the MBeanServer.
	 */
     private void registerMBeans()
     {
         ServerInfo serverbean = new ServerInfo();
         try
         {
             name = new ObjectName("MyStdMBean:type=server,id=1");
             mbs.registerMBean(serverbean, name);
             System.out.println("Standard mbean registration successful : [Server]");
         }
         catch (Exception e)
         {
             System.out.println("Exception while registering standard mbean : [Server]");
             e.printStackTrace();
         }

         Overloaded overloadbean = new Overloaded(1, "password");
         Overloaded.enableLog();
         try
         {
             name = new ObjectName("MyStdMBean:type=overloaded");
             mbs.registerMBean(overloadbean, name);
             System.out.println("Standard mbean registration successful : [Overloaded]");
         }
         catch (Exception e)
         {
             System.out.println("Exception while registering standard mbean : [Overloaded]");
             e.printStackTrace();
         }
     }

	/**
	 *	The main method to start the JMXAgent.
	 */
     public static void main(String[] args)
     {
         RunStandardAgent run = new RunStandardAgent();
         run.createMBS();
         run.registerAdaptors();
         run.registerMBeans();
     }
 }