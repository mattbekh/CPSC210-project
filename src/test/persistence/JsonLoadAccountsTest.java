package persistence;

import model.AccountManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JsonLoadAccountsTest {
    private JsonLoadAccounts jsonLoader;
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
    void testLoaderNonExistentFile() {
        jsonLoader = new JsonLoadAccounts("./data/noFile.json");
        try {
            jsonLoader.loadAccounts();
            fail("Exception should be thrown");
        } catch (IOException e) {
            // expected
        }
    }
    @Test
    void testLoadNoAccounts() {
        jsonLoader = new JsonLoadAccounts("./data/testLoadNoAccounts.json");
        try {
            jsonLoader.loadAccounts();
            // success, manager should be loaded with no accounts
            assertEquals(0,manager.getMapSize());
        } catch (IOException e) {
            fail("Exception thrown. Unable to read from file");
        }

    }

    @Test
    void testLoadGeneralAccounts() {
        jsonLoader = new JsonLoadAccounts("./data/testLoadGeneralAccounts.json");
        try {
            jsonLoader.loadAccounts();
            // success, manager should be loaded with many accounts + associated data
            // 3 accounts stored in json, should have 3 in manager
            assertEquals(3,manager.getMapSize());
            // User2 has 2 collections
            assertEquals(2,manager.getAccount("User2","password2").getCollections().size());
            // User1 -> Collection1 has 3 items
            assertEquals(3,
                    manager.getAccount("User1","password2")
                            .getCollections()
                            .get(0)
                            .getItems()
                            .size());
        } catch (IOException e) {
            fail("Exception thrown. Unable to read from file");
        }
    }
}