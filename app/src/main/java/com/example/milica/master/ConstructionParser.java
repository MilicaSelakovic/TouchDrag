package com.example.milica.master;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

public class ConstructionParser {

    public ConstructionParser() {
    }

    private String read(Context context, String filename) {
        StringBuilder sb = new StringBuilder();
        try {


            AssetManager am = context.getAssets();
            InputStream fin = am.open(filename);
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

    public void fillConstructions(HashMap<String, Vector<String>> constructions, Context context, String filename) {
        String konstrukcije = read(context, filename);
        try {

            JSONArray jsonarray = new JSONArray(konstrukcije);
            int n = jsonarray.length();
            for (int i = 0; i < n; i++) {
                JSONObject jsonobject = null;
                jsonobject = jsonarray.getJSONObject(i);
                String problem = jsonobject.getString("problem");
                JSONArray solution = jsonobject.getJSONArray("solution");
                Vector<String> solutions = new Vector<>();
                for (int j = 0; j < solution.length(); j++) {
                    String s = solution.getString(j);
                    solutions.add(s);
                }

                constructions.put(problem, solutions);
            }

            Log.d("size", Integer.toString(constructions.size()));

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}
