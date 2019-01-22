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

 package tutorials.agent;

 import javax.management.MBeanServerFactory;
 import javax.management.MBeanServer;
 import javax.management.ObjectName;
 import com.adventnet.adaptors.rmi.RMIAdaptor;
 import com.adventnet.adaptors.html.HtmlAdaptor;
import com.adventnet.adaptors.html.JettyHtmlServer;
 import javax.management.monitor.GaugeMonitor;
 import com.adventnet.agent.logging.*;

 import tutorials.mbeans.dynamic.InventoryManager;
 import tutorials.mbeans.standard.Cart;
 import tutorials.application.shoppingcart.ShoppingCartApplication;

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


 public class RunAgent
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

        private HtmlAdaptor htmladaptor = null;
        private String htmlRootDir = ".";
        private int htmlPort = 8030; // HTML Adaptor Port will be overridden by the port value
                                    // in %htmlRootDir%/conf/http/etc/JettyConfig.xml file.

		private int loggingLevel = Level.FATAL;

     public RunAgent()
     {
		 // setting Logging Level
		 LogFactory.setLoggingLevel(loggingLevel);
     }

        /**
         * This method creates the Server Repository for the JMX Agent.
         */
        private void createMBS() {
                try {
                        mbs = MBeanServerFactory.createMBeanServer("myDomain");
                        System.out.println("MBeanServer instance creation successful");
                } catch (Exception e) {
                        System.out.println("MBeanServer instance creation failed");
                        e.printStackTrace();
                }
        }

        /**
         * This method registers the adaptors for the JMXAgent.
         */
        private void registerAdaptors() {
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
                        mbs.registerMBean(rmiadaptor, name);
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
                        mbs.registerMBean(htmladaptor, name);
                } catch (Exception e) {
                        System.out.println("Exception while initializing HTML adaptor");
                        e.printStackTrace();
                }
        }

        /**
         * This method creates and registers the StandardMBeans and DynamicMBeans with the MBeanServer.
         */
     private void registerMBeans()
     {
         Cart cart = new Cart();
         try
         {
             name = new ObjectName("ShoppingCart:type=MBean");
             mbs.registerMBean(cart, name);
             System.out.println("Standard mbean : [CartMBean] registration successful");
         }
         catch (Exception e)
         {
             System.out.println("Exception while registering standard mbean : [CartMBean]");
             e.printStackTrace();
         }

         InventoryManager imp = new InventoryManager("Parrot");
         try
         {
             name = new ObjectName("MBean:type=Inventory,name=Parrot");
             mbs.registerMBean(imp, name);
             System.out.println("Dynamic mbean : [InventoryMBean~Parrot] registration successful");
         }
         catch (Exception e)
         {
             System.out.println("Exception while registering dynamic mbean : [InventoryMBean~Parrot]");
             e.printStackTrace();
         }

         InventoryManager imf = new InventoryManager("Fish");
         try
         {
             name = new ObjectName("MBean:type=Inventory,name=Fish");
             mbs.registerMBean(imf, name);
             System.out.println("Dynamic mbean : [InventoryMBean~Fish] registration successful");
         }
         catch (Exception e)
         {
             System.out.println("Exception while registering dynamic mbean : [InventoryMBean~Fish]");
             e.printStackTrace();
         }
     }

        /**
         * This method registers a GaugeMonitor Service MBean to the server MBean.
         */
     public void registerServices()
     {
         try
         {
			 GaugeMonitor gm = new GaugeMonitor( );
             name = new ObjectName("Services:type=GaugeMonitor");
             gm.setObservedObject(new ObjectName("InventoryMBean:Pet=Parrot"));
             gm.setObservedAttribute("InventoryLevel");
             gm.setGranularityPeriod(5000);
             gm.setDifferenceMode(false);
             gm.setNotifyHigh(false);
             gm.setNotifyLow(true);
             gm.setThresholds(new Integer(50), new Integer(5));
             mbs.registerMBean(gm , name);
             gm.start();
         }
         catch (Exception e)
         {
            System.out.println("Exception while registering service mbean : [GaugeMonitor]");
            e.printStackTrace();
         }
     }

     public static void main(String[] args)
     {
         RunAgent run = new RunAgent();
         Thread th = new CartInvoker(args);
         th.start();
         try
         {
             Thread.sleep(2000);
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
         run.createMBS();
         run.registerAdaptors();
         run.registerMBeans();
         run.registerServices();
     }
 }

 class CartInvoker extends Thread {
     private String[] args = null;
     CartInvoker(String[] args) {
         this.args = args;
     }
     public void run() {
         try
         {
            ShoppingCartApplication.main(args);
         }
         catch (Throwable th)
         {
             th.printStackTrace();
         }
     }

 }
