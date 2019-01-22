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

import java.util.*;

/**
 * This class holds the quantity of items
 * remaining in each category.
 */
public class Store {

    public final static String PARROT = "Parrot";
    public final static String FISH = "Fish";
    public final static String DOG = "Dog";
    public final static String CAT = "Cat";

    private final static Integer DEFAULT_INVENTORY_LEVEL = new Integer(20);

    private static Integer parrot_inventory = DEFAULT_INVENTORY_LEVEL;
    private static Integer fish_inventory = DEFAULT_INVENTORY_LEVEL;
    private static Integer dog_inventory = DEFAULT_INVENTORY_LEVEL;
    private static Integer cat_inventory = DEFAULT_INVENTORY_LEVEL;

    private static Integer parrot_price = new Integer(20); // $20
    private static Integer fish_price = new Integer(5); // $5
    private static Integer dog_price = new Integer(25); // $25
    private static Integer cat_price = new Integer(20); // $20

    private static String parrot_image = "parrot.png";
    private static String fish_image = "fish.png";
    private static String dog_image = "dog.png";
    private static String cat_image = "cat.png";

    public static int PET_COUNT = 4;

    private static List items = null;
    private static Map priceTable = null;
    private static Map inventoryTable = null;
    private static Map imageTable = null;

    static {
        inventoryTable = new Hashtable(PET_COUNT);
        resetInventory();

        priceTable = new Hashtable(PET_COUNT);
        priceTable.put(new StringWrapper(PARROT), parrot_price);
        priceTable.put(new StringWrapper(FISH), fish_price);
        priceTable.put(new StringWrapper(DOG), dog_price);
        priceTable.put(new StringWrapper(CAT), cat_price);

        imageTable = new Hashtable(PET_COUNT);
        imageTable.put(new StringWrapper(PARROT), parrot_image);
        imageTable.put(new StringWrapper(FISH), fish_image);
        imageTable.put(new StringWrapper(DOG), dog_image);
        imageTable.put(new StringWrapper(CAT), cat_image);

        items = new ArrayList(PET_COUNT);
        items.add(PARROT);
        items.add(FISH);
        items.add(DOG);
        items.add(CAT);
    }

    /**
     * Returns the list of items available in the store.
     */
    public static List getItems() {
        return items;
    }

    /**
     * This method returns the inventory level of a particular category.
     * @param category The pet category - eg) Fish
     * @return The remaining quantity of this item.
     */
    public static Integer getInventoryLevel(String category) {
        return (Integer)inventoryTable.get(new StringWrapper(category));
    }

    /**
     * This method returns the unit price of a particular category.
     * @param category The pet category - eg) Fish
     * @return The unit price of this item.
     */
    public static Integer getUnitPrice(String category) {
        return (Integer)priceTable.get(new StringWrapper(category));
    }

    /**
     * This method returns the image name for a particular category.
     * @param category The pet category - eg) Fish
     * @return The image name of this item.
     */
    public static String getImageName(String category) {
        return (String)imageTable.get(new StringWrapper(category));
    }

    /**
     * This method is used to purchase some quantity of a particular item.
     * The inventory will be filled with the quantity purchased.
     * @param category The pet category to be purchased. Eg) Dog.
     * @param quantity The quantity of item to be purchased.
     * @throw IllegalArgumentException - If either the category or quantity is null.
     */
    public static void increaseStock(String category, Integer quantity) {
        if(category == null || quantity == null) {
            throw new IllegalArgumentException("null arguments passed for increaseStock");
        }
        Integer qnty = getInventoryLevel(category);
        int q;
        if(qnty == null) {
            q = 0;
        } else {
            q = qnty.intValue();
        }
        inventoryTable.put(new StringWrapper(category),
                           new Integer(q + quantity.intValue()));
    }

    /**
     * This method is used to decrement the inventory level. This would
     * be invoked when some purchase has been made.
     * @param category The pet category to be purchased. Eg) Dog.
     * @param quantity The quantity of item to be purchased.
     * @throw IllegalArgumentException - If either the category or quantity is null.
     */
    public static void decreaseStock(String category, Integer quantity) {
        if(category == null || quantity == null) {
            throw new IllegalArgumentException("null arguments passed for decreaseStock");
        }
        Integer qnty = getInventoryLevel(category);
        int q;
        if(qnty == null) {
            q = 0;
        } else {
            q = qnty.intValue();
        }
        inventoryTable.put(new StringWrapper(category),
                           new Integer(q - quantity.intValue()));
    }

    /**
     * This method is used to reset the inventory with the default stocks.
     */
    public static void resetInventory() {
        inventoryTable.put(new StringWrapper(PARROT), parrot_inventory);
        inventoryTable.put(new StringWrapper(FISH), fish_inventory);
        inventoryTable.put(new StringWrapper(DOG), dog_inventory);
        inventoryTable.put(new StringWrapper(CAT), cat_inventory);
    }

}
