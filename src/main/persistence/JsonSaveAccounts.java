package persistence;

import model.AccountManager;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*
    Class which writes JSON representation of the accounts + associated data to file

    This code is based off : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonSaveAccounts {
    private PrintWriter writer;
    private String destination;

    // EFFECTS  : constructs a saver to save to file destination
    public JsonSaveAccounts(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of accounts + associated data to file
    public void write(AccountManager manager) {
        JSONObject json = manager.toJson();
        saveToFile(json.toString());
    }

    // MODIFIES: this
    // EFFECTS: saves string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
