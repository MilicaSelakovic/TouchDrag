package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;


public interface GeometricObject {
    double EPISLON = 10; //TODO nastelovati konstantu
    void draw(Canvas canvas, Paint paint, boolean finished);

    boolean connection(GeometricObject object);

    boolean isUnderCursor(float x, float y);
    void translate(float x, float y);

    public String getId();

    public void setId(String id);


}
