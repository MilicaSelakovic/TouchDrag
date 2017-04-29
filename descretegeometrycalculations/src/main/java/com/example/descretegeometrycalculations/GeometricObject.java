package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by milica on 23.11.16..
 */

public interface GeometricObject {
    public static final double EPISLON = 0.001;
    public abstract void draw (Canvas canvas, Paint paint);

    public abstract void connection(GeometricObject object);

}
