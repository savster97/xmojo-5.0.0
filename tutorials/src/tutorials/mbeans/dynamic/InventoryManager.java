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

package tutorials.mbeans.dynamic;

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


import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import tutorials.application.shoppingcart.*;

public class InventoryManager implements DynamicMBean,MBeanRegistration
{
    // Variable declarations
	private String itemName = null;

	AttributeList alist = new AttributeList( );

	MBeanAttributeInfo[] attribInfoArray = null;

	MBeanOperationInfo[] opInfoArray = null;

	MBeanParameterInfo[] paramInfoArray = null;

	MBeanConstructorInfo[] consInfoArray = null;

	MBeanNotificationInfo[] notifInfoArray = null;

	ShoppingCart cart = null;

	// Constructor definition

    public InventoryManager(String name)
    {
		itemName = name;
    }

	// DynamicMBean implementation for getMBeanInfo

	public MBeanInfo getMBeanInfo( )
	{
		MBeanInfo minfo = null;

		try
		{
			// constructing MBean constructors

			constructConstructors( );

			// constructing MBean attributes

			constructAttributes( );

			// constructing MBean operations

			constructOperations( );

			// construct MBean Notifications

			constructNotifications( );

			// constructing the MBean information

			minfo = new MBeanInfo("InventoryManager", "Dynamic MBean for managing the Inventory information",
			                        attribInfoArray, consInfoArray, opInfoArray, notifInfoArray);

		}
		catch(IllegalArgumentException ex)
		{
			ex.printStackTrace( );
		}

		return minfo;
	}

	// DynamicMBean implementation for getAttribute

	public Object getAttribute(String attributeName)
	                throws AttributeNotFoundException ,
	                       MBeanException ,
	                       ReflectionException
	{
    	//System.out.println("getAttribute called with attribute name : " + attributeName);

        try
		{
    		//System.out.println("Inside try block of getAttribute @@");

			if ( attributeName.equals("InventoryLevel") )
			{
				return Store.getInventoryLevel(itemName);
			}

			else if ( attributeName.equals("UnitCost") )
			{
				return Store.getUnitPrice(itemName);
			}

			else if ( attributeName.equals("Name") )
			{
				return itemName;
			}

			else if ( attributeName.equals("Purchased") )
			{
    			//System.out.println("Going to get ShoppingCartReference...");
                cart = ShoppingCartApplication.getShoppingCartReference();
                //System.out.println("After Getting ShoppingCartReference...");
    			if (cart == null)
    			{
        			//System.out.println("Cart is null ! ");
                    return new Integer(0);
                }
                else
                {
                    //System.out.println("Cart is not null. ");
                    ShoppingItem item = cart.getItem(itemName);
                    if (item == null)
                    {
                        //System.out.println("Item is null...");
                        return new Integer(0);
                    }
                    //System.out.println("Items purchased are :"+item.getQuantity());
                    return item.getQuantity();
                }
			}

			else
			{
    			//System.out.println("  @@ ["+attributeName+"] not found");
				throw new AttributeNotFoundException("The specified attribute does not exist");
			}
		}
		catch(RuntimeException e)
		{
    		//System.out.println("RuntimeException caught inside catch block of getAttribute and thrown");
    		e.printStackTrace();
			throw new RuntimeOperationsException( e, e.getMessage() );
		}
		catch(Exception e)
		{
    		//System.out.println("Exception caught inside catch block of getAttribute");
			e.printStackTrace( );
		}

		return null;

	}

	// Dynamic MBean implementation for setAttribute

	public void setAttribute(Attribute attrib)
	              throws AttributeNotFoundException ,
	                     InvalidAttributeValueException ,
	                     MBeanException ,
	                     ReflectionException
	{
		String attributeName = attrib.getName( );
		Object attributeValue = attrib.getValue( );

		//System.out.println("Inside setAttribute(new Attribute("+attributeName+", "+attributeValue+")");

		try
		{
    		//System.out.println("Inside try block of setAttribute **");
            //System.out.println("  ** ["+attributeName+"] does not exist");
			throw new AttributeNotFoundException("The specified attribute does not exist");
		}
		catch(Exception e)
		{
    		System.out.println("Exception caught inside catch block of setAttribute");
			e.printStackTrace( );
		}

	}

	// Dynamic MBean implementation of getAttributes

	public AttributeList getAttributes(String[] attribs)
	{
    	//System.out.println("Inside getAttributes ....");

		AttributeList attribList = new AttributeList( );

		for (int index = 0; index < attribs.length ; index ++ )
		{
			try
			{
				Object obj = getAttribute(attribs[index]);
				Attribute a = new Attribute(attribs[index], obj);
				attribList.add(a);
			}
			catch(Exception e)
			{
				// Exception should not be thrown to the caller.
				System.out.println("Exception caught while performing getAttributes for attribute :: [" + attribs[index] + "]");
			}
		}

		return attribList;
	}

	// Dynamic MBean implementation of setAttributes

	public AttributeList setAttributes(AttributeList attribList)
	{
    	//System.out.println("Inside setAttributes ...");

		AttributeList newList = new AttributeList( );

		for (int index = 0; index < attribList.size( ) ; index ++ )
		{
			try
			{
				Attribute attr = (Attribute) attribList.get(index);
				setAttribute(attr);
				newList.add(attr);
			}
			catch(Exception e)
			{
				// Exception should not be thrown to the caller.
				System.out.println("Exception caught while performing setAttributes for attribute :: [" + ((Attribute)attribList.get(index)).getName() + " with the value :: [" + ((Attribute)attribList.get(index)).getValue() + "]" );
			}
		}

		return newList;
	}

