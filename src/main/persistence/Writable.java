package persistence;

import org.json.JSONObject;

// Citation: Code obtained from JsonSerializationDemo
//           https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {

    // EFFECTS: returns as a JSON object
    JSONObject toJson();
}
