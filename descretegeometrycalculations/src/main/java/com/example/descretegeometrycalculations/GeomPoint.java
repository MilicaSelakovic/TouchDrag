package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class GeomPoint implements GeometricObject {

    private float x;
    private float y;
    private Paint circlePaint;

    private boolean move;


    public enum Type {
        TRIANGLE_FREE, TRIANGLE_CANFREE, TRIANGLE_CANNOTFREE, OTHER;
    }

    private Type type;


    private Triangle triangle;
    private String label;

    private String id;

    GeomPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.move = true;
        this.type = Type.OTHER;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        triangle = null;
        label = "";


    }

    GeomPoint(float x, float y, boolean move) {
        this.x = x;
        this.y = y;
        this.move = move;
        this.type = Type.OTHER;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        triangle = null;
        label = "";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (label.compareTo("") == 0)
            label = "P" + id;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setLabel(String l) {
        label = l;
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
    }


    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose) {
        if (move) {
            circlePaint.setColor(Color.BLUE);

        } else {
            circlePaint.setColor(Color.GRAY);
        }

        if (choose) {
            switch (type) {
                case TRIANGLE_CANNOTFREE:
                    circlePaint.setColor(Color.RED);
                    break;
                case OTHER:
                    circlePaint.setColor(Color.DKGRAY);
                    break;
                case TRIANGLE_FREE:
                    circlePaint.setColor(Color.rgb(27, 226, 98));
                    break;
                case TRIANGLE_CANFREE:
                    circlePaint.setColor(Color.YELLOW);
                    break;
            }


        }

        canvas.drawCircle(x, y, 20f, circlePaint);
        circlePaint.setTextSize(50);
        canvas.drawText(label, x + 20f, y + 20f, circlePaint);
    }

    @Override
    public String toString() {
        return "Tacka" + Float.toString(x) + ", " + Float.toString(y);
    }

    public boolean isUnderCursor(float x, float y) {
        //TODO: 28.5.17. konstanta mora da se nasteluje
        double epsilon = 20;
        return move && Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) < epsilon;
    }

    public boolean underCursor(float x, float y) {
        //TODO: 28.5.17. konstanta mora da se nasteluje
        double epsilon = 20;
        return Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) < epsilon;
    }


    public void translate(float x, float y){
        this.x = x;
        this.y = y;

        if (triangle != null) {
            triangle.translate(x, y);
        }
    }

    @Override
    public void scale(float scaleFactor) {
        x *= scaleFactor;
        y *= scaleFactor;
    }


    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {
        return false;
    }

    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {
        if (object instanceof Line) {
            Line l = (Line) object;
            if (l.contain(this)) {

                for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
                    if (entry.getValue() instanceof GeomPoint && entry.getValue() != this &&
                            l.contain((GeomPoint) entry.getValue())) {
                        l.setBegin(this);
                        l.setEnd((GeomPoint) entry.getValue());

                        commands.add("w02 " + object.getId() + " " + getId() + " " + entry.getValue().getId());
                        return true;
                    }
                }

                if (l.getBegin().distance(this) < l.getEnd().distance(this)) {
                    l.setEnd(this);
                } else {
                    l.setBegin(this);
                }


            }

        }

        return false;
    }

    public float X() {
        return x;
    }


    public float Y() {
        return y;
    }

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    void setMove(boolean value) {
        move = value;
    }


    boolean equal(GeomPoint Y){
        // TODO epsilon
        double epsilon = 10;
        return Math.abs(x - Y.X()) < epsilon && Math.abs(y - Y.Y()) < epsilon;

    }

    double distance(GeomPoint X){
        return Math.sqrt((x-X.X())*(x-X.X()) + (y-X.Y())*(y-X.Y()));
    }

    public void setFixed(HashMap<String, Vector<String>> trics) {
        if (type == Type.TRIANGLE_FREE) {
            triangle.fix(this, trics);
        }

    }

    public void setFree(HashMap<String, Vector<String>> trics) {
        if (type == Type.TRIANGLE_CANFREE) {
            triangle.free(this, trics);
        }
    }
}
