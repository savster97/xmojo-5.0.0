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

package examples.mbeans.standard;

/**
 * Definition of the management information of a Web Server.
**/
public class ServerInfo implements ServerInfoMBean
{
	private String serverName = null;

	private String serverId = null;

	private boolean serverStarted;

	private int port;

	// Constructors definitions

	public ServerInfo()
	{
		serverName = "test-server";
		serverId = "test-server_1";
		serverStarted = true;
		port = 8072;
	}

    public ServerInfo(String serverName, String serverId, boolean serverStarted,  int port)
    {
        this.serverName = serverName ;
        serverId = serverId ;
        serverStarted = serverStarted ;
        this.port = port ;
    }

	// Instrument the required changes to implement the ServerInfoMBean

	public String getServerName()
	{
		// get the ServerName
		return serverName;
	}

	public String getServerId()
	{
		// get the ServerIdentifier
		return serverId;
	}

	public boolean isServerStarted()
	{
		// check whether the server is started
		return serverStarted;
	}

	public int getPort()
	{
        // get the ServerPort
        return port;
    }

	public void setPort(int port)
	{

    	// set the ServerPort if the port is valid

        if ( !(port < 0 || port > 16000) )
        {
          	// stop the service
         	stopService();

            // change the port with the new value
            this.port = port;

            // start the service
            startService();
        }

    }

	public void startService()
	{
        // start the server
        System.out.println("Server started at port [" + port + "]");
        serverStarted = true;
    }

	public void stopService()
	{
        // stop the server
        System.out.println("Server running at port [" + port + "] stopped successfully");
        serverStarted = false;
    }

	// Additional methods not exposed for management

	public void restart()
	{
        stopService();
        startService();
    }
}