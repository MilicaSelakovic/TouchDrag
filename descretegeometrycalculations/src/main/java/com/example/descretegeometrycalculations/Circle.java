package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Vector;

public class Circle implements GeometricObject {


    private GeomPoint center;

    private String id;

    private double radius;


    Circle(double x, double y, double r) {
        center = new GeomPoint((float) x, (float) y);
        radius = r;
    }

    Circle(GeomPoint X, double r){
        this.center = X;
        this.radius = r;
    }

    public GeomPoint getCenter() {
        return center;
    }

    public void setCenter(GeomPoint center) {
        this.center = center;
    }


    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public void setChoose() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose) {
        if (finished)
            center.draw(canvas, paint, finished, choose);
        canvas.drawCircle(center.X(), center.Y(), (float) radius, paint);
    }

    @Override
    public String toString() {
        return "Krug  " + Double.toString(radius);
    }

    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {
        return false;
    }


    public boolean connection(GeometricObject object, Vector<String> commands) {
        return false;
    }


    public boolean isUnderCursor(float x, float y){
       return false;
    }

    public void translate(float x, float y) {
    }



}
