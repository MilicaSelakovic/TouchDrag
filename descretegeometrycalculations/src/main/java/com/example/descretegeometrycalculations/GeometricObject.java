package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;


public interface GeometricObject {
    public static final double EPISLON = 10; //TODO nastelovati konstantu
    public abstract void draw(Canvas canvas, Paint paint, boolean finished);

    public abstract boolean connection(GeometricObject object);

}
