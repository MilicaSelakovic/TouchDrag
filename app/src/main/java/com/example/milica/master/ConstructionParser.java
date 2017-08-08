package com.example.milica.master;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ConstructionParser {
    private String konstrukcije;

    public ConstructionParser(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int crt;
        try {
            crt = inputStream.read();

            while (crt != -1) {
                outputStream.write(crt);
                crt = inputStream.read();
            }

            inputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void fillConstructions(HashMap<String, String> constructions) {

    }

}
