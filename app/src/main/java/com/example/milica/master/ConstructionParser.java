package com.example.milica.master;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConstructionParser {

    public ConstructionParser() {
    }

    private String read() {
        StringBuilder sb = new StringBuilder();
        try {

            File fl = new File("");
            FileInputStream fin = new FileInputStream(fl);

            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();

            fin.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();

    }

    public void fillConstructions(HashMap<String, String> constructions) {
        String konstrukcije = read();
        try {

            JSONArray jsonarray = new JSONArray(konstrukcije);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = null;
                jsonobject = jsonarray.getJSONObject(i);
                String problem = jsonobject.getString("problem");
                String solution = jsonobject.getString("solution");
                constructions.put(problem, solution);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}
