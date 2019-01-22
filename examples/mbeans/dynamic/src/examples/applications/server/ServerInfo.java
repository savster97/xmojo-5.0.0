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

package examples.applications.server;

import java.util.Date;

/**
 * A simple class for obtaining the information of a
 * Web Server.
**/

public class ServerInfo {

        ///////////// Variables declaration /////////////

        private String serverName = null;

        private String serverId = null;

        private boolean serverStarted = true;

        private int port = 8072;

        private Date startTime = new Date();

        private long serverUpTime;

        private int restartCount;

        private boolean flag = false;

        ///////////// Constructor definitions /////////////

        public ServerInfo() {
                serverName = "test-server";
                serverId = "test-server_1";
                serverStarted = true;
                port = 8072;
        }

        public ServerInfo(String serverName, String serverId,
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
                this.port = port;
                startService();
        }

        public void startService() {
                if (serverStarted) {
                        System.out.println("Server already started " +
                                getServerUpTime() +
                                " milli seconds back at " + getPort());
                        return;
                }

                System.out.println("Starting server at port : [" + port +"]");
                serverStarted = true;
                startTime = new Date();
        }

        public void stopService() {
                if (serverStarted == false) {
                        System.out.println("Server already stopped");
                        return;
                }

                System.out.println("Server running at port : ["+port +"] halted");
                serverStarted = false;
        }

        public Date getStartTime() {
                return startTime;
        }

        public long getServerUpTime() {
			    if (serverStarted == false) {
					return 0l;
                }
                else {
					return (System.currentTimeMillis() - startTime.getTime());
				}
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

}