	// Dynamic MBean implementation of invoke

	public Object invoke(String actionName, Object[] params, String[] signature)
	               throws MBeanException ,
	                      ReflectionException
	{
    	//System.out.println("Invoke called with action name : " + actionName);

		Method m = null;

		int len = 0;

		if (signature != null)
		{
           len = signature.length;
        }

        Class[] sig = new Class[len];

		try
		{
    		//System.out.println("Inside try block of invoke ##");

    		for (int index = 0; index < len; index ++ )
            {
                sig[index] = Class.forName( signature[index] );
            }

			if ( actionName.equals("getInventoryLevel") || actionName.equals("getUnitCost")
					|| actionName.equals("getPurchased")	|| actionName.equals("getName")
					|| actionName.equals("increaseStock") )
			{
    			//System.out.println("  ## ["+actionName+"] matches the action name ~~ Going to apply conditions");
				if ( actionName.equals("increaseStock") && !checkInventoryLevel() )
				{
    				//System.out.println("  ## Operation : ["+actionName+"] cannot be invoked");
					throw new IllegalArgumentException("Operation : ["+actionName+"] cannot be invoked.");
				}
				else
				{
    				//System.out.println("  ## ["+actionName+"] Passed all the conditions");
					m = InventoryManager.class.getMethod(actionName, sig);

					if ( m == null )
					{
    					//System.out.println("  ## Unable to invoke ["+actionName+"] using Java Reflection");
						throw new ReflectionException(new Exception("Problem while invoking the method"),"Method signature not specified properly");
					}
					else
					{
    					//System.out.println("  ## ["+actionName+"] to be invoked");
						return m.invoke(this, params);
					}
				}
			}
			else
			{
    			//System.out.println("  ## ["+actionName+"] No matches found");
				throw new RuntimeOperationsException(new RuntimeException("Specified action is not found."), "Kindly check the actionName string passed");
			}
		}
		catch(Exception e)
		{
    		  //System.out.println("  ## Exception caught inside catch block of invoke");
              e.printStackTrace();
        }
		return null;
	}

	public void constructConstructors( ) throws IllegalArgumentException
	{
		consInfoArray = new MBeanConstructorInfo[1];

		// constructing the parameters of the constructor

		paramInfoArray = new MBeanParameterInfo[1];

		paramInfoArray[0] = new MBeanParameterInfo("ItemName", "java.lang.String", "Name of the item");

		consInfoArray[0] = new MBeanConstructorInfo("InventoryManager", "One argument constructor", paramInfoArray);

	}

	public void constructAttributes( ) throws IllegalArgumentException
	{

		attribInfoArray = new MBeanAttributeInfo[4];

    	// System.out.println("Constructing Attributes");

		attribInfoArray[0] = new MBeanAttributeInfo("Name", "java.lang.String", "Name of the pet",
		                                                 true, false, false);

		attribInfoArray[1] = new MBeanAttributeInfo("UnitCost", "java.lang.Integer", "Cost of one pet",
		                                                 true, false, false);

		attribInfoArray[2] = new MBeanAttributeInfo("InventoryLevel", "java.lang.Integer", "Inventory Level of the pet",
		                                                 true, false, false);

		attribInfoArray[3] = new MBeanAttributeInfo("Purchased", "java.lang.Integer", "Number of pets sold out",
		                                                 true, false, false);

	}

	public void constructOperations( ) throws IllegalArgumentException
	{
		if ( checkInventoryLevel() )
		{
    		opInfoArray = new MBeanOperationInfo[1];

    		//System.out.println("Constructing operations ~~ increaseStock for ["+itemName+"]");

		    // constructing the parameters of the constructor

		    paramInfoArray = new MBeanParameterInfo[1];

		    paramInfoArray[0] = new MBeanParameterInfo("ItemQuantity", "java.lang.Integer", "Quantity of the item");

			opInfoArray[0] = new MBeanOperationInfo("increaseStock", "To increase the Inventory Level of "+itemName,
		                                            paramInfoArray, "void", MBeanOperationInfo.ACTION);
		}
		else
		{
    		opInfoArray = new MBeanOperationInfo[0];

    		//System.out.println("No Operation constructed");
		}
	}

	public boolean checkInventoryLevel()
	{
    	//System.out.println("Checking inventory level");
    	try
        {
            //int i = (Store.getInventoryLevel(itemName)).intValue() - ((Integer)getAttribute("Purchased")).intValue() );
            int i = (Store.getInventoryLevel(itemName)).intValue();
            //System.out.println("The value of InventoryLevel is : ["+i+"]");
            if ( i  <  5 )
            return true;
            else
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void increaseStock(Integer quantity) {
		Store.increaseStock(itemName, quantity);
	}

	public void constructNotifications( ) throws IllegalArgumentException
	{
		notifInfoArray = new MBeanNotificationInfo[0];
	}

    public ObjectName preRegister(MBeanServer mbs, ObjectName obj_name) throws Exception
    {
		System.out.println("Inside preDeregister");
        return new ObjectName("InventoryMBean:Pet="+itemName);
    }

    public void postRegister(Boolean result)
    {
        System.out.println("Inside postRegister : ["+result+"]");
    }

    public void preDeregister() throws Exception
    {
        System.out.println("Inside preDeregister");
    }

    public void postDeregister()
    {
        System.out.println("Inside postDeregister");
    }
}
