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
 * Definition of the Simple Interface of a StandardMBean that
 * explains the attribute name overloading
**/

public class Overloaded implements OverloadedMBean
{
    private int state;

    private String pwd;

    private static boolean log;

    // Constructor definition

    public Overloaded(int state, String pwd)
    {
        this.state = state;
        this.pwd = pwd;
    }

    // Implementation details of the StandardMBean interface.

    public int getState()
    {
        return state;
    }

    public int getState(int value)
    {
        state = value;
        return state;
    }

    public void setPassword(String pwd)
    {
        this.pwd = pwd;
    }

    public void setPassword(String pwd, String newpwd)
    {
        if (log)
        {
            System.out.println("Inside setPassword("+pwd+", "+newpwd+")");
        }

        if ( pwd.equals(this.pwd) )
        {
             this.pwd = newpwd;
             if (log)
             {
                 System.out.println("Password has been changed. New value : [" + newpwd + "]. Old value : [" + pwd + "].");
             }
         }

         else
         {
             if (log)
             {
                 System.out.println("setPassword invoked with improper value. Existing password is provided with value : [" + pwd + "]");
             }
         }
    }

    public static void enableLog()
    {
        log = true;
    }

    public static void disableLog()
    {
        log = false;
    }
}