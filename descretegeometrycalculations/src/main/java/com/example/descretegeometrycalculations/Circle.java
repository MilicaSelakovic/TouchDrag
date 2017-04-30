package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by milica on 23.11.16..
 */

public class Circle implements GeometricObject {

    private GeomPoint center;
    private double radius;

    private TreeSet<Line> tangentLines = new TreeSet<>();

    public Circle(double x, double y, double r){
        center = new GeomPoint((float) x, (float)  y);
        radius = r;
    }

    public GeomPoint getCenter() {
        return center;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        center.draw(canvas, paint);
        canvas.drawCircle(center.X(), center.Y(), (float) radius, paint);
    }

    @Override
    public String toString() {
        return "Krug  " + Double.toString(radius);
    }

    public boolean pointBelong(GeomPoint point){
        return Math.abs(point.X()*point.X() + point.Y()*point.Y() - radius*radius) <= EPISLON;
    }

    public void connection(GeometricObject object){

    }

    private void connectionLine(Line line){
        // veze kruga i linije
        if(ConnectionCalculations.tangentLine(this, line)){
            tangentLines.add(line);
        }

    }


    //TODO Veze

    // tangenta
    //
}
