package persistence;

import org.json.JSONObject;

// Interface for writing to JSONObject
// This code is based off : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
