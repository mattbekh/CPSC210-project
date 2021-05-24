package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

/*
    Class which stores a collection of items selected by the user
 */
public class Collection implements Writable {

    private ArrayList<Item> collection;
    private String collectionName;

    // REQUIRES : name not empty
    // MODIFIES : this
    // EFFECTS  : Constructor for a collection, sets collection name to input name and instantiates a list of items
    public Collection(String name) {
        collectionName = name;
        this.collection = new ArrayList<>();
    }

    // This code is based off : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject jsonCollection = new JSONObject();
        JSONArray jsonItems = new JSONArray();
        int i = 0;
        for (Item item : collection) {
            jsonItems.put(i, item.toJson());
            i++;
        }
        jsonCollection.put("collection_items", jsonItems);
        jsonCollection.put("collection_name", collectionName);
        return jsonCollection;
    }

    // REQUIRES : item != null
    // MODIFIES : this
    // EFFECTS  : Adds an item to a list of items
    public void addItem(Item item) {
        collection.add(item);
    }

    // REQUIRES : 0 <= index <= size of collection
    // MODIFIES : this
    // EFFECTS  : Removes an item from a list of items
    public void removeItem(int index) {
        collection.remove(index);
    }

    // REQUIRES : 0 <= index <= size of collection
    // EFFECTS  : returns an item at a specific location in collection
    public Item getItem(int index) {
        return collection.get(index);
    }

    // EFFECTS  : returns true if collection is empty
    public Boolean isEmpty() {
        return collection.isEmpty();
    }

    // REQUIRES : item != null
    // EFFECTS  : returns true if item is in the collection
    public Boolean contains(Item item) {
        return collection.contains(item);
    }

    public String getName() {
        return collectionName;
    }

    public ArrayList<Item> getItems() {
        return collection;
    }

    public int getSize() {
        return collection.size();
    }

    @Override
    public String toString() {
        return collectionName;
    }



    // Singleton Support
//    private static Collection instance;

//    public static Collection getInstance() {
//        if (instance == null) {
//            instance = new Collection();
//        }
//        return instance;
//    }
//
//    private Collection() {
//        this.collection = new ArrayList<Item>();
//    }
}
