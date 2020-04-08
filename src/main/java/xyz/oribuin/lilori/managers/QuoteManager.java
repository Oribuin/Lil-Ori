package xyz.oribuin.lilori.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuoteManager {

    private static final Gson gson = new Gson();
    public Map<Integer, String> jsonMap = new HashMap<>();

    public void load(String json) {
        jsonMap = gson.fromJson(json, new TypeToken<Map<Integer, String>>(){}.getType());
    }

    public void save() throws IOException {
        File file = new File("data", "quotes.json");
        FileWriter fileWriter = new FileWriter(file);
        gson.toJson(jsonMap, fileWriter);
    }
}
