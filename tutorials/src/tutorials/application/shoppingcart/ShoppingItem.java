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

/**
 * This class holds information of an Item purchased by the customer.
 * The information about the pet purchased such as the name, unit cost,
 * quantity and list price are stored in this class.
 */
public class ShoppingItem {

    private String petName = null;
    private Integer unitCost = null;
    private Integer quantity = null;
    private Integer listPrice = null;

	public ShoppingItem() {
	}

    /**
     * Initializes the ShoppingItem with the parameters.
     * @param petName The name of the pet - Eg) Dog
     * @param quantity The quantity purchased.
     */
    public ShoppingItem(String petName, Integer quantity) {
        this.petName = petName;
        this.unitCost = Store.getUnitPrice(petName);
        this.quantity = quantity;
        if(unitCost != null &&
           quantity != null) {
            this.listPrice = new Integer(unitCost.intValue() * quantity.intValue());
        }
    }

    /**
     * Getter for the Pet Name. Eg) Parrot.
     * @return The name of the Pet as a String
     */
    public String getPetName() {
         return this.petName;
    }

    /**
     * Getter for the Unit Cost of this pet.
     * @return The Unit Cost in dollars
     */
    public Integer getUnitCost() {
         return this.unitCost;
    }

    /**
     * Getter for the quantity of this pet purchased.
     * @return Quantity as an Integer
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * Setter for the quantity of this pet purchased.
     * @param Quantity as an Integer
     */
    public void setQuantity(Integer qnty) {
         this.quantity = qnty;
         if(unitCost != null &&
            quantity != null) {
             this.listPrice = new Integer(unitCost.intValue() * quantity.intValue());
         }
    }

    /**
     * Getter for the List Price of this item. This is the product
     * of Unit Cost and the quantity purchased.
     * @return ListPrice in dollars.
     */
    public Integer getListPrice() {
         return this.listPrice;
    }

    /**
     * Compares this object with the Object o specified.
     * @return true if the pet names are same; false otherwise.
     *         The petname uniquely identifies the purchases.
     */
    public boolean equals(Object o) {
        if(o instanceof ShoppingItem) {
            ShoppingItem i = (ShoppingItem)o;
            return petName.equalsIgnoreCase(i.getPetName());
        } else {
            return false;
        }
    }

    /**
     * Overriding the {@link java/lang/Object.html#toString toString()} method.
     * @return human readable String representation of this object.
     */
    public String toString() {
        // System.out.println("Inside toString method...");
        return new String(petName);
    }

}
