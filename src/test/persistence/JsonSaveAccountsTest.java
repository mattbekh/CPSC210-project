package persistence;

import model.Account;
import model.AccountManager;
import model.Collection;
import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JsonSaveAccountsTest {
    private JsonSaveAccounts jsonSaver;
    private AccountManager manager;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = AccountManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        // instantiating fresh manager
        manager = AccountManager.getInstance();
    }
    @Test
    void testInvalidFile() {
        try {
            jsonSaver = new JsonSaveAccounts("./data/my\0illegal:fileName.json");
            jsonSaver.open();
            fail("Exception expected");
        } catch (IOException e) {
            // expected
        }
    }
    @Test
    void testSaveNoAccounts() {
        try {
            jsonSaver = new JsonSaveAccounts("./data/testSaveNoAccounts.json");
            jsonSaver.open();
            jsonSaver.write(manager);
            jsonSaver.close();

            JsonLoadAccounts loader = new JsonLoadAccounts("./data/testSaveNoAccounts.json");
            loader.loadAccounts();
            // No accounts to be loaded
            assertEquals(0,manager.getMapSize());
            // success
        } catch (FileNotFoundException e) {
            fail("Exception thrown. Unable to find file");
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void testSaveGeneralAccounts() {
        loadManager();
        try {
            jsonSaver = new JsonSaveAccounts("./data/testSaveGeneralAccounts.json");
            jsonSaver.open();
            jsonSaver.write(manager);
            jsonSaver.close();

            JsonLoadAccounts loader = new JsonLoadAccounts("./data/testSaveGeneralAccounts.json");
            loader.loadAccounts();
            // 3 accounts to be loaded
            assertEquals(3,manager.getMapSize());
            // User2 has 2 collections
            assertEquals(2,manager.getAccount("TestAccount2","TestPassword")
                    .getCollections()
                    .size());
            // User1 -> Collection1 has 3 items
            assertEquals(3,
                    manager.getAccount("TestAccount2","TestPassword")
                            .getCollections()
                            .get(0)
                            .getItems()
                            .size());
            // success
        } catch (FileNotFoundException e) {
            fail("Exception thrown. Unable to find file");
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    // MODIFIES : this
    // EFFECTS  : Sets up a manager with general account data to test the saver
    private void loadManager() {
        Account testAccount2 = new Account("TestAccount2","TestPassword");
        Collection testCollection1 = new Collection("TestCollection1");
        testCollection1.addItem(new Item("hat","red","small"));
        testCollection1.addItem(new Item("pants","blue","medium"));
        testCollection1.addItem(new Item("shirt","brown","large"));
        testAccount2.addCollection(testCollection1);
        testAccount2.addCollection(new Collection("TestCollection2"));
        manager.addAccount(new Account("TestAccount","TestPassword"));
        manager.addAccount(testAccount2);
        manager.addAccount(new Account("TestAccount3","TestPassword"));
    }

}