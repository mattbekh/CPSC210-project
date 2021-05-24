package persistence;

import model.Account;
import model.AccountManager;
import model.Collection;
import model.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/*
    Class which loads accounts & associated data from JSON data stored in file

    This code is loosely based off : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonLoadAccounts {
    private String file;

    // EFFECTS: constructs loader to load from source file
    public JsonLoadAccounts(String file) {
        this.file = file;
    }

    // EFFECTS: reads accounts + associated data from file
    // throws IOException if an error occurs reading data from file
    public void loadAccounts() throws IOException {
        String jsonFile = readFile(file);
        JSONObject jsonObject = new JSONObject(jsonFile);
        parseAccounts(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS  : parses account Objects from JSON array, calls method to store accounts in manager
    //          return not necessary because manager is a Singleton
    private void parseAccounts(JSONObject jsonObject) {
        AccountManager accountManager = AccountManager.getInstance();
        JSONArray jsonAccounts = jsonObject.getJSONArray("accounts");
        for (Object jsonAccount : jsonAccounts) {
            JSONObject account = (JSONObject) jsonAccount;
            addAccount(accountManager, account);
        }
    }

    // EFFECTS  : stores accounts in manager, for each account Object parses JSONArray for associated collections
    //          calls method to store collections per account
    //          return not necessary because manager is a Singleton
    private void addAccount(AccountManager accountManager, JSONObject account) {
        String accountName = account.getString("account");
        String accountPassword = account.getString("password");
        Account loadAccount = new Account(accountName, accountPassword);
        JSONArray jsonCollections = account.getJSONArray("collections");
        if (jsonCollections.length() > 0) {
            addCollections(loadAccount, jsonCollections);
        }
        accountManager.addAccount(loadAccount);
    }

    // EFFECTS  : parses JSONArray of collections for JSONObject collection, retrieves JSONArray of items
    //            then parses each JSONArray of items inside for items
    //            creates new item and stores to Collection, adds collection to Account
    private void addCollections(Account loadAccount, JSONArray jsonCollections) {
        for (Object jsonCollection : jsonCollections) {
            JSONObject collection = (JSONObject) jsonCollection;
            String collectionName = collection.getString("collection_name");
            Collection loadCollection = new Collection(collectionName);
            JSONArray jsonItems = collection.getJSONArray("collection_items");
            if (jsonItems.length() > 0) {
                for (Object jsonItem : jsonItems) {
                    JSONObject item = (JSONObject) jsonItem;
                    String itemType = item.getString("type");
                    String itemColor = item.getString("color");
                    String itemSize = item.getString("size");
                    Item newItem = new Item(itemType, itemColor, itemSize);
                    loadCollection.addItem(newItem);
                }
            }
            loadAccount.addCollection(loadCollection);
        }
    }
}
