package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Vector;


public interface GeometricObject {
    double EPISLON = 10; //TODO nastelovati konstantu

    void draw(Canvas canvas, Paint paint, boolean finished, boolean choose);

    boolean choose(float x, float y, HashMap<String, Vector<String>> trics);

    boolean connection(GeometricObject object, Vector<String> commands);

    void setChoose();

    boolean isUnderCursor(float x, float y);
    void translate(float x, float y);

    public String getId();

    public void setId(String id);


}
