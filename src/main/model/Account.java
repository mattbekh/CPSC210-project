package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

/*
    Class for an individual account, keeps track of saved collections per user along with
    their name and accountId. User is able to retrieve old collections and modify, or create
    new collections.
 */
public class Account implements Writable {

    private ArrayList<Collection> collections;
    private String password;
    private String name;

    // REQUIRES : name != null , password != null
    // MODIFIES : this
    // EFFECTS : Creates an account with specified name & password
    public Account(String name, String password) {
        this.name = name;
        this.password = password;
        collections = new ArrayList<>();
    }

    // REQUIRES : collection != null
    // MODIFIES : this
    // EFFECTS : Adds a new collection to list of collections
    public void addCollection(Collection collection) {
        this.collections.add(collection);
    }

    // REQUIRES : collection != null
    // MODIFIES : this
    // EFFECTS : Remove a collection from list of collections
    public void removeCollection(Collection collection) {
        this.collections.remove(collection);
    }


    // REQUIRES : updatedCollection != null , collectionIndex within list size
    // MODIFIES : this
    // EFFECTS : Deletes an existing collection from list at position collectionIndex,
    //           adds new collection to list
    public void updateCollection(Collection updatedCollection, int collectionIndex) {
        collections.remove(collectionIndex);
        collections.add(updatedCollection);
    }

    // Essentially a getter method, tested implicitly
    // This code is based off : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonCollections = new JSONArray();
        int i = 0;
        for (Collection collection : collections) {
            jsonCollections.put(i, collection.toJson());
            i++;
        }
        json.put("collections", jsonCollections);
        json.put("password", password);
        json.put("account", name);
        return json;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Collection> getCollections() {
        return collections;
    }
}
