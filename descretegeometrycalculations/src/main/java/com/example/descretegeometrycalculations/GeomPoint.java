package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Vector;


public class GeomPoint implements GeometricObject {

    private float x;
    private float y;
    private Paint circlePaint;

    private boolean move;


    private int canChoose;

    private String id;

    GeomPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.move = true;
        this.canChoose = 0;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);


    }

    GeomPoint(float x, float y, boolean move) {
        this.x = x;
        this.y = y;
        this.move = move;
        this.canChoose = 0;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCanChoose(int canChoose) {
        this.canChoose = canChoose;
    }


    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose) {
        if (move) {
            circlePaint.setColor(Color.BLUE);

        } else {
            circlePaint.setColor(Color.GRAY);
        }

        if (choose) {
            switch (canChoose) {
                case -1:
                    circlePaint.setColor(Color.RED);
                    break;
                case 0:
                    circlePaint.setColor(Color.DKGRAY);
                    break;
                case 1:
                    circlePaint.setColor(Color.rgb(27, 226, 98));
                    break;
                case 2:
                    circlePaint.setColor(Color.YELLOW);
                    break;
                case 3:
                    circlePaint.setColor(Color.MAGENTA);
                    break;
            }


        }

        canvas.drawCircle(x, y, 20f, circlePaint);
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
    }


    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {
        return false;
    }

    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {
        if (object instanceof Line) {
            Line l = (Line) object;
            if (l.contain(this)) {
                if (l.getBegin().distance(this) < l.getEnd().distance(this)) {
                    l.setEnd(this);
                } else {
                    l.setBegin(this);
                }
            }

        }

        return false;
    }

    @Override
    public void setChoose() {

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
}
