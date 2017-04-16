package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Vector;

/**
 * Created by milica on 23.11.16..
 */

public class Circle implements GeometricObject {
    private double centerx;
    private double centery;
    private double radius;

    public Circle(double x, double y, double r){
        centerx = x;
        centery = y;
        radius = r;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        canvas.drawCircle((float) centerx, (float) centery, (float) radius, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawPoint((float) centerx, (float) centery, paint);
    }

    @Override
    public String toString() {
        return "Krug  " + Double.toString(radius);
    }
}
