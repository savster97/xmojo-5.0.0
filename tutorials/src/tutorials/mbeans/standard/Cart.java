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

package tutorials.mbeans.standard;

// Java imports
import java.util.*;
// ShoppingCart Application imports
import tutorials.application.shoppingcart.ShoppingCartApplication;
import tutorials.application.shoppingcart.ShoppingCart;


public class Cart implements CartMBean {

	private java.lang.Integer totalItemCount = null;
	private java.lang.Integer totalPrice = null;

	private ShoppingCart cart = null;

	public Cart() {
	}

	//////////////////// Attributes ////////////////////

	public java.lang.Integer getTotalItemCount() throws Exception {
    	cart = ShoppingCartApplication.getShoppingCartReference();
    	if(cart == null)
    	  throw new Exception("null reference got");
    	return new java.lang.Integer (cart.getTotalItemCount());
	}

	public java.lang.Integer getTotalPrice() throws Exception {
    	cart = ShoppingCartApplication.getShoppingCartReference();
    	if(cart == null)
    	  throw new Exception("null reference got");
    	return new java.lang.Integer (cart.getTotalPrice());
	}

	//////////////////// Operations ////////////////////

	public void addItem(java.lang.String param0, java.lang.Integer param1) throws Exception {
    	cart = ShoppingCartApplication.getShoppingCartReference();
		if(cart == null)
		  throw new Exception("null reference got");
		cart.addItem(param0,param1);
	}

	public boolean updateItem(java.lang.String param0, java.lang.Integer param1) throws Exception {
    	cart = ShoppingCartApplication.getShoppingCartReference();
		if(cart == null)
		  throw new Exception("null reference got");
		return cart.updateItem(param0,param1);
	}

	public java.util.Vector getItemsPurchased() throws Exception {
    	cart = ShoppingCartApplication.getShoppingCartReference();
		if(cart == null)
			throw new Exception("null reference got");
		java.util.List l = cart.getItemsPurchased();
        System.out.println("Items purchased :" + l);
		return (Vector)cart.getItemsPurchased();
	}

	public void reset() throws Exception {
    	cart = ShoppingCartApplication.getShoppingCartReference();
		if(cart == null)
			throw new Exception("null reference got");
		cart.reset();
	}

}
