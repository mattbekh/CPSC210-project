package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;

/*
    Class that manages all the accounts associated with saved collections, different users may "log in" and
    retrieve their saved collections.
 */
public class AccountManager implements Writable {

    private Map<String, Account> hashMap;
    private static AccountManager instance;

    // Singleton Support

    // MODIFIES : this
    // EFFECTS : Maintains memory of existent account manager and its' stored accounts by
    //           checking if it has been instantiated in the past, keeping track of instance
    //           and returning the instance. If not, returning new manager
    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    // Private "constructor" for Singleton

    // MODIFIES : this
    // EFFECTS : Creates new empty hash map to store data (all the user accounts)
    private AccountManager() {
        hashMap = new HashMap<>();
    }

    // Public destructor for Singleton

    // MODIFIES : this
    // EFFECTS  : Destroys singleton
    public void destruct() {
        hashMap.clear();
        instance = null;
    }

    // This code is based off : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONArray jsonAccounts = new JSONArray();
        int i = 0;
        for (Account account : hashMap.values()) {
            jsonAccounts.put(i,account.toJson());
            i++;
        }
        JSONObject obj = new JSONObject();
        obj.put("accounts",jsonAccounts);
        return obj;
    }

    // REQUIRES : account != null
    // MODIFIES : this
    // EFFECTS  : Stores input account into hash map, using name as key
    public void addAccount(Account account) {
        hashMap.put(account.getName(), account);
    }

    // REQUIRES : account name not empty, password not empty
    // MODIFIES : this
    // EFFECTS  : As long as there is an instance of a manager (hasn't been destructed)
    //            if accountName matches an account stored, and the retrieved accounts' stored password matches the
    //            input password
    //                  - return that account
    //            otherwise return null
    public Account getAccount(String accountName, String password) {
        if (instance != null) {
            if (hashMap.get(accountName).getPassword().equals(password)) {
                return hashMap.get(accountName);
            } else {
                return null;
            }
        }
        return null;
    }

    // REQUIRES : name not empty
    // EFFECTS  : returns boolean value if an account name is stored in hash map
    public Boolean isMember(String name) {
        return hashMap.containsKey(name);
    }

    // MODIFIES : this
    // EFFECTS  : clears all keys and values from hash map
    public void clearMap() {
        hashMap.clear();
    }

    public int getMapSize() {
        return hashMap.size();
    }
}
