package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/*
    Test class for the AccountManager class
 */

class AccountManagerTest {
    private Account myAccount;
    private AccountManager manager;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        myAccount = new Account("TestAccount","testPassword");
        Field instance = AccountManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        manager = AccountManager.getInstance();
    }

    @Test
    public void testConstructorNotInstantiated() {
        manager.addAccount(myAccount);
        assertEquals(myAccount,manager.getAccount("TestAccount","testPassword"));
        assertNull(manager.getAccount("TestAccount","WrongPassword"));
    }

    @Test
    public void testConstructorInstantiated() {
        manager.addAccount(myAccount);
        AccountManager manager2 = AccountManager.getInstance();
        Account myAccount2 = new Account("TestAccount2","TestPassword2");
        manager2.addAccount(myAccount2);
        // Maintains knowledge of existing myAccount
        assertEquals(myAccount,manager2.getAccount("TestAccount","testPassword"));
        // Stores second account as well
        assertEquals(myAccount2,manager2.getAccount("TestAccount2","TestPassword2"));
        // Total accounts stored should be 2
        assertEquals(2,manager2.getMapSize());
    }

    @Test
    public void testDestruct() {
        manager.addAccount(myAccount);
        assertEquals(myAccount,manager.getAccount("TestAccount","testPassword"));
        manager.destruct();
        assertNull(manager.getAccount("TestAccount","testPassword"));
    }

    @Test
    public void testClearMap() {
        manager.addAccount(myAccount);
        assertEquals(1,manager.getMapSize());
        manager.clearMap();
        assertEquals(0,manager.getMapSize());
    }

    @Test
    public void testIsMemberSingle() {
        manager.addAccount(myAccount);
        assertTrue(manager.isMember(myAccount.getName()));
    }

    @Test
    public void testIsMemberMultiple() {
        manager.addAccount(myAccount);
        Account myAccount2 = new Account("TestAccount2","TestPassword2");
        Account myAccount3 = new Account("TestAccount3","TestPassword3");
        manager.addAccount(myAccount2);
        manager.addAccount(myAccount3);
        assertTrue(manager.isMember(myAccount2.getName()));
    }
}