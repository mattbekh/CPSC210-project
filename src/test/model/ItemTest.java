package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
    Test class for the Item class
 */

class ItemTest {

    @Test
    public void testConstructor() {
        Item testItem = new Item("hat","red","large");
        assertEquals(Item.ItemType.HAT,testItem.getItemType());
        assertEquals("red",testItem.getItemColor().toString().toLowerCase());
        assertEquals("large",testItem.getItemSize().toString().toLowerCase());
    }

    @Test
    public void testTypeEnums() {
        Item testItem = new Item("hat","red","large");
        Item testItem2 = new Item("pants","blue","medium");
        Item testItem3 = new Item("shirt","brown","small");

        assertEquals(Item.ItemType.HAT,testItem.getItemType());
        assertEquals(Item.ItemType.PANTS,testItem2.getItemType());
        assertEquals(Item.ItemType.SHIRT,testItem3.getItemType());
    }

    @Test
    public void testWrongInput() {
        assertThrows(IllegalArgumentException.class,
                ()-> new Item("hoodie","brown","medium"));

    }

    @Test
    public void testThrowsColorEnum() {
        assertThrows(IllegalArgumentException.class,
                ()-> new Item("hat","gold","medium"));
    }

    @Test
    public void testThrowsSizeEnum() {
        assertThrows(IllegalArgumentException.class,
                ()-> new Item("hat","brown","huge"));
    }
}