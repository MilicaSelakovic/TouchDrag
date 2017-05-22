package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


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
        canvas.drawCircle(x, y, 10f, circlePaint);
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean equal(GeomPoint Y){
        // TODO epsilon
        double epsilon = 1e-3;
        if( Math.abs(x - Y.X()) < epsilon && Math.abs(y-Y.Y()) <epsilon){
            return true;
        }

        return false;
    }

}
