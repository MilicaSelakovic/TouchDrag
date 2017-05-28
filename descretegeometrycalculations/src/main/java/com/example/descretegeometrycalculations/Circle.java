package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Vector;

class Circle implements GeometricObject {

    private GeomPoint center;
    private double radius;

    private Vector<Line> tangentLines = new Vector<>();

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

    double getRadius() {
        return radius;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        if (finished)
            center.draw(canvas, paint, false);
        canvas.drawCircle(center.X(), center.Y(), (float) radius, paint);
    }

    @Override
    public String toString() {
        return "Krug  " + Double.toString(radius);
    }

     boolean pointBelong(GeomPoint point) {
        double x = point.X() - center.X();
        double y = point.Y() - center.Y();
        double d = Math.sqrt(x * x + y * y) - radius;
        return Math.abs(d) <= EPISLON; // TODO promeniti ovu konstantu
    }

    public boolean connection(GeometricObject object) {
        if (object instanceof Line) {
            connectionLine((Line) object);
        }


        return false;

    }

    public boolean isUnderCursor(float x, float y){
       return false;
    }

    public void translate(float x, float y){

    }


    private void connectionLine(Line line) {

        if (ConnectionCalculations.tangentLine(this, line)) {
            tangentLines.add(line);
//            Log.d("Tangenta", "da");
        }

    }


}
