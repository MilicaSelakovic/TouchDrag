package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.DocumentsContract;
import android.renderscript.Float2;


class GeomPoint implements GeometricObject {

    private float x;
    private float y;
    private Paint circlePaint;

    private boolean move;

    GeomPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.move = true;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);


    }

    GeomPoint(float x, float y, boolean move) {
        this.x = x;
        this.y = y;
        this.move = move;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

    }




    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        if (move) {
            circlePaint.setColor(Color.BLUE);

        } else {
            circlePaint.setColor(Color.GRAY);
        }
        canvas.drawCircle(x, y, 15f, circlePaint);
    }

    @Override
    public String toString() {
        return "Tacka" + Float.toString(x) + ", " + Float.toString(y);
    }

    public boolean isUnderCursor(float x, float y) {
        //TODO: 28.5.17. konstanta mora da se nasteluje
        double epsilon = 15;
        return move && Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) < epsilon;
    }
    public void translate(float x, float y){
        this.x = x;
        this.y = y;
    }



    public boolean connection(GeometricObject object) {
        return false;
    }

    float X() {
        return x;
    }


    float Y() {
        return y;
    }

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }


    boolean equal(GeomPoint Y){
        // TODO epsilon
        double epsilon = 1e-3;
        return Math.abs(x - Y.X()) < epsilon && Math.abs(y - Y.Y()) < epsilon;

    }

    double distance(GeomPoint X){
        return Math.sqrt((x-X.X())*(x-X.X()) + (y-X.Y())*(y-X.Y()));
    }
}
