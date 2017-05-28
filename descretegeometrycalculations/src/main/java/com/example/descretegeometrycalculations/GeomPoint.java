package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class GeomPoint implements GeometricObject {

    private float x;
    private float y;
    private Paint circlePaint;

    private boolean move;

    public GeomPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.move = true;

        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

    }

    public GeomPoint(float x, float y, boolean move) {
        this.x = x;
        this.y = y;
        this.move = move;

        circlePaint = new Paint();
        if(move)
            circlePaint.setColor(Color.BLUE);
        else
            circlePaint.setColor(Color.DKGRAY);

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


    public boolean connection(GeometricObject object) {
        return false;
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


    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }


    public boolean equal(GeomPoint Y){
        // TODO epsilon
        double epsilon = 1e-3;
        if( Math.abs(x - Y.X()) < epsilon && Math.abs(y-Y.Y()) <epsilon){
            return true;
        }

        return false;
    }

    public double distance(GeomPoint X){
        return Math.sqrt((x-X.X())*(x-X.X()) + (y-X.Y())*(y-X.Y()));
    }
}
