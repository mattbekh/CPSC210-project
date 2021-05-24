package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/*
    Test class for the Collection class
 */

class CollectionTest {
    private Item item;
    private Item item2;
    private Item item3;

    @BeforeEach
    public void setup() {
        item = new Item("hat","red","large");
        item2 = new Item("pants","blue","small");
        item3 = new Item("shirt","brown","medium");
    }

    @Test
    public void testConstructor() {
        Collection collection = new Collection("TestCollection");
        assertEquals("TestCollection",collection.getName());
        assertTrue(collection.isEmpty());
        assertEquals("TestCollection",collection.toString());
    }

    @Test
    public void testAddSingleItem() {
        Collection collection = new Collection("TestCollection");
        collection.addItem(item);
        assertEquals(item,collection.getItem(0));
    }

    @Test
    public void testAddMultipleItems() {
        Collection collection = new Collection("TestCollection");
        collection.addItem(item);
        collection.addItem(item2);
        collection.addItem(item3);
        assertEquals(3,collection.getSize());
    }

    @Test
    public void testRemoveItem() {
        Collection collection = new Collection("TestCollection");
        collection.addItem(item);
        collection.addItem(item2);
        collection.removeItem(0);
        assertEquals(1,collection.getSize());
        assertEquals(item2,collection.getItem(0));
    }

    @Test
    public void testGetCollectionItems() {
        Collection collection = new Collection("TestCollection");
        collection.addItem(item);
        collection.addItem(item2);
        ArrayList<Item> test = collection.getItems();
        for(Item item : test) {
            assertTrue(collection.contains(item));
        }
    }
}