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

package tutorials.application.shoppingcart;

import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * This class holds the information of a Shopping Cart,
 * namely, the total price and the total no. of items purchased.
 * This class also provides methods for adding / updating / removing an item.
 */
public class ShoppingCart extends DefaultTableModel{

    // Collection of ShoppingItem objects.
    private List itemsPurchased = null;

    private int itemCount = 0;
    private int itemPrice = 0;

	private ShoppingCartApplication app = null;

    /**
     * Initializes the Shopping Cart.
     */
    public ShoppingCart() {
		super();
		addColumn("Pet Name");
		addColumn("Unit Cost");
		addColumn("Quantity");
		addColumn("List Price");
		addColumn("Image");
        itemsPurchased = new Vector();
    }

    /**
     * Adds an item into the Shopping Cart.
     * @param petName The name of the pet - Eg) Dog
     * @param quantity The quantity purchased.
     * @throw IllegalArgumentException - If any of the parameters passed is null.
     */
    public void addItem(String petName, Integer quantity) {

        if((petName == null) || (quantity == null)) {
            throw new IllegalArgumentException("null arguments passed for addItem");
        }
        if((!petName.equals("Parrot")) && (!petName.equals("Fish")) && (!petName.equals("Dog")) && (!petName.equals("Cat"))){
			throw new IllegalArgumentException("wrong arguments passed for addItem");
		}
        ShoppingItem item = new ShoppingItem(petName, quantity);
        Store.decreaseStock(petName, quantity);
        itemsPurchased.add(item);
        itemCount += quantity.intValue();
        itemPrice += item.getListPrice().intValue();
        addRow(toStringArray(petName));
        if (app != null){
			app.showSummary();
		}
    }

    /**
     * Updates an existing item in the Shopping Cart.
     * @param petName The name of the pet - Eg) Dog
     * @param quantity The quantity purchased.
     */
    public boolean updateItem(String petName, Integer quantity) {
        ShoppingItem item = getItem(petName);
        if(item != null) {
			int row = itemsPurchased.indexOf(new ShoppingItem(petName, null));
	        removeRow(row);
            itemPrice -= item.getListPrice().intValue();
            itemCount -= item.getQuantity().intValue();
            Store.increaseStock(petName, item.getQuantity());
            item.setQuantity(quantity);
            itemPrice += item.getListPrice().intValue();
            itemCount += quantity.intValue();
            Store.decreaseStock(petName, quantity);
            insertRow(row, toStringArray(petName));
	        if (app != null){
				app.showSummary();
			}
            return true;
        }
        return false;
  	}

    /**
     * Removes an item from the Shopping Cart identified by the index.
     * @param i The index of the pet - Eg) 1
     */
    public boolean removeItemAt(int i) {
        ShoppingItem item = null;
        try {
            item = (ShoppingItem)itemsPurchased.get(i);
        } catch(IndexOutOfBoundsException iobe) {
            return false;
        }
        return remove(item);
    }

    /**
     * Removes an item from the Shopping Cart.
     * @param petName The name of the pet - Eg) Dog
     */
    public boolean removeItem(String petName) {
        ShoppingItem item = getItem(petName);
        return remove(item);
    }

    /**
     * Removes the Item and recalculates the item count and price.
     */
    private boolean remove(ShoppingItem item) {
        if(item == null) {
            return false;
        }
        removeRow(itemsPurchased.indexOf(item));
        itemsPurchased.remove(item);
        itemCount -= item.getQuantity().intValue();
        itemPrice -= item.getListPrice().intValue();
        Store.increaseStock(item.getPetName(), item.getQuantity());
        if (app != null){
			app.showSummary();
		}
        return true;
    }

    /**
     * This method returns the ShoppingItem identified by the index.
     * @param index The index of the pet - Eg) 1
     * @return The ShoppingItem object; null if not found.
     */
    public ShoppingItem getItemAt(int index) {
        try {
            return (ShoppingItem)itemsPurchased.get(index);
        } catch(IndexOutOfBoundsException iobe) {
            return null;
        }
    }

    /**
     * This method returns the ShoppingItem identified by the name.
     * @param petName The name of the pet which uniquely identifies this row.
     * @return The ShoppingItem object; null if not found.
     */
    public ShoppingItem getItem(String petName) {
        int i = itemsPurchased.indexOf(new ShoppingItem(petName, null));
        if(i != -1) {
            ShoppingItem item = (ShoppingItem)itemsPurchased.get(i);
            return item;
        }
        return null;
    }

    /**
     * This method returns the list of items purchased.
     * Each element in this list is an object of type ShoppingItem.
     * This list should not be changed with the reference returned.
     */
    public List getItemsPurchased() {
        return itemsPurchased;
    }

    /**
     * This method returns the Total Item Count present in this shopping cart.
     * @return The Total Item Count added in the shopping cart.
     */
    public int getTotalItemCount() {
        return itemCount;
    }

    /**
     * This method returns the Total Price of the items in this shopping cart.
     * @return The Total Price of items added in the shopping cart.
     */
    public int getTotalPrice() {
        return itemPrice;
    }

    /**
     * Empties the items in the Shopping Cart.
     */
    public void reset() {
        itemsPurchased.clear();
        Store.resetInventory();
        itemCount = 0;
        itemPrice = 0;
    }

    /**
     * Returns a Object[] which reflects the information of the Item purchased.
     */
    public Object[] toStringArray(String petName) {
        ShoppingItem ii = getItem(petName);
        Object[] toRet = new Object[5];
        if(ii == null) {
            return toRet;
        }
        toRet[0] = ii.getPetName();
        toRet[1] = ii.getUnitCost();
        toRet[2] = ii.getQuantity();
        toRet[3] = ii.getListPrice();
        toRet[4] = Store.getImageName(ii.getPetName());
        return toRet;
    }

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void clearAll() {
		reset();
		for(int i=(getRowCount() - 1); i>=0; i--) {
			removeRow(i);
		}
	}

	public void setAppRef(ShoppingCartApplication app){
		this.app = app;
	}

}
