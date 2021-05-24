package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/*
    Test class for the Account class
 */

class AccountTest {
    private Account myAccount;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = AccountManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        myAccount = new Account("test","testPassword");
    }

    @Test
    public void testConstructor() {
        assertEquals("test", myAccount.getName());
        assertEquals("testPassword",myAccount.getPassword() );
    }

    @Test
    public void testAddCollection() {
        Collection collection = new Collection("TestCollection");
        myAccount.addCollection(collection);
        ArrayList<Collection> testCollections = myAccount.getCollections();
        assertFalse(testCollections.isEmpty());
        assertEquals("TestCollection", testCollections.get(0).getName());
    }

    @Test
    public void testUpdateCollection() {
        Collection collection = new Collection("TestCollection");
        myAccount.addCollection(collection);
        Collection updatedCollection = new Collection("UpdatedCollection");
        myAccount.updateCollection(updatedCollection,0);
        assertEquals("UpdatedCollection", myAccount.getCollections().get(0).getName());
    }

    @Test
    public void testRemoveCollection() {
        Collection collection = new Collection("TestCollection");
        myAccount.addCollection(collection);
        assertFalse(myAccount.getCollections().isEmpty());
        myAccount.removeCollection(collection);
        assertTrue(myAccount.getCollections().isEmpty());
    }
}