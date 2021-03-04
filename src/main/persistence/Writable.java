package persistence;

import org.json.JSONObject;

public interface Writable {

    // EFFECTS: returns as a JSON object
    JSONObject toJson();
}
