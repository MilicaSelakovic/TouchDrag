package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by milica on 27.11.16..
 */

public class GeomPoint implements GeometricObject {
    private float x;
    private float y;
    private Paint circlePaint;

    public GeomPoint(float x, float y) {
        this.x = x;
        this.y = y;

        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        canvas.drawCircle(x, y, 15f, circlePaint);
    }

    @Override
    public String toString() {
        return "Tacka";
    }


    public void connection(GeometricObject object) {

    }

    public float X() {
        return x;
    }


    public float Y() {
        return y;
    }
}
